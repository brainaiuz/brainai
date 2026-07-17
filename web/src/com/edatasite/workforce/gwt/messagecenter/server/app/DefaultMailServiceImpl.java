package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.MessageCenterCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.EmailConnectionUtils;
import com.edatasite.workforce.gwt.core.server.utils.EmailSessionCache;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.utils.EdsContextParams;
import jakarta.activation.CommandMap;
import jakarta.activation.MailcapCommandMap;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.MessageIDTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.util.SharedByteArrayInputStream;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.angus.mail.imap.IMAPFolder;
import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * All email fetching logic must be stored here. It makes the system
 * more  understandable  and  flexible. Now the system possesses two
 * sections that use email fetching.They are Message Center and Case
 * Management. Maybe in  future it will increase, but all conception
 * has to be stored here.
 * <p/>
 * For further info please refer Ruslan Muhammadov.
 */

@Transactional
@Service("genericEmailService")
@Priority(Ordered.LOWEST_PRECEDENCE)
public class DefaultMailServiceImpl extends AbstractMailServiceImpl implements DefaultMailServiceLocal {
    private static final Logger log = LoggerFactory.getLogger(DefaultMailServiceImpl.class);

    public Transport getTransporter(EdsEmailSetting edsEmailSetting) throws MessagingException {
        Session session = initSession(edsEmailSetting);
        Transport transport = session.getTransport();
        if (edsEmailSetting.isSmtpAuth()) {
            transport.connect(edsEmailSetting.getEmailHostSMTP(), edsEmailSetting.getEmailPortSMTP() == null ? -1 : edsEmailSetting.getEmailPortSMTP(), edsEmailSetting.getUserName(), EncryptionHelper.decrypt(edsEmailSetting.getPassword()));
        } else {
            transport.connect();
        }
        return transport;
    }

    public Session initSession(EdsEmailSetting edsEmailSetting) {
        return EmailSessionCache.contains(edsEmailSetting.getSessionKey())
                ? EmailSessionCache.get(edsEmailSetting.getSessionKey())
                : EmailConnectionUtils.getEmailSession(edsEmailSetting);
    }

    /**
     * We are just simply getting the result through map.
     *
     * @return - returns email session and store which both are wrapped in HashMap.
     */
    /**
     * Connects to or retrieves an email store, with retry logic if disconnected.
     */
    public static Store connectAndGetEmailStore(EdsEmailSetting edsEmailSetting) throws MessagingException {
        String sessionKey = edsEmailSetting.getSessionKey();
        Store store;

        // Retrieve or create the Store
        if (EmailSessionCache.containsKey(sessionKey)) {
            store = EmailSessionCache.get(sessionKey).getStore();
            log.info("Store retrieved from cache for '{}'", sessionKey);
        } else {
            store = EmailConnectionUtils.getStore(edsEmailSetting);
            log.info("Store created for '{}'", sessionKey);
        }

        if (!store.isConnected()) {
            log.info("Store is disconnected for '{}'. Attempting to reconnect with retries...", sessionKey);
            try {
                reconnectStoreWithRetries(store, edsEmailSetting);
            } catch (MessagingException e) {
                log.error("All reconnection attempts failed for '{}': {}", sessionKey, e.getMessage(), e);
                // Remove invalid Store from cache and create a new one
                EmailSessionCache.remove(sessionKey);
                store = EmailConnectionUtils.getStore(edsEmailSetting);
                log.info("New store created after reconnection failure for '{}'", sessionKey);
            }
        }

        // Final validation
        if (!store.isConnected()) {
            log.error("Store is not connected after all attempts for '{}'", sessionKey);
        }

        return store;
    }

    /**
     * Attempts to reconnect the Store with delays of 0s (immediate), 10s, and 25s.
     */
    private static void reconnectStoreWithRetries(Store store, EdsEmailSetting edsEmailSetting) throws MessagingException {
        int[] delays = {0, 15, 25}; // Delays in seconds: immediate, 15s, 25s
        for (int attempt = 0; attempt < delays.length; attempt++) {
            try {
                // Wait before retrying (skip delay for the first attempt)
                if (attempt > 0) {
                    log.info("Waiting {} seconds before next reconnection attempt for '{}'", delays[attempt], edsEmailSetting.getSessionKey());
                    Thread.sleep(delays[attempt] * 1000);
                }

                // Attempt to reconnect
                String host = edsEmailSetting.getEmailHost();
                String user = edsEmailSetting.getUserName();
                String password = EncryptionHelper.decrypt(edsEmailSetting.getPassword());
                Integer port = edsEmailSetting.getEmailPort();

                if (port != null) {
                    log.info("Reconnecting store for '{}' with port {} (attempt {})", edsEmailSetting.getSessionKey(), port, attempt + 1);
                    store.connect(host, port, user, password);
                } else {
                    log.info("Reconnecting store for '{}' without port (attempt {})", edsEmailSetting.getSessionKey(), attempt + 1);
                    store.connect(host, user, password);
                }

                // Check if connected successfully
                if (store.isConnected()) {
                    log.info("Successfully reconnected store for '{}'", edsEmailSetting.getSessionKey());
                    // Exit if connection is successful
                    return;
                }
            } catch (InterruptedException e) {
                // Restore interrupted status
                Thread.currentThread().interrupt();
                log.warn("Reconnection attempt interrupted for '{}'", edsEmailSetting.getSessionKey());
                throw new MessagingException("Reconnection interrupted", e);
            } catch (MessagingException e) {
                log.warn("Reconnection attempt {} failed for '{}': {}", attempt + 1, edsEmailSetting.getSessionKey(), e.getMessage());
                if (attempt == delays.length - 1) {
                    throw e;
                }
            }
        }
    }
    @Override
    public Integer fetchEmails(Integer folderId, Integer companyId, Integer emailSettingsId, Store store) throws Exception {
        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingsId);
        if (emailSetting == null || !emailSetting.isActive()) {
            return null;
        }

        Folder folder = getFolder(folderId, store);

        if (folder == null) {
            log.warn("Folder is not found " + folderId);
            return null;
        }

        Map<Long, Message> messageMap = downloadMessages(folderId, folder);

        List<String> emails = createEmails(messageMap, emailSettingsId, folderId, companyId);

        if (folder.isOpen()) {
            folder.close(false);
        }

        return emails.size();
    }

    private Map<Long, Message> downloadMessages(Integer folderId, Folder folder) throws MessagingException {
        Map<Long, Message> map = new TreeMap<>();
        EdsEmailFolder edsFolder = emailFolderManager.get(folderId);

        Long nextUID = emailRepository.getLastFetchedMessagesUID(folderId) + 1;
        if (nextUID <= 1) {//temp solution for not fetching existing emails
            nextUID = edsFolder.getNextUID();
        }

        LinkedHashSet<Message> messages = new LinkedHashSet<>();
        Integer fetchSize = null;
        try {
            fetchSize = Integer.valueOf(SpringPropertiesUtil.getProperty("rabbitmq.fetch.size"));
        } catch (NumberFormatException ex) {
            fetchSize = 20;
        }
        fetchMessages(messages, (IMAPFolder) folder, nextUID, fetchSize);

        if (messages.size() == 0) {
            return map;
        }

        FetchProfile fp = new FetchProfile();
        fp.add(IMAPFolder.FetchProfileItem.HEADERS);
        folder.fetch(messages.toArray(new Message[]{}), fp);

        for (Message message : messages) {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_ONLY);
            }
            long uId = ((IMAPFolder) folder).getUID(message);
            map.put(uId, message);
        }
        return map;
    }

    private void fetchMessages(LinkedHashSet<Message> messages, IMAPFolder folder, Long fromUID, Integer fetchSize) throws MessagingException {
        int interval = fetchSize - messages.size();
        Message[] metaMessages;
        long toUID = fromUID + interval;
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        metaMessages = folder.getMessagesByUID(fromUID, toUID);
        messages.addAll(Arrays.asList(metaMessages));

        if (metaMessages.length < interval && (metaMessages = folder.getMessagesByUID(toUID, IMAPFolder.MAXUID)).length > 0) {
            fetchMessages(messages, folder, folder.getUID(metaMessages[0]), fetchSize);
        }
    }

    private Folder getFolder(Integer folderId, Store store) {
        if (folderId == null) {
            return null;
        }

        EdsEmailFolder edsFolder = emailFolderManager.get(folderId);
        if (edsFolder == null) {
            return null;
        }

        try {
            Folder folder = store.getFolder(edsFolder.getFullName());
            if (folder != null && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                if (!folder.isOpen()) {
                    folder.open(Folder.READ_ONLY);
                }
                return folder;
            } else {
                log.warn("Folder unsuitable: {}, exists={}, holdsMessages={}", edsFolder.getFullName(), folder != null, folder != null ? (folder.getType() & Folder.HOLDS_MESSAGES) != 0 : false);
//                edsFolder.setFetchable(false);
                emailFolderManager.update(edsFolder);
            }
        } catch (Exception e) {
            log.error("Error accessing folder: {}", edsFolder.getFullName(), e);
//            edsFolder.setFetchable(false);
            if (e instanceof StoreClosedException) {
                edsFolder.setFetchRejectReason("Store Closed");
            } else if (e instanceof FolderNotFoundException) {
                edsFolder.setFetchRejectReason("Your  " + edsFolder.getFullName() + " is not found ");
            } else {
                edsFolder.setFetchRejectReason("Your  " + edsFolder.getFullName() + " is unsuitable for synchronization ");
            }
            emailFolderManager.update(edsFolder);
        }
        return null;
    }
    protected void send(EdsEmailSetting settings, KPIMimeMessage message) throws MessagingException {
        Transport transport = getTransporter(settings);
        if (transport != null) {
            transport.sendMessage(message, message.getAllRecipients());
        } else {
            Transport.send(message);
        }
    }

    protected void appendMessage(EdsEmailSetting settings, MimeMessage message, MCFolderType type) throws MessagingException {
        if (settings != null) {
            Store store = connectAndGetEmailStore(settings);
            IMAPFolder destinationFolder = null;
            if (store != null && store.isConnected()) {
                try {
                    if (message != null) {
                        message.saveChanges();
                        EdsEmailFolder folder = settings.getFolder(type);
                        if (folder != null) {
                            destinationFolder = (IMAPFolder) store.getFolder(folder.getFullName());
                            if (destinationFolder != null && destinationFolder.exists()) {
                                if (!destinationFolder.isOpen()) {
                                    destinationFolder.open(Folder.READ_WRITE);
                                }
                                message.setFlag(Flags.Flag.SEEN, true);
                                destinationFolder.appendMessages(new Message[]{message});
                            }
                        }
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (destinationFolder != null && destinationFolder.isOpen()) {
                            destinationFolder.close(false);
                        }
                        store.close();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Email getWithContent(EdsEmail edsEmail) {
        if (edsEmail != null) {
            Email mailItem = new Email();
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
            mc.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_html");
            CommandMap.setDefaultCommandMap(mc);
            KPIMimeMessage mimeMessage = null;
            EdsEmailFolder folder = emailFolderManager.get(edsEmail.getFolderId());
            EdsEmailSetting emailSetting = folder != null ? folder.getEmailSetting() : null;
            mailItem.setDeleted(true);
            if (edsEmail.getTrackerId() != null) {
                mailItem.setCaseID(emailTrackerManager.getCaseIDByTrackerID(edsEmail.getTrackerId()));
            }
            if (emailSetting != null) {
                try {
                    if (edsEmail.getEmailContentId() != null) {
                        Properties properties = EmailConnectionUtils.getDefaultProperties(emailSetting);
                        Session emailSession = Session.getDefaultInstance(properties);
                        EdsUpload upload = (EdsUpload) uploadManager.get(edsEmail.getEmailContentId());
                        InputStream inputStream = uploadManager.getInputStream(upload);
                        if (inputStream != null) {
                            mimeMessage = new KPIMimeMessage(emailSession, inputStream);
                        }
                    }
                    if (mimeMessage == null) {
                        Store store = connectAndGetEmailStore(emailSetting);
                        if (store != null && store.isConnected()) {
                            try {
                                mimeMessage = getFolderMimeMessage(store, emailSetting, folder.getFullName(), edsEmail.getMessageUID());
                                if (mimeMessage != null && edsEmail != null) {
//                                    edsEmail.setEmailContentId(uploadMessageToAmazon(mimeMessage, edsEmail));
                                }
                            } catch (MessagingException e) {
                                log.error(e.getMessage());
                            } finally {
                                try {
                                    store.close();
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        emailRepository.save(edsEmail);
                    }
                } catch (MessagingException | IOException e) {
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

    public KPIMimeMessage toMimeMessage(EdsEmail email) {
        if (email != null) {
            KPIMimeMessage mimeMessageClone;
            KPIMimeMessage mimeMessage = null;
            EdsEmailSetting emailSetting = emailSettingsManager.get(email.getEmailSettingId());
            Properties properties = EmailConnectionUtils.getDefaultProperties(emailSetting);
            Session emailSession = Session.getDefaultInstance(properties);
            if (email.getEmailContentId() != null) {
                EdsUpload upload = (EdsUpload) uploadManager.get(email.getEmailContentId());
                InputStream stream = uploadManager.getInputStream(upload);
                if (stream != null) {
                    try {
                        mimeMessage = new KPIMimeMessage(emailSession, stream);
                    } catch (MessagingException e) {
                        log.error("while converting MimeMessage to KPIMimeMessage throwed:\n" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            if (mimeMessage == null) {
                if (emailSetting != null) {
                    Store store = null;
                    try {
                        store = connectAndGetEmailStore(emailSetting);
                        if (store.isConnected()) {

                            EdsEmailFolder folder = emailFolderManager.get(email.getFolderId());
                            mimeMessage = getFolderMimeMessage(store, emailSetting, folder.getFullName(), email.getMessageUID());
                        }
                    } catch (MessagingException | IOException e) {
                        log.error("while getting mimeMessage from folder throwed:\n" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        if (store != null) {
                            try {
                                store.close();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
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
        if (emailSetting != null) {
            Store store = null;
            try {
                store = connectAndGetEmailStore(emailSetting);

                Set<String> folders = new HashSet<>();
                if (store != null && store.isConnected()) {
                    try {
                        for (Folder folder : store.getDefaultFolder().list("*")) {
                            try {
                                IMAPFolder imapFolder = (IMAPFolder) folder;
                                if (imapFolder != null && !StringUtils.isEmpty(imapFolder.getFullName()) && !"[Gmail]/All Mail".equals(folder.getFullName())) {
                                    if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                                        createFolder(imapFolder, emailSetting, isFirstTime);
                                        folders.add(imapFolder.getFullName());
                                    }
                                }
                            } catch (MessagingException e) {
                                log.error("couldn't create folder " + (folder != null ? folder.getName() : "") + " anv = " + SecurityContext.getCompanyID() + " emailsettingID = " + emailSetting.getObjectID());
                            }
                        }
                        if (folders.size() > 0) {
                            emailFolderManager.deleteFolders(emailSetting.getObjectID(), folders);
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } finally {
                        store.close();
                    }
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private EdsEmailFolder createFolder(IMAPFolder folder_, EdsEmailSetting emailSetting, boolean isFirstTime) throws MessagingException {
        EdsEmailFolder edsFolder = folder_.getURLName() != null ? emailFolderManager.getByURL(folder_.getURLName().toString(), emailSetting.getObjectID()) : null;
        if (edsFolder != null) {
            return edsFolder;
        } else {
            edsFolder = new EdsEmailFolder();
            edsFolder.setEmailSetting(emailSetting);
            edsFolder.setName(folder_.getName());
            edsFolder.setFullName(folder_.getFullName());
            if (folder_.getURLName() != null && folder_.getURLName().toString() != null) {
                edsFolder.setUrl(folder_.getURLName().toString());
            }
            if (isFirstTime && StringUtils.isNotEmpty(folder_.getName())) {
                if (folder_.getName().toLowerCase().contains("sent")) {
                    edsFolder.setType(MCFolderType.SENT);
                    edsFolder.setFetchable(true);
                } else if (folder_.getName().toLowerCase().contains("trash")) {
                    edsFolder.setType(MCFolderType.TRASH);
                    edsFolder.setFetchable(false);
                } else if (folder_.getName().toLowerCase().contains("draft")) {
                    edsFolder.setType(MCFolderType.DRAFT);
                    edsFolder.setFetchable(false);
                } else {
                    edsFolder.setType(MCFolderType.INBOX);
                    edsFolder.setFetchable(true);
                }
            }
            Folder parentFolder = folder_.getParent();
            if (parentFolder != null && StringUtils.isNotEmpty(parentFolder.getFullName()) &&
                    !("[Gmail]".equals(parentFolder.getFullName()) || "[Gmail]/All Mail".equals(parentFolder.getFullName()))) {
                edsFolder.setParent(createFolder((IMAPFolder) parentFolder, emailSetting, isFirstTime));
            }
            log.info("Folder created.(" + edsFolder.getName() + "), companyID:" + SecurityContext.getCompanyID() + ", email:" + emailSetting.getEmail());
            emailFolderManager.createOrUpdate(edsFolder);
            emailSetting.getFolders().add(edsFolder);
            return edsFolder;
        }
    }

    @Override
    public void setFlags(Set<Email> emails, Integer emailSettingID, String flag) {
        EdsEmailSetting settings = emailSettingsManager.get(emailSettingID);
        try {
            Store store = connectAndGetEmailStore(settings);
            if (store != null && store.isConnected()) {
                try {
                    for (Email e : emails) {
                        boolean folderOpened = false;
                        IMAPFolder folder = (IMAPFolder) store.getFolder(e.getFolderName());
                        if (folder.exists()) {
                            folder.open(Folder.READ_WRITE);
                            folderOpened = true;
                        }
                        if (folderOpened) {
                            SearchTerm searchTerm = new MessageIDTerm(e.getMessageId());
                            Message[] messages = folder.search(searchTerm);
                            if (messages != null && messages.length > 0) {
                                for (Message m : messages) {
                                    if (EdsEmail.FLAG_DELETED.equals(flag)) {
                                        m.setFlag(Flags.Flag.DELETED, true);
                                    } else {
                                        m.setFlag(Flags.Flag.SEEN, EdsEmail.FLAG_READ.equals(flag));
                                    }
                                }
                                if (EdsEmail.FLAG_DELETED.equals(flag) && !MCFolderType.TRASH.equals(e.getType())) {
                                    EdsEmailFolder trashFolder = settings.getFolder(MCFolderType.TRASH);
                                    if (trashFolder != null) {
                                        boolean trashFolderOpened = false;
                                        Folder trFolder = store.getFolder(new URLName(trashFolder.getUrl()));
                                        if (trFolder.exists()) {
                                            trFolder.open(Folder.READ_WRITE);
                                            trashFolderOpened = true;
                                        }
                                        if (trashFolderOpened) {
                                            folder.copyMessages(messages, trFolder);
                                            trFolder.close(true);
                                        }
                                    }
                                }
                            }
                            folder.close(true);
                        }
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } finally {
                    store.close();
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>... This is method message search in entered folder
     * if this is message no in folder that search all folder ...</h1>
     * <br/
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {15:55 13/07/2011} ...</h3>
     *
     * @param store
     * @param emailSetting
     * @param folderName
     * @param messageUID   @return
     */
    private KPIMimeMessage getFolderMimeMessage(Store store, EdsEmailSetting emailSetting, String folderName, long messageUID) throws MessagingException, IOException {
        Folder folder = store.getFolder(folderName);
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }

        Message message = ((IMAPFolder) folder).getMessageByUID(messageUID);
        if (message == null) {
            log.warn("Message Does not exists in email Server...");
            return null;
        }
        KPIMimeMessage mimeMessage = KPIMimeMessage.convert(EmailSessionCache.get(emailSetting.getSessionKey()), message);

        if (folder.isOpen()) {
            folder.close(false);
        }

        return mimeMessage;
    }

    public Integer testConnection(EmailAccountItem item) {
        Session emailSession = EmailConnectionUtils.getEmailSession(item);
        emailSession.setDebug(true);
        if (item.getImapHost() != null && !"".equals(item.getImapHost())) {
            Integer response = incomingValidate(item, emailSession);
            if (0 != response) {
                return response;
            }
        }
        if (item.getSmtpHost() != null && !"".equals(item.getSmtpHost())) {
            Integer response = smtpValidateNew(item, emailSession);
            if (0 != response) {
                return response;
            }
        }
        return 0;
    }

    private Integer incomingValidate(EmailAccountItem item, Session emailSession) {
        String user = item.getEmail();
        String password = item.getPassword();

        String incomingServerHost = item.getImapHost();
        Integer incomingServerPort = item.getImapPort();
        try {
            Store store = emailSession.getStore();
            if (incomingServerPort != null) {
                store.connect(incomingServerHost, incomingServerPort, user, password);
            } else {
                store.connect(incomingServerHost, user, password);
            }
            return 0;
        } catch (NoSuchProviderException e) {
            return EmailAccountItem.ERROR_COULDNOTCONNECT;
        } catch (MessagingException e) {
            e.printStackTrace();
            if (e instanceof AuthenticationFailedException) {
                if (e.getMessage().contains("Please log in via your web browser") || e.getMessage().contains("https://support.google.com/mail/accounts/answer/")) {
                    return EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER;
                }
                return EmailAccountItem.ERROR_CREDENTIAL;
            } else {
                if (e.getNextException() != null) {
                    if (e.getNextException() instanceof UnknownHostException) {
                        return EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION;
                    } else {
                        if (e.getNextException() instanceof ConnectException) {
                            if (e.getNextException().getMessage().contains("refused")) {
                                return EmailAccountItem.ERROR_CONNECTIONREFUSED;
                            } else {
                                if (e.getNextException().getMessage().contains("timed out")) {
                                    return EmailAccountItem.ERROR_CONNECTIONTIMEDOUT;
                                }
                            }
                        }
                    }
                }
                return EmailAccountItem.ERROR_CONNECTIONTIMEDOUT;
            }
        }
    }

    private Integer smtpValidateNew(EmailAccountItem item, Session emailSession) {
        String user = item.getEmail();
        String password = item.getPassword();
        String smtpHost = item.getSmtpHost();
        Integer smtpPort = item.getSmtpPort();
        try {
            Transport transport = emailSession.getTransport();
            transport.connect(smtpHost, smtpPort == null ? -1 : smtpPort, user, password);
            Message simpleMessage = new MimeMessage(emailSession);
            InternetAddress fromAddress = new InternetAddress(item.getEmail());
            InternetAddress toAddress = new InternetAddress(item.getEmail());
            simpleMessage.setFrom(fromAddress);
            simpleMessage.setRecipient(Message.RecipientType.TO, toAddress);
            simpleMessage.setSubject("This is an e-mail message sent automatically by " + EdsContextParams.getProductName() + " while testing the settings for your account.");
            simpleMessage.setText("This is an e-mail message sent automatically by " + EdsContextParams.getProductName() + " while testing the settings for your account.");
            transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
            transport.close();
            return 0;
        } catch (NoSuchProviderException e) {
            return EmailAccountItem.ERROR_COULDNOTCONNECT + EmailAccountItem.ERROR_SMTP_SERVER;
        } catch (MessagingException e) {
            e.printStackTrace();
            if (e instanceof AuthenticationFailedException) {
                return EmailAccountItem.ERROR_CREDENTIAL + EmailAccountItem.ERROR_SMTP_SERVER;
            } else {
                if (e instanceof SMTPSendFailedException) {
                    return EmailAccountItem.ERROR_CANTSENDEMAIL;
                } else {
                    if (e.getNextException() != null) {
                        if (e.getNextException() instanceof UnknownHostException) {
                            return EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION + EmailAccountItem.ERROR_SMTP_SERVER;
                        } else {
                            if (e.getNextException() instanceof ConnectException) {
                                if (e.getNextException().getMessage().contains("refused")) {
                                    return EmailAccountItem.ERROR_CONNECTIONREFUSED + EmailAccountItem.ERROR_SMTP_SERVER;
                                } else {
                                    if (e.getNextException().getMessage().contains("timed out")) {
                                        return EmailAccountItem.ERROR_CONNECTIONTIMEDOUT + EmailAccountItem.ERROR_SMTP_SERVER;
                                    }
                                }
                            }
                        }
                    }
                    return EmailAccountItem.ERROR_CONNECTIONTIMEDOUT + EmailAccountItem.ERROR_SMTP_SERVER;
                }
            }
        }
    }

    public boolean supports(String provider) {
        return EdsEmailSetting.Provider.DEFAULT.name().equals(provider);
    }
}
