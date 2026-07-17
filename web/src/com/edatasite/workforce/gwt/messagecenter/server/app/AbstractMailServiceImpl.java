package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.shared.mail.MailMessage;
import com.edatasite.shared.mail.Upload;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.emailfetching.EdsTracker;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManagerImpl;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.MapEntry;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.TrackerManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.EmailSessionCache;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.gwt.messagecenter.server.app.tracker.EmailTrackerService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.InputStreamDataSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public abstract class AbstractMailServiceImpl implements MailService {
    private static final Logger log = LoggerFactory.getLogger(AbstractMailServiceImpl.class);
    protected static final Cache<String, Map<String, String>> tempTrackers =
            CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000).build();

    private static final AtomicBoolean SSL_PROVIDER_REGISTERED = new AtomicBoolean(false);

    static {
        registerSslProviderOnce();
    }

    private static void registerSslProviderOnce() {
        if (SSL_PROVIDER_REGISTERED.compareAndSet(false, true)) {
            try {
                java.security.Security.addProvider(
                        javax.net.ssl.SSLContext.getInstance("TLS", "SunJSSE").getProvider()
                );
            } catch (Exception e) {
                LoggerFactory.getLogger(AbstractMailServiceImpl.class)
                        .warn("SSL provider registration failed: {}", e.getMessage());
            }
        }
    }

    @Autowired private RabbitMQService rabbitMQService;
    @Autowired private GenericSettingsManager genericSettingsManager;
    @Autowired protected UserManager userManager;
    @Autowired protected EmailSettingsManager emailSettingsManager;
    @Autowired protected EmailFolderManager emailFolderManager;
    @Autowired private EmailFilterManager emailFilterManager;
    @Autowired protected UploadManager uploadManager;
    @Autowired private ReferenceManager referenceManager;
    @Autowired protected BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired protected EmailRepository emailRepository;
    @Autowired protected CompanyEmailManager companyEmailManager;
    @Autowired protected EmailAttachmentManager emailAttachmentManager;
    @Autowired protected EmailTrackerManager emailTrackerManager;
    @Autowired protected EmailTrackerService emailTrackerService;
    @Autowired protected TrackerManager trackerManager;
    @Autowired protected RelationManager relationManager;
    @Autowired private GlobalAuthJdbcSpringManagerImpl globalAuthJdbcSpringManager;

    abstract Session initSession(EdsEmailSetting edsEmailSetting);

    abstract void send(EdsEmailSetting settings, KPIMimeMessage message) throws MessagingException;

    abstract void appendMessage(EdsEmailSetting settings, MimeMessage message, MCFolderType type) throws MessagingException;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> createEmails(Map<Long, Message> messageMap, Integer emailSettingId, Integer folderId, Integer companyId) throws MessagingException {
        if (messageMap == null || messageMap.isEmpty()) {
            return Collections.emptyList();
        }
        boolean convertEachEmailToCase = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONVERT_EACH_EMAIL_TO_CASE);

        EdsEmailFolder edsFolder = emailFolderManager.get(folderId);
        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingId);

        boolean convertable = MCFolderType.INBOX.equals(edsFolder.getType()) && (companyId != 65159 || "support@kpi.com".equals(emailSetting.getEmail()));
        List<EdsEmailFilter> filters = emailFilterManager.getParentsOnly();
        Session emailSession = initSession(emailSetting);
        String folderType = edsFolder.getType() != null ? edsFolder.getType().name() : null;

        List<String> emailIds = new ArrayList<>();
        List<String> pendingCaseConversion = new ArrayList<>();
        for (Map.Entry<Long, Message> entry : messageMap.entrySet()) {
            Long uId = entry.getKey();
            Message message = entry.getValue();

            KPIMimeMessage kpiMessage;
            try {
                kpiMessage = KPIMimeMessage.convert(emailSession, message);
                kpiMessage.setFlag(Flags.Flag.SEEN, message.getFlags().contains(Flags.Flag.SEEN));
            } catch (MessagingException | IOException e) {
                log.warn("Failed to convert message uid={} for setting={}: {}", uId, emailSettingId, e.getMessage());
                continue;
            }

            String emailId = createEmailItem(kpiMessage, uId, emailSettingId, folderId, folderType, companyId, convertEachEmailToCase);

            if (emailId != null) {
                emailIds.add(emailId);
                if (convertable) {
                    emailRepository.findById(emailId).ifPresent(email -> {
                        email.filter(filters, kpiMessage);
                        if (email.hasCaseFilter()) {
                            email.setEmailContentId(uploadMessageToAmazon(kpiMessage, email));
                            emailRepository.save(email);
                            pendingCaseConversion.add(email.getId());
                        }
                    });
                }
                log.info("Company:[{}] Email created setting={} folder={} emailId={}", companyId, emailSettingId, edsFolder.getObjectID(), emailId);
            }
        }

        pendingCaseConversion.forEach(rabbitMQService::emailConvertToCaseMQ);

        edsFolder.setLastFetchedDate(new Date());
        emailFolderManager.update(edsFolder);

        return emailIds;
    }

    public String createEmailItem(MimeMessage message, Long uId,
                                  Integer emailSettingId, Integer folderId,
                                  String folderType, Integer companyId,
                                  boolean convertEachEmailToCase) {
        if (message == null || uId == null || emailSettingId == null) {
            return null;
        }

        EdsEmail edsEmail = null;
        try {

            if (StringUtils.isNotEmpty(message.getMessageID())) {
                List<EdsEmail> existing = emailRepository
                        .findTopByMessageIdAndEmailSettingIdAndCompanyId(
                                message.getMessageID(), emailSettingId, companyId.toString());

                if (existing != null && !existing.isEmpty()) {
                    edsEmail = existing.get(0);
                    for (int i = 1; i < existing.size(); i++) {
                        emailRepository.delete(existing.get(i));
                    }
                }
                if (edsEmail != null && edsEmail.isFetched()) {
                    log.info("Re-fetching existing email id={}", edsEmail.getId());
                    if (!edsEmail.getFolderId().equals(folderId)) {
                        edsEmail.setFolderId(folderId);
                        edsEmail.setFolderType(folderType);
                    }
                    edsEmail.setMessageUID(uId);
                    edsEmail.setFetchedDate(new Date());
                    emailRepository.save(edsEmail);
                    return null;
                }
            }

            edsEmail = new EdsEmail();
            edsEmail.setCompanyId(String.valueOf(companyId));
            edsEmail.setClusterType(globalAuthJdbcSpringManager.getCompanyClusterType(companyId));
            edsEmail.setEmailSettingId(emailSettingId);
            edsEmail.setSubject(message.getSubject());
            edsEmail.setMessageUID(uId);
            edsEmail.setMessageId(message.getMessageID());
            edsEmail.setRead(message.getFlags().contains(Flags.Flag.SEEN));
            edsEmail.setFolderId(folderId);
            edsEmail.setFolderType(folderType);
            edsEmail.setFetchedDate(new Date());
            edsEmail.setFetched(true);
            edsEmail.setCreatedDate(parseSentDate(message));

            try {
                if (message.getFrom() != null && message.getFrom().length > 0) {
                    edsEmail.setFrom(((InternetAddress) message.getFrom()[0]).toUnicodeString());
                }
            } catch (Exception e) {
                log.warn("Failed to parse From address for message {}: {}", message.getMessageID(), e.getMessage());
            }

            String replyTo = null;
            try {
                if (message.getReplyTo() != null
                        && message.getReplyTo().length > 0
                        && message.getReplyTo()[0] != null) {
                    replyTo = message.getReplyTo()[0].toString();
                }
            } catch (MessagingException e) {
                log.warn("Failed to parse ReplyTo for message {}: {}", message.getMessageID(), e.getMessage());
            }

            if (replyTo != null && edsEmail.getFrom() != null && edsEmail.getFrom().contains("no-reply@kpi.com")) {
                edsEmail.setFrom(replyTo);
            }
            edsEmail.setReplyTo(replyTo);
            edsEmail.setTo(getMessageRecipients(message, Message.RecipientType.TO));
            edsEmail.setToCC(getMessageRecipients(message, Message.RecipientType.CC));
            edsEmail.setToBCC(getMessageRecipients(message, Message.RecipientType.BCC));

            boolean convertEmailAnyway = MCFolderType.INBOX.name().equals(folderType) && convertEachEmailToCase;
            Integer trackerId = generateTrackerID(message, companyId, emailSettingId, convertEmailAnyway);

            edsEmail.setTrackerId(trackerId);

            boolean hasAttachments = createMessageAttachments(edsEmail, message.getContent());
            edsEmail.setHasAttachment(hasAttachments);

            emailRepository.save(edsEmail);

            rabbitMQService.emailTrackerRelationMQ(edsEmail.getId());

            if (!StringUtil.isEmpty(edsEmail.getFrom())) {
                Map<Integer, String> data = new HashMap<>();
                data.put(trackerId, edsEmail.getId());
                rabbitMQService.mergeContactAndTracker(data, companyId, edsEmail.getClusterType());
            }

        } catch (Exception e) {
            log.error("Failed to create email item for setting={} uid={}: {}", emailSettingId, uId, e.getMessage(), e);
            return null;
        }

        return edsEmail.getId();
    }

    public Integer sendMessage(EdsEmailSetting settings, MailMessage mail) {
        Integer cId = SecurityContext.getCompanyID();
        String companyID = cId + "_" + settings.getObjectID();
        tempTrackers.asMap().computeIfAbsent(companyID, k -> new ConcurrentHashMap<>());

        EdsEmailTracker tracker = null;
        try {
            MapEntry<KPIMimeMessage, EdsEmailTracker> messageMap = retrieveMessage(settings, mail);
            if (messageMap != null) {
                KPIMimeMessage message = messageMap.getKey();
                send(settings, message);
                tracker = messageMap.getValue();
                if (tracker != null && message.getMessageID() != null) {
                    String msgId = message.getMessageID().toLowerCase();
                    String code = tracker.getCode() != null ? tracker.getCode() : "";
                    tempTrackers.asMap().get(companyID).put(msgId, code);
                    saveTracker(settings.getObjectID(), tracker.getObjectID(), message.getMessageID());
                }
                if (settings.isSaveCopyToSentFolder()) {
                    appendMessage(settings, message, MCFolderType.SENT);
                }
                log.info("Message sent from={} to={}",
                        settings.getEmail(),
                        ServerUtils.getAsCommoDelimited(mail.getContactsTo(), "nobody", ", "));
            }
        } catch (MessagingException ex) {
            log.error("Failed to send message from={}: {}", settings.getEmail(), ex.getMessage(), ex);
        } finally {
            EmailSessionCache.remove(settings.getSessionKey());
        }
        return tracker != null ? tracker.getObjectID() : null;
    }

    protected MapEntry<KPIMimeMessage, EdsEmailTracker> retrieveMessage(EdsEmailSetting settings, MailMessage mail) {
        try {
            Session session = initSession(settings);

            Map<String, MimeBodyPart> embeddedImages = new HashMap<>();
            mail.setContent(EmailUtils.replaceEmbeddedImages(embeddedImages, mail.getContent()));

            KPIMimeMessage message = new KPIMimeMessage(session) {
                @Override
                protected void updateMessageID() throws MessagingException {
                    if (getHeader("Message-ID") == null) {
                        super.updateMessageID();
                    }
                }
            };

            String fromName = StringUtils.isNotEmpty(mail.getFromUserFullName())
                    ? mail.getFromUserFullName()
                    : settings.getFromName();
            message.setFrom(new InternetAddress(settings.getEmail(), fromName));
            message.setText(mail.getContent(), "utf-8");
            if (mail.isInvisibleTrackerInSubject()) {
                message.setTrackerInvisibleSubject(mail.getSubject());
            } else {
                message.setSubject(mail.getSubject(), "utf-8");
            }
            message.setHeader("Content-Type", "text/html; charset=\"UTF-8\"");

            // Tracker resolution
            EdsEmailTracker emailTracker = resolveOrCreateTracker(mail, message);

            message.initTracker(emailTracker.getCode());
            applyRelations(mail, emailTracker);

            // Recipients
            addRecipients(message, Message.RecipientType.TO, mail.getContactsTo(), false);
            addRecipients(message, Message.RecipientType.CC, mail.getContactsCc(), true);
            addRecipients(message, Message.RecipientType.BCC, mail.getContactsBcc(), true);

            setReplyTo(message, mail.getContactsReplyTo());
            applyCompanyBcc(message);

            // Attachments + embedded images
            boolean hasContent = !mail.getAttachments().isEmpty() || !embeddedImages.isEmpty();
            if (hasContent) {
                buildMultipart(message, mail, embeddedImages);
            }

            message.saveChanges();
            return new MapEntry<>(message, emailTracker);
        } catch (IOException | MessagingException ex) {
            log.error("Failed to build outbound message: {}", ex.getMessage(), ex);
        }
        return null;
    }

    protected Integer uploadMessageToAmazon(MimeMessage message, EdsEmail edsEmail) {
        String subFolder = "MessageCenter/"
                + (edsEmail.getEmailSettingId() != null ? edsEmail.getEmailSettingId() : "emailsettingnotfound")
                + "/"
                + (edsEmail.getFolderId() != null ? edsEmail.getFolderId() : "NO_FOLDER");

        byte[] rawBytes;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            message.writeTo(bos);
            rawBytes = bos.toByteArray();
        } catch (IOException | MessagingException e) {
            log.error("Failed to serialize message content for emailId={}: {}",
                    edsEmail.getId(), e.getMessage(), e);
            return null;
        }

        try (InputStream bis = new ByteArrayInputStream(rawBytes)) {
            EdsUpload upload = new EdsUpload();
            upload.setFolderName(subFolder);
            upload.setContentType("application/octet-stream");
            upload.setOriginalName(edsEmail.getId() + ".eml");
            upload.setType(referenceManager.findReference(
                    Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
            upload.setInputStream(bis);
            uploadManager.createAndCommit(upload);
            return upload.getObjectID();
        } catch (IOException e) {
            log.error("Failed to upload message to storage for emailId={}: {}",
                    edsEmail.getId(), e.getMessage(), e);
        }
        return null;
    }

    private void saveTracker(Integer emailSettingsId, Integer trackerID, String messageID) {
        EdsTracker tracker = new EdsTracker();
        tracker.setTrackerID(trackerID);
        tracker.setEmailSettingID(emailSettingsId);
        tracker.setMessageID(messageID.replaceAll("[<>]", "").toLowerCase());
        trackerManager.create(tracker);
    }

    protected boolean createMessageAttachments(EdsEmail edsEmail, Object content) {
        boolean result = false;
        try {
            if (content instanceof Multipart multipart) {

                for (int i = 0; i < multipart.getCount(); i++) {
                    Part part = multipart.getBodyPart(i);
                    Object partContent = part.getContent();

                    if (partContent instanceof Multipart) {
                        result |= createMessageAttachments(edsEmail, partContent);
                    } else if (partContent instanceof InputStream || partContent instanceof String) {
                        boolean isAttachment = Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                                || StringUtils.isNotBlank(part.getFileName());
                        if (isAttachment) {
                            processAttachmentPart(edsEmail, part);
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to process attachments for emailId={}: {}", edsEmail.getId(), e.getMessage(), e);
        }
        return result;
    }

    private void processAttachmentPart(EdsEmail edsEmail, Part part) {
        try {
            byte[] data;
            try (InputStream is = part.getDataHandler().getInputStream()) {
                data = is.readAllBytes();
            }

            String contentType = resolveContentType(part);
            createAttachment(edsEmail, part.getFileName(), data.length, contentType, part);

        } catch (Exception e) {
            log.error("Failed to process attachment part for emailId={}: {}", edsEmail.getId(), e.getMessage(), e);
        }
    }

    private void createAttachment(EdsEmail edsEmail, String fileName, long size, String contentType, Part part) {
        EdsEmailAttachment edsAttachment = new EdsEmailAttachment();
        edsAttachment.setDescription("Message Center Email Attachment");
        edsAttachment.setFilesize(size);
        edsAttachment.setFileName(fileName);
        edsAttachment.setContentType(ServerUtils.getSplitEmailContent(contentType, fileName));
        edsAttachment.setEmailId(edsEmail.getId());
        if (edsEmail.getTrackerId() != null) {
            edsAttachment.setEmailTracker(emailTrackerManager.get(edsEmail.getTrackerId()));
        }
        if (part != null) {
            try {
                String[] cids = part.getHeader("Content-ID");
                if (cids != null && cids.length > 0 && StringUtils.isNotEmpty(cids[0])) {
                    edsAttachment.setContentID(cids[0]);
                }
            } catch (MessagingException e) {
                log.warn("Failed to read Content-ID header: {}", e.getMessage());
            }
        }
        edsAttachment.setCreator(emailAttachmentManager.getUser());
        edsAttachment.setUpdater(emailAttachmentManager.getUser());
        emailAttachmentManager.create(edsAttachment);
    }

    protected Integer generateTrackerID(MimeMessage message,
                                        Integer companyId,
                                        Integer emailSettingId,
                                        boolean createNew) throws MessagingException {
        String cacheKey = companyId + "_" + (emailSettingId != null ? emailSettingId : "");
        tempTrackers.asMap().computeIfAbsent(cacheKey, k -> new ConcurrentHashMap<>());

        // 1. Check X-TRACKER-IDS header and subject for embedded tracker code
        String trackerHeader = "";
        if (message != null) {
            String[] trackerIDs = message.getHeader("X-TRACKER-IDS");
            if (trackerIDs != null && trackerIDs.length > 0 && trackerIDs[0] != null) {
                trackerHeader = "," + trackerIDs[0];
            }
        }
        EdsEmailTracker emailTracker = emailTrackerManager.getByCode(
                KPIMimeMessage.extractTrackerCodefromSubject(trackerHeader));

        HashSet<String> messageIDs = new HashSet<>();

        if (emailTracker == null && message != null && message.getMessageID() != null) {
            messageIDs.add(message.getMessageID());
            collectInReplyTo(message, messageIDs);
            collectReferences(message, messageIDs);

            // 2. DB lookup by related MessageIDs (primary and most reliable)
            if (!messageIDs.isEmpty()) {
                List<EdsEmail> candidates = emailRepository.findAllByMessageIdsAndEmailSettingId(
                        String.join(",", messageIDs), emailSettingId, companyId.toString());

                if (candidates != null && !candidates.isEmpty()) {
                    EdsEmail email = candidates.stream()
                            .filter(e -> e.getTrackerId() != null)
                            .min(Comparator.comparing(EdsEmail::getCreatedDate,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .orElse(candidates.get(0));
                    emailTracker = emailTrackerManager.get(email.getTrackerId());
                }
            }

            // 3. Tracker table lookup by MessageIDs
            if (emailTracker == null) {
                Integer trackerID = trackerManager.getByMessageIDs(emailSettingId, messageIDs.toArray(new String[0]));
                if (trackerID != null) {
                    emailTracker = emailTrackerManager.get(trackerID);
                }
            }

            // 4. In-memory cache fallback (short-lived; not authoritative)
            if (emailTracker == null) {
                Map<String, String> cache = tempTrackers.asMap().get(cacheKey);
                for (String msgId : messageIDs) {
                    if (!StringUtil.isEmpty(msgId)) {
                        String cachedCode = cache.get(msgId.toLowerCase());
                        if (cachedCode != null) {
                            emailTracker = emailTrackerManager.getByCode(new String[]{cachedCode});
                            if (emailTracker != null) break;
                        }
                    }
                }
            }
        }

        // 5. Create a new tracker if none found or if createNew is requested
        emailTracker = emailTrackerService.createTracker(
                (emailTracker == null || createNew) ? null : emailTracker);

        Map<String, String> cache = tempTrackers.asMap().get(cacheKey);
        for (String msgId : messageIDs) {
            if (!StringUtil.isEmpty(msgId)) {
                cache.put(msgId.toLowerCase(), emailTracker.getCode());
            }
        }

        return emailTracker.getObjectID();
    }

    protected String getMessageRecipients(Message message,
                                          Message.RecipientType type,
                                          String... escapeEmails) throws MessagingException {
        Address[] recipients = message.getRecipients(type);
        if (recipients == null || recipients.length == 0) {
            return null;
        }

        List<String> emails = new ArrayList<>();
        for (Address address : recipients) {
            if (address == null) continue;
            String addr = ((InternetAddress) address).getAddress();
            if (StringUtils.isBlank(addr)) continue;

            if (escapeEmails != null && escapeEmails.length > 0) {
                boolean skip = Arrays.stream(escapeEmails)
                        .anyMatch(e -> address.toString().toLowerCase().contains(e.toLowerCase()));
                if (skip) continue;
            }
            emails.add(addr);
        }

        return emails.isEmpty() ? null : String.join(",", emails);
    }

    private Date parseSentDate(MimeMessage message) {
        try {
            Date sentDate = message.getSentDate();
            if (sentDate != null) return sentDate;
        } catch (MessagingException e) {
            log.warn("getSentDate() failed: {}", e.getMessage());
        }

        try {
            String[] dateHeaders = message.getHeader("Date");
            if (dateHeaders != null && dateHeaders.length > 0 && StringUtils.isNotBlank(dateHeaders[0])) {
                jakarta.mail.internet.MailDateFormat mdf = new jakarta.mail.internet.MailDateFormat();
                return mdf.parse(dateHeaders[0]);
            }
        } catch (Exception e) {
            log.warn("Failed to parse Date header, falling back to now: {}", e.getMessage());
        }

        return new Date();
    }

    private EdsEmailTracker resolveOrCreateTracker(MailMessage mail, KPIMimeMessage message) throws MessagingException {
        EdsEmailTracker emailTracker = mail.getTrackerID() != null
                ? emailTrackerManager.get(mail.getTrackerID())
                : emailTrackerManager.getByCode(KPIMimeMessage.extractTrackerCodefromSubject(message.getSubject()));

        if (emailTracker == null) {
            emailTracker = emailTrackerService.createTracker(null);
        } else {
            message.initReferences(
                    emailRepository.findByTrackerId(emailTracker.getObjectID(),
                                    SecurityContext.getInstance().getCompanyId())
                            .stream()
                            .map(EdsEmail::getMessageId)
                            .toArray(String[]::new));
        }
        return emailTracker;
    }

    private void applyRelations(MailMessage mail, EdsEmailTracker emailTracker) {
        if (mail.getRelations() == null || mail.getRelations().isEmpty()) return;

        RelationItem.setFromID(emailTracker.getObjectID(), mail.getRelations());
        RelationItem.setFromName(emailTracker.getObjectID(), RelationItem.TYPE_EMAIL_TRACKER,
                emailTracker.getCode(), mail.getRelations());
        for (RelationItem relationItem : mail.getRelations()) {
            relationManager.create(new EdsRelation(relationItem));
        }
    }

    private void addRecipients(MimeMessage message,
                               Message.RecipientType type,
                               List<String> addresses,
                               boolean validateFormat) throws MessagingException {
        if (addresses == null) return;
        for (String addr : addresses) {
            if (StringUtils.isEmpty(addr)) continue;
            if (validateFormat && !addr.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) continue;
            try {
                message.addRecipient(type, new InternetAddress(addr));
            } catch (AddressException e) {
                log.warn("Skipping invalid recipient address '{}': {}", addr, e.getMessage());
            }
        }
    }

    private void setReplyTo(MimeMessage message, List<String> replyToAddresses) throws MessagingException {
        if (replyToAddresses == null || replyToAddresses.isEmpty()) return;
        List<InternetAddress> replyList = new ArrayList<>();
        for (String addr : replyToAddresses) {
            if (StringUtils.isNotEmpty(addr) && addr.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                try {
                    replyList.add(new InternetAddress(addr));
                } catch (AddressException e) {
                    log.warn("Invalid reply-to address '{}': {}", addr, e.getMessage());
                }
            }
        }
        if (!replyList.isEmpty()) {
            message.setReplyTo(replyList.toArray(new InternetAddress[0]));
        }
    }

    private void applyCompanyBcc(MimeMessage message) throws MessagingException {
        Integer companyId = SecurityContext.getCompanyID();
        if (companyId == null) return;

        String email = companyEmailManager.getCompanyEmail(companyId);
        if (StringUtils.isEmpty(email)) return;

        for (String bcc : email.split(",")) {
            try {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc.trim()));
            } catch (AddressException e) {
                log.warn("Invalid company BCC address '{}': {}", bcc.trim(), e.getMessage());
            }
        }
    }

    private void buildMultipart(MimeMessage message,
                                MailMessage mail,
                                Map<String, MimeBodyPart> embeddedImages)
            throws MessagingException, IOException {
        Multipart multipart = new MimeMultipart();

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(mail.getContent(), "utf-8");
        bodyPart.setHeader("Content-Type", "text/html");
        multipart.addBodyPart(bodyPart);

        for (Upload attachment : mail.getAttachments()) {
            InputStream is = attachment.getInputStream();
            if (is == null) continue;

            if (StringUtils.isBlank(attachment.getFileName())) continue;

            MimeBodyPart part = new MimeBodyPart();
            part.setDataHandler(new DataHandler(
                    new InputStreamDataSource(
                            attachment.getFileName(),
                            attachment.getContentType(),
                            is)));
            part.setFileName(attachment.getFileName());
            multipart.addBodyPart(part);
        }

        for (Map.Entry<String, MimeBodyPart> entry : embeddedImages.entrySet()) {
            multipart.addBodyPart(entry.getValue());
        }

        message.setContent(multipart);
    }

    private String resolveContentType(Part part) {
        try {
            if (part.getDataHandler() != null
                    && part.getDataHandler().getTransferDataFlavors() != null
                    && part.getDataHandler().getTransferDataFlavors().length > 0) {
                return part.getDataHandler().getTransferDataFlavors()[0].getMimeType();
            }
        } catch (Exception e) {
            log.debug("Could not resolve MIME type from DataHandler: {}", e.getMessage());
        }
        try {
            return part.getContentType();
        } catch (MessagingException e) {
            return "application/octet-stream";
        }
    }

    private void collectInReplyTo(MimeMessage message, Set<String> messageIDs) {
        try {
            String[] headers = message.getHeader("in-reply-to");
            if (headers == null || headers.length == 0) return;
            String inReplyTo = headers[0];
            if (inReplyTo != null) {
                inReplyTo = inReplyTo.replaceAll("[<>]", "").trim();
                if (!StringUtil.isEmpty(inReplyTo)) {
                    messageIDs.add(inReplyTo);
                }
            }
        } catch (MessagingException e) {
            log.debug("Failed to read In-Reply-To header: {}", e.getMessage());
        }
    }

    private void collectReferences(MimeMessage message, Set<String> messageIDs) {
        try {
            String[] refs = message.getHeader("references");
            if (refs == null || refs.length == 0 || refs[0] == null) return;
            for (String ref : refs[0].split("[\\s\t]")) {
                if (ref != null && ref.contains("<")) {
                    String clean = ref.replaceAll("[<>]", "").trim();
                    if (!StringUtil.isEmpty(clean)) {
                        messageIDs.add(clean);
                    }
                }
            }
        } catch (MessagingException e) {
            log.debug("Failed to read References header: {}", e.getMessage());
        }
    }
}