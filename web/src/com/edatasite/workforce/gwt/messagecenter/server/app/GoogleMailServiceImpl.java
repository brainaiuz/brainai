package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.controllers.WebSocketServletImpl;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.MessageCenterCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.EmailSessionCache;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.gson.Gson;
import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.SharedByteArrayInputStream;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service("gmailService")
@Priority(0)
public class GoogleMailServiceImpl extends AbstractMailServiceImpl implements GoogleMailServiceLocal {
    private static final Logger log = LoggerFactory.getLogger(GoogleMailServiceImpl.class);

    @Autowired
    private GoogleManager googleManager;
    @Autowired
    private RabbitMQService rabbitMQService;

    private Gmail getService(String token) throws GeneralSecurityException, IOException {
        GoogleCredential credential = googleManager.getGoogleCredential(token);
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("kpi.com").build();
    }

    @Override
    Session initSession(EdsEmailSetting setting) {
        if (EmailSessionCache.contains(setting.getSessionKey())) {
            return EmailSessionCache.get(setting.getSessionKey());
        } else {
            Properties properties = new Properties();
            Session session = Session.getDefaultInstance(properties);

            EmailSessionCache.put(setting.getSessionKey(), session);
            return session;
        }
    }

    @Override
    public void saveToken(String token) throws GeneralSecurityException, IOException {
        EdsUser user = emailSettingsManager.getUser();

        Gmail service = getService(token);
        Profile profile = service.users().getProfile("me").execute();
        String email = profile.getEmailAddress();

        EmailAccountItem accountItem = new EmailAccountItem();
        accountItem.setEmail(email);
        accountItem.setProvider(EdsEmailSetting.Provider.GOOGLE.name());
        accountItem.setRefreshToken(token);

        WebSocketServerObject message = new WebSocketServerObject();
        message.setUserId(user.getObjectID());
        message.setData(new Gson().toJson(accountItem));
        message.setEventType(WfmUiEventType.ON_EMAIL_ACCOUNT_SUCCESS);

        WebSocketServletImpl.sendMessage(message.getUserId(), message);
        log.info("Registering gmail account [CID:{}]: {}", SecurityContext.getCompanyID(), email);


    }

    @Override
    public Integer fetchEmails(Integer folderId, Integer companyId, Integer emailSettingsId, Store store) throws Exception {

        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingsId);
        if (emailSetting == null || !emailSetting.isActive()) {
            return null;
        }
        EdsEmailFolder edsFolder = emailFolderManager.get(folderId);
        if (edsFolder == null) {
            return null;
        }


        Long nextUid = emailRepository.getLastFetchedMessagesUID(folderId) + 1;
        if (nextUid <= 1) {//temp solution for not fetching existing emails
            nextUid = edsFolder.getNextUID();
        }
        String nextUidHex = Long.toHexString(nextUid);

        List<String> labelIds = Collections.singletonList(edsFolder.getUrl());
        try {
            Gmail service = getService(emailSetting.getRefreshToken());
            Session emailSession = initSession(emailSetting);

            List<Message> messages = new ArrayList<>();
            fetchMessages(service, messages, labelIds, nextUidHex, null);

            if (messages.isEmpty()) {
                return 0;
            }

            messages.sort(Comparator.comparing(Message::getId));

            List<String> fetchedEmails = new ArrayList<>();
            int total = messages.size();
            int fetchSize = 100;

            for (int i = 0; i < total; i += fetchSize) {
                int end = Math.min(i + fetchSize, total);
                List<Message> subMessages = messages.subList(i, end);

                Map<Long, jakarta.mail.Message> messageMap = new HashMap<>();
                BatchRequest batch = service.batch();

                for (Message message : subMessages) {
                    service.users().messages().get("me", message.getId()).setFormat("raw")
                            .queue(batch, new JsonBatchCallback<Message>() {

                                @Override
                                public void onFailure(GoogleJsonError error, HttpHeaders headers) {
                                    log.error("Gmail message fetch failed [{}], messageId: {}", emailSetting.getEmail(), message.getId());
                                }

                                @Override
                                public void onSuccess(Message message, HttpHeaders headers) {
                                    try {
                                        byte[] emailBytes = Base64.decodeBase64(message.getRaw());
                                        MimeMessage mimeMessage = new MimeMessage(emailSession, new ByteArrayInputStream(emailBytes));

                                        boolean unread = message.getLabelIds().contains("UNREAD");
                                        mimeMessage.setFlag(Flags.Flag.SEEN, !unread);

                                        messageMap.put(Long.parseLong(message.getId(), 16), mimeMessage);
                                    } catch (MessagingException e) {
                                        log.error("MimeMessage convert failed [{}], messageId: {}", emailSetting.getEmail(), message.getId(), e);
                                    }
                                }
                            });
                }
                batch.execute();

                fetchedEmails.addAll(createEmails(messageMap, emailSettingsId, folderId, companyId));
            }

            return fetchedEmails.size();

        } catch (GeneralSecurityException e) {
            log.error("Gmail security error [{}]", emailSetting.getEmail(), e);
            throw e;
        } catch (IOException e) {
            log.error("Gmail IO error [{}]", emailSetting.getEmail(), e);
            throw e;
        } catch (MessagingException e) {
            log.error("Gmail messaging error [{}]", emailSetting.getEmail(), e);
            throw e;
        }
    }

    private void fetchMessages(Gmail service, List<Message> messages, List<String> labelIds, String fromUidHex, String pageToken) throws IOException {
        ListMessagesResponse response = service.users().messages().list("me").setLabelIds(labelIds).setPageToken(pageToken).execute();
        if (response.getMessages() != null) {
            List<Message> subMessages = new ArrayList<>();

            for (Message message : response.getMessages()) {
                String uidHex = message.getId();
                if (uidHex.compareTo(fromUidHex) > 0) {
                    subMessages.add(message);
                } else {
                    return;
                }
            }
            messages.addAll(subMessages);

            if (response.getNextPageToken() != null) {
                fetchMessages(service, messages, labelIds, fromUidHex, response.getNextPageToken());
            }
        }
    }

    protected void send(EdsEmailSetting settings, KPIMimeMessage mimeMessage) throws MessagingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            mimeMessage.writeTo(baos);
            String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedEmail);

            Gmail service = getService(settings.getRefreshToken());

            message = service.users().messages().send("me", message).execute();
        } catch (IOException | GeneralSecurityException e) {
            throw new MessagingException(e.getMessage());
        }
    }

    void appendMessage(EdsEmailSetting settings, MimeMessage message, MCFolderType type) throws MessagingException {

    }

    @Override
    public Email getWithContent(EdsEmail edsEmail) {
        if (edsEmail != null) {
            Email mailItem = new Email();
            KPIMimeMessage mimeMessage = null;
            EdsEmailFolder folder = emailFolderManager.get(edsEmail.getFolderId());
            EdsEmailSetting emailSetting = emailSettingsManager.get(edsEmail.getEmailSettingId());
            mailItem.setDeleted(true);
            if (edsEmail.getTrackerId() != null) {
                mailItem.setCaseID(emailTrackerManager.getCaseIDByTrackerID(edsEmail.getTrackerId()));
            }
            if (emailSetting != null) {
                Session emailSession = initSession(emailSetting);
                try {
                    if (edsEmail.getEmailContentId() != null) {
                        EdsUpload upload = (EdsUpload) uploadManager.get(edsEmail.getEmailContentId());
                        InputStream inputStream = uploadManager.getInputStream(upload);
                        if (inputStream != null) {
                            mimeMessage = new KPIMimeMessage(emailSession, inputStream);
                        }
                    }
                    if (mimeMessage == null) {
                        mimeMessage = getKpiMimeMessage(edsEmail, mimeMessage, emailSetting, emailSession);
                        emailRepository.save(edsEmail);
                    }
                } catch (MessagingException e) {
                    log.error(e.getMessage());
                }
                if (mimeMessage != null) {
                    try {
                        String subject = mimeMessage.getSubject();
                        String content = EmailUtils.retrieveContent(mimeMessage.getContent(), mimeMessage.getContentType(), new StringBuilder(), null).toString();
                        String to = getMessageRecipients(mimeMessage, MimeMessage.RecipientType.TO);
                        String cc = getMessageRecipients(mimeMessage, MimeMessage.RecipientType.CC, emailSetting.getEmail());
                        String bcc = getMessageRecipients(mimeMessage, MimeMessage.RecipientType.BCC);
                        mailItem.setDeleted(false);
                        mailItem.setObjectID(edsEmail.getId());
                        mailItem.setSubject(subject);
                        mailItem.setContent(content);
                        mailItem.setFromEmailWithName(edsEmail.getFrom());
                        mailItem.setTrackerID(edsEmail.getTrackerId());
                        mailItem.setToEmails(to);
                        mailItem.setToEmailWithName(to);
                        mailItem.setCc(cc);
                        mailItem.setBcc(bcc);
                        mailItem.setDate(mimeMessage.getSentDate() == null ? edsEmail.getCreatedDate() : mimeMessage.getSentDate());
                        mailItem.setReceivedDate(mimeMessage.getReceivedDate());
                        if (folder != null) {
                            mailItem.setType(folder.getType());
                        }
                        mailItem.setAttachment(edsEmail.isHasAttachment());
                        mailItem.setAttachments(EdsEmailAttachment.asFileResourses(emailAttachmentManager.getEmailAttachments(edsEmail.getId())));
                        mailItem.setSubject(edsEmail.getSubject());
                        mailItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMAIL_TRACKER, mailItem.getTrackerID())));
                        if (!edsEmail.isRead() && !MCFolderType.DRAFT.equals(mailItem.getType())) {
                            EdsBusinessEvent event = baseEventsPostProcessor.registerEvent(MessageCenterCustomEventListenerImpl.TYPE, EdsEmail.FLAG_READ, emailSetting, userManager.getUser());
                            event.setCustomStringField(mailItem.getObjectID());
                            mailItem.setUnreadStatusChanged(true);
                        }
                        edsEmail.setRead(true);
                        emailRepository.save(edsEmail);
                    } catch (MessagingException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mailItem.setDeleted(true);
                    edsEmail.setDeleted(true);
                    emailRepository.save(edsEmail);
                    if (!MCFolderType.DRAFT.equals(mailItem.getType())) {
                        EdsBusinessEvent event = baseEventsPostProcessor.registerEvent(MessageCenterCustomEventListenerImpl.TYPE, EdsEmail.FLAG_DELETED, emailSetting, userManager.getUser());
                        event.setCustomStringField(mailItem.getObjectID());
                    }
                }
            } else if (!edsEmail.isFetched()) {
                mailItem = edsEmail.getRPC();
                mailItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMAIL_TRACKER, mailItem.getTrackerID())));
            }
            return mailItem;
        }
        return null;
    }

    private KPIMimeMessage getKpiMimeMessage(EdsEmail edsEmail, KPIMimeMessage mimeMessage, EdsEmailSetting emailSetting, Session emailSession) {
        try {
            Gmail service = getService(emailSetting.getRefreshToken());
            String uidHex = Long.toHexString(edsEmail.getMessageUID());
            Message gMessage = service.users().messages().get("me", uidHex).setFormat("raw").execute();

            byte[] emailBytes = Base64.decodeBase64(gMessage.getRaw());
            InputStream stream = new ByteArrayInputStream(emailBytes);

            mimeMessage = new KPIMimeMessage(emailSession, stream);
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            log.error("while getting mimeMessage from folder throwed:\n{}", e.getMessage());
        }
        return mimeMessage;
    }

    public KPIMimeMessage toMimeMessage(EdsEmail email) {
        if (email != null) {
            KPIMimeMessage mimeMessageClone;
            KPIMimeMessage mimeMessage = null;
            EdsEmailSetting emailSetting = emailSettingsManager.get(email.getEmailSettingId());
            Session emailSession = initSession(emailSetting);
            if (email.getEmailContentId() != null) {
                EdsUpload upload = (EdsUpload) uploadManager.get(email.getEmailContentId());
                InputStream stream = uploadManager.getInputStream(upload);
                if (stream != null) {
                    try {
                        mimeMessage = new KPIMimeMessage(emailSession, stream);
                    } catch (MessagingException e) {
                        log.error("while converting MimeMessage to KPIMimeMessage throwed:\n{}", e.getMessage());
                    }
                }
            }
            if (mimeMessage == null) {
                mimeMessage = getKpiMimeMessage(email, mimeMessage, emailSetting, emailSession);
            }
            if (mimeMessage != null) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    mimeMessage.writeTo(bos);
                    bos.close();
                    SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
                    mimeMessageClone = new KPIMimeMessage(emailSession, bis);
                    bis.close();
                    return mimeMessageClone;
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void createFolders(EdsEmailSetting emailSetting, boolean isFirstTime) {
        try {
            Gmail service = getService(emailSetting.getRefreshToken());

            ListLabelsResponse response = service.users().labels().list("me").execute();
            Set<String> foldersToSave = new HashSet<>();

            for (Label label : response.getLabels()) {
                if (!"labelHide".equals(label.getLabelListVisibility())) {
                    createFolder(label, emailSetting, isFirstTime);
                    foldersToSave.add(label.getName());
                }
            }
            if (!foldersToSave.isEmpty()) {
                emailFolderManager.deleteFolders(emailSetting.getObjectID(), foldersToSave);
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private EdsEmailFolder createFolder(Label label_, EdsEmailSetting emailSetting, boolean isFirstTime) {
        EdsEmailFolder edsFolder = label_.getId() != null ? emailFolderManager.getByURL(label_.getId(), emailSetting.getObjectID()) : null;
        if (edsFolder != null) {
            return edsFolder;
        } else {
            edsFolder = new EdsEmailFolder();
            edsFolder.setEmailSetting(emailSetting);
            edsFolder.setName(label_.getName());
            edsFolder.setFullName(label_.getName());
            edsFolder.setUrl(label_.getId());
            if (StringUtils.isNotEmpty(label_.getName())) {
                if (label_.getName().toLowerCase().contains("sent")) {
                    edsFolder.setType(MCFolderType.SENT);
                    edsFolder.setFetchable(true);
                } else if (label_.getName().toLowerCase().contains("trash")) {
                    edsFolder.setType(MCFolderType.TRASH);
                    edsFolder.setFetchable(false);
                } else if (label_.getName().toLowerCase().contains("draft")) {
                    edsFolder.setType(MCFolderType.DRAFT);
                    edsFolder.setFetchable(false);
                } else {
                    edsFolder.setType(MCFolderType.INBOX);
                    edsFolder.setFetchable(true);
                }
            }
            log.info("Folder created.({}), companyID:{}, email:{}", edsFolder.getName(), SecurityContext.getCompanyID(), emailSetting.getEmail());
            emailFolderManager.createOrUpdate(edsFolder);
            emailSetting.getFolders().add(edsFolder);
            return edsFolder;
        }
    }

    @Override
    public void setFlags(Set<Email> emails, Integer emailSettingID, String flag) {
        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingID);

        try {
            Gmail service = getService(emailSetting.getRefreshToken());
            BatchModifyMessagesRequest batchRequest = new BatchModifyMessagesRequest();
            batchRequest.setIds(emails.stream().map(Email::getMessageUIDHex).collect(Collectors.toList()));
            if (EdsEmail.FLAG_DELETED.equals(flag)) {
                batchRequest.setAddLabelIds(Collections.singletonList("TRASH"));
            } else if (EdsEmail.FLAG_READ.equals(flag)) {
                batchRequest.setRemoveLabelIds(Collections.singletonList("UNREAD"));
            } else {
                batchRequest.setAddLabelIds(Collections.singletonList("UNREAD"));
            }
            service.users().messages().batchModify("me", batchRequest).execute();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProvider() {
        return EdsEmailSetting.Provider.GOOGLE.name();
    }

    @Override
    public boolean supports(String provider) {
        return getProvider().equals(provider);
    }
}
