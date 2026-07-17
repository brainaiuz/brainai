package com.edatasite.workforce.gwt.messagecenter.server;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.mail.EdsMailParams;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.shared.mail.MailMessage;
import com.edatasite.shared.mail.Upload;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCaseHistory;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.FileBodyManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.impl.rbac.email.UserEmailRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.MessageCenterCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.EmailSessionCache;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.server.app.DefaultMailServiceImpl;
import com.edatasite.workforce.gwt.messagecenter.server.app.DefaultMailServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.app.MailServices;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Store;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 18, 2010
 * Time: 6:24:04 PM
 */

@Transactional
@Service("messageCenterService")
public class MessageCenterServiceImpl implements MessageCenterService, MessageCenterServiceLocal, Constants {
    private static final Logger log = LoggerFactory.getLogger(MessageCenterServiceImpl.class);

    @Autowired
    private EmailAttachmentManager emailAttachmentManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private FileBodyManager fileBodyManager;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private EmailFolderManager emailFolderManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private MailServices mailServices;
    @Autowired
    private DefaultMailServiceLocal genericEmailService;
    @Autowired
    private EmailTrackerManager emailTrackerManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UserEmailRbacManager userEmailRbacManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private CaseHistoryManager caseHistoryManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private WorkflowAlertManager workflowAlertManager;

    public CaseItem getCase(Integer caseId) {
        return crmService.getCase(caseId, false);
    }

    public ArrayList<SelectItem> getContactAccounts(Integer contactID) {
        ArrayList<SelectItem> list = Lists.newArrayList();

        if (emailFolderManager.getUser().getCompany().getCompanySettings().getEmailAutoLinking()) {
            EdsOpportunity opportunity = opportunityManager.getOpportunityByContactId(contactID);
            if (opportunity != null) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(opportunity.getObjectID());
                selectItem.setName(opportunity.getName());
                selectItem.setReferenceCode(RelationItem.TYPE_OPPORTUNITY);
                list.add(selectItem);
            }
        }
        list.add(contactService.getContactAccountSelect(contactID));
        return list;
    }

    public SelectItem[] getImapHostAndSmptHost(ListingFilterParameter fp) {

        SelectItem[] income = new SelectItem[5];
        income[0] = new SelectItem(1, "imap.gmail.com");
        income[2] = new SelectItem(2, "imap.yandex.ru");
        income[1] = new SelectItem(3, "imap.yandex.com");
        income[3] = new SelectItem(4, "outlook.office365.com");
        income[4] = new SelectItem(5, "imap-mail.outlook.com");

        SelectItem[] outcome = new SelectItem[5];
        outcome[0] = new SelectItem(1, "smtp.gmail.com");
        outcome[2] = new SelectItem(2, "smtp.yandex.ru");
        outcome[1] = new SelectItem(3, "smtp.yandex.com");
        outcome[3] = new SelectItem(4, "smtp.office365.com");
        outcome[4] = new SelectItem(5, "smtp-mail.outlook.com");

        return fp.getCategory().equals("income") ? income : outcome;
    }

    public ArrayList<FileResource> getAttachedFilesByAttachmentId(Integer attachmentId) {
        ArrayList<FileResource> result = new ArrayList<>();
        if (attachmentId != null) {
            EdsAttachment attachedFile = attachmentManager.get(attachmentId);
            EdsUploadSettings uploadSettings = uploadManager.getUploadSettings(attachedFile);
            if (uploadSettings != null) {
                try {
                    List<EdsAttachment> attachments = attachmentManager.getAttachmentsByAttachmentId(attachmentId);
                    for (EdsAttachment attachment : attachments) {
                        FileResource file = new FileResource();
                        file.setEntityID(attachmentId);
                        file.setName(attachment.getOriginalName());
                        file.setContentLength(attachment.getSize());
                        file.setContentType(attachment.getContentType());
                        file.setBodyId(attachment.getObjectID());
                        file.setNewEdsAttachment(true);
                        file.setUrlFromSolr(uploadManager.getUploadFileUrl(attachmentId));
                        result.add(file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * In the message center the user may possess several emails, thus
     * we need them on demand.
     *
     * @param params - list of emails
     * @return - returns all email related inbox messages
     */
    //@CheckPermission(permissions = {PermissionConstants.PM_TASKS_EMAILS, PermissionConstants.PM_PROJECT_EMAIL})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<Email> getEmails(ListingFilterParameter params) {
        ArrayList<Email> result = emailRepository.getEmailList(params).stream().map(EdsEmail::getListRPC).collect(Collectors.toCollection(ArrayList::new));
        Integer messageTotalCount = emailRepository.getEmailCount(params);
        return new ListResult<>(result, messageTotalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<Email> getEmailsToTop(ListingFilterParameter params) {
        List<Integer> emailSettingIDs = getEmailSettingIDs();
        ArrayList<Email> result = new ArrayList<>();
        Integer total = 0;
        int i = 0;
        for (Integer emailSettingID : emailSettingIDs) {
            params.setObjectId(emailSettingID);
            if (i == 0) {
                result.addAll(emailRepository.getEmailList(params).stream().map(EdsEmail::getListRPC).collect(Collectors.toCollection(ArrayList::new)));
            }
            if (result.size() < params.getLimit() && i > 0) {
                result.addAll(emailRepository.getEmailList(params).stream().map(EdsEmail::getListRPC).collect(Collectors.toCollection(ArrayList::new)));
            }
            total = total + emailRepository.getEmailCount(params);
            i++;
        }

        return new ListResult<>(result, total);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getEmailsCountToTop(ListingFilterParameter params) {
        List<Integer> emailSettingIDs = getEmailSettingIDs();
        Integer total = 0;
        for (Integer emailSettingID : emailSettingIDs) {
            params.setObjectId(emailSettingID);
            total = total + emailRepository.getEmailCount(params);
        }

        return total;
    }

    public void manuallyFetchEmails(Integer folderID) {
        EdsEmailFolder folder = emailFolderManager.get(folderID);
        if (folder != null && folder.getEmailSetting() != null) {
            log.info("Fetching email started from listing... ");
            EdsEmailSetting emailSetting = folder.getEmailSetting();
            Integer companyID = emailFolderManager.getUser().getCompany().getObjectID();
            Store store = null;
            try {
                if (emailSetting.isDefaultProvider()) {
                    store = DefaultMailServiceImpl.connectAndGetEmailStore(emailSetting);
                }
                fetchEmail(folderID, companyID, emailSetting.getObjectID(), store);
            } catch (Exception e) {
                log.error("Error while fetching email from listing... ");
                log.error(SecurityContext.getInstance().getCompanyId(), e);
                if (emailSetting != null) {
                    if (e instanceof AuthenticationFailedException || e instanceof NoSuchProviderException) {
                        log.error("Email fetch failed AuthenticationFailedException");
                        updateEmailSettingsAfterException(emailSetting.getObjectID(), "Eror message 5 - " + e.getMessage());
                    } else if (e instanceof MessagingException ex) {
                        if (ex.getNextException() instanceof UnknownHostException) {
                            updateEmailSettingsAfterException(emailSetting.getObjectID(), "Eror message 6 - " + e.getMessage());
                        } else if (ex.getNextException() instanceof ConnectException) {
                            if (ex.getNextException().getMessage().contains("refused")) {
                                log.error("Email fetch failed ConnectException Refused");
                                updateEmailSettingsAfterException(emailSetting.getObjectID(), "Eror message 7 - " + e.getMessage());
                            } else if (ex.getNextException().getMessage().contains("timed out")) {
                                log.error("Email fetch failed ConnectException Time Out");
                                updateEmailSettingsAfterException(emailSetting.getObjectID(), "Eror message 8 - " + e.getMessage());
                            }
                        }
                    }
                }
            } finally {
                SecurityContext.getInstance().setStaticUserID(null);
                try {
                    if (store != null) {
                        store.close();
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer fetchEmail(Integer folderId, Integer companyID, Integer emailSettingId, Store store) throws Exception {
        return mailServices.getService(emailSettingId).fetchEmails(folderId, companyID, emailSettingId, store);
    }

    @Override
    public Email getEmailWithContent(String emailID) {
        Optional<EdsEmail> emailOptional = emailRepository.findById(emailID);
        return emailOptional.map(edsEmail -> mailServices.getService(emailID).getWithContent(edsEmail)).orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<EmailAccountItem> getSharedEmailAccounts(boolean withInboxCount) {
        String companyId = SecurityContext.getInstance().getCompanyId();
        EdsEmailSetting defaultEmailSetting = emailSettingsManager.getUserDefaultEmailAccount();
        List<EdsEmailSetting> settings = userEmailRbacManager.getSharedEmailAccounts(false);
        ArrayList<EmailAccountItem> items = new ArrayList<>();
        if (withInboxCount && defaultEmailSetting != null) {
            EmailAccountItem item = new EmailAccountItem();
            Long inboxCount = emailRepository.getUnreadInboxMessageCount(defaultEmailSetting.getObjectID(), companyId);
            item.setUnreadCount(inboxCount);
            item.setLastEmails(getLastFiveUnreadEmails(defaultEmailSetting.getObjectID()).stream().map(EdsEmail::getRPC).collect(Collectors.toCollection(LinkedHashSet::new)));
            items.add(defaultEmailSetting.getRPC(item));
            settings.remove(defaultEmailSetting);
        }
        for (EdsEmailSetting setting : settings) {
            EmailAccountItem item = new EmailAccountItem();
            if (withInboxCount) {
                Long inboxCount = emailRepository.getUnreadInboxMessageCount(setting.getObjectID(), companyId);
                item.setUnreadCount(inboxCount);
                item.setLastEmails(getLastFiveUnreadEmails(setting.getObjectID()).stream().map(EdsEmail::getRPC).collect(Collectors.toCollection(LinkedHashSet::new)));
            }
            items.add(setting.getRPC(item));
        }
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getUserEmailAccounts(boolean showAll) {
        List<EdsEmailSetting> settings = userEmailRbacManager.getSharedEmailAccounts(true);
        EdsEmailSetting companyEmailSettings = emailSettingsManager.getCompanyEmailSetting(null);
        EdsEmailSetting defaultEmailSettings = emailSettingsManager.getUserDefaultEmailAccount();
        Integer defaultEmailSettingsID = defaultEmailSettings != null && settings.contains(defaultEmailSettings) ? defaultEmailSettings.getObjectID() : companyEmailSettings != null ? companyEmailSettings.getObjectID() : null;
        if (companyEmailSettings != null && !settings.contains(companyEmailSettings) && showAll) {
            settings.add(companyEmailSettings);
        }
        ArrayList<SelectItem> result = new ArrayList<>();
        boolean defaultSelected = false;
        boolean isKpi = false;
        for (EdsEmailSetting setting : settings) {
            SelectItem mailItem = new SelectItem(setting.getObjectID(), setting.getEmail());
            if (setting.getObjectID().equals(defaultEmailSettingsID)) {
                mailItem.setSelected(true);
                defaultSelected = true;
            }
             if ("@kpi.com".contains(setting.getEmail())){
                 isKpi = true;
             }

            mailItem.setNumber(String.valueOf(setting.getAttempts()));
            result.add(mailItem);
        }
        if (showAll && isKpi && !settings.isEmpty()) {
            EdsMailParams mailParams = EdsMailer.getWhiteLabelingMailer();
            SelectItem item = new SelectItem(0, mailParams.getEmail() != null ? mailParams.getEmail() : "no-reply@kpi.com", Constants.DEFAULT_MAILER);
            item.setNumber("0");
            item.setSelected(!defaultSelected);
            result.add(item);
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    @Override
    public Integer testConnection(EmailAccountItem item) {
        return genericEmailService.testConnection(item);
    }

    @Override
    public Integer saveEmailAccount(EmailAccountItem item) {
        EdsEmailSetting isNullEmailSettings = emailSettingsManager.getActiveEmailSetting(item.getEmail());
        if (isNullEmailSettings != null && !isNullEmailSettings.getObjectID().equals(item.getObjectID())) {
            return EmailAccountItem.ERROR_DUBLICATE_EMAIL;
        }
        EdsUser user = userManager.getUser();
        EdsEmailSetting emailSettings = item.getObjectID() != null ? emailSettingsManager.get(item.getObjectID()) : new EdsEmailSetting();
        boolean isEmailChanged = emailSettings.getObjectID() != null && !emailSettings.getEmail().equals(item.getEmail());
        if (item.getObjectID() == null) {
            emailSettings.setUser(userManager.get(user.getObjectID()));
        }
        emailSettings.setEmail(item.getEmail());
        emailSettings.setProvider(EdsEmailSetting.Provider.valueOf(item.getProvider()));

        if (EdsEmailSetting.Provider.DEFAULT.equals(emailSettings.getProvider())) {
            emailSettings.setFromName(item.getFromName());
            emailSettings.setEmailHost(item.getImapHost());
            emailSettings.setEmailHostSMTP(item.getSmtpHost());
            emailSettings.setEmailPort(item.getImapPort());
            emailSettings.setEmailPortSMTP(item.getSmtpPort());
            emailSettings.setSmtpAuth(item.isSmtpAuth());
            emailSettings.setUserName(item.getUserName());
            emailSettings.setPassword(EncryptionHelper.encrypt(item.getPassword()));
        } else {
            emailSettings.setRefreshToken(item.getRefreshToken());
        }

        emailSettings.setCompanyEmail(item.isCompanyEmail());
        emailSettings.setDefaultEmail(item.isDefaultEmail());
        emailSettings.setActive(item.getObjectID() == null || item.isActive());
        emailSettings.setAttempts(0);
        emailSettings.setSaveCopyToSentFolder(item.isSaveCopyToSentFolder());
        emailSettings.setStartDate(item.getStartDate() != null ? item.getStartDate() : new Date());
        boolean isNew = emailSettingsManager.createOrUpdate(emailSettings);
        if (isNew) {
            userEmailRbacManager.createEmailOwnerRbacEntry(emailSettings);
        } else {
            if (isEmailChanged) {
                emailFolderManager.deleteFolders(emailSettings.getObjectID(), null);
            }
            EmailSessionCache.remove(emailSettings.getSessionKey());
        }
        if (isNew || isEmailChanged) {
            mailServices.getService(emailSettings.getProvider()).createFolders(emailSettings, true);
        }
        if (emailSettings.isDefaultEmail()) {
            emailSettingsManager.undefaultAccounts(emailSettings.getObjectID(), user.getObjectID());
        }
        if (emailSettings.isCompanyEmail()) {
            emailSettingsManager.undoCompanyEmails(emailSettings.getObjectID());
        }
        if (!EdsEmailSetting.Provider.DEFAULT.equals(emailSettings.getProvider())) {
            rabbitMQService.emailFetchMQ(emailSettings.getObjectID());
        }
        if (item.isCompanyEmail() && item.isDefaultEmail()) {
            workflowAlertManager.updateFromEmail(emailSettings);
            List<String> activeAccounts = emailSettingsManager.getAllActiveEmails();
            /*String activeAccounts = "";
            if (accounts != null && !accounts.isEmpty()) {
                activeAccounts = accounts.stream().map(EdsEmailSetting::getEmail).collect(Collectors.joining(","));
            }*/
            emailTemplateManager.updateFromEmail(item.getEmail(), ServerUtils.getAsCommoDelimited(activeAccounts, "0", ","));
        }
        return 0;
    }

    @Override
    public EmailAccountItem getEmailAccount(Integer objectID) {
        EdsEmailSetting s = emailSettingsManager.get(objectID);
        return s != null ? s.getRPC(null) : new EmailAccountItem();
    }

    @Override
    public boolean deleteEmailAccount(Integer objectID) {
        EdsUser user = userManager.getUser();
        EdsEmailSetting setting = emailSettingsManager.get(objectID);
        setting.setActive(false);
        setting.setDefaultEmail(false);
        setting.setCompanyEmail(false);
        setting.setDeleted(true);
        EmailSessionCache.remove(setting.getSessionKey());
        emailSettingsManager.update(setting);
        emailFolderManager.deleteFolders(objectID, null);
        List<EdsEmailSetting> settings = userEmailRbacManager.getSharedEmailAccounts(false);
        if (!settings.isEmpty()) {
            Integer defaultID = null;
            for (EdsEmailSetting s : settings) {
                if (s.getUser() != null && user.getObjectID().equals(s.getUser().getObjectID())) {
                    defaultID = s.getObjectID();
                }
            }
            defaultID = defaultID == null ? settings.get(0).getObjectID() : defaultID;

            RedisClient.setKey(CacheConstants.USER_EMAIL_ACCOUNT + "_" + SecurityContext.getCompanyID() + "_" + user.getObjectID(), String.valueOf(defaultID));
            return true;
        }
        RedisClient.removeKey(CacheConstants.USER_EMAIL_ACCOUNT + "_" + SecurityContext.getCompanyID() + "_" + user.getObjectID());
        return false;
    }

    @Override
    public void addEmailAccountToHazelcast(Integer accountID) {
        EdsUser edsUser = userManager.getUser();
        RedisClient.setKey(CacheConstants.USER_EMAIL_ACCOUNT + "_" + SecurityContext.getCompanyID() + "_" + edsUser.getObjectID(), String.valueOf(accountID));
    }

    @Transactional(readOnly = true)
    public List<EdsEmail> getEmailsByIDs(Iterable<String> ids) {
        List<EdsEmail> emails = new ArrayList<>();
        emailRepository.findAllById(ids).forEach(emails::add);
        return emails;
    }

    @Transactional(readOnly = true)
    public List<EdsEmail> getLastFiveUnreadEmails(Integer emailSettingID) {
        Pageable request = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        return emailRepository.getUnreadEmails(emailSettingID, SecurityContext.getInstance().getCompanyId(), request);
    }

    @Override
    public void setEmailFlags(ArrayList<String> emailIDs, Integer emailFolderID, String flag) {
        if (emailIDs != null && !emailIDs.isEmpty()) {
            if (emailFolderID != null) {
                EdsEmailFolder folder = emailFolderManager.get(emailFolderID);
                EdsEmailSetting setting = folder.getEmailSetting();
                if (setting != null) {
                    String asCommoDelimeted = ServerUtils.getAsCommoDelimited(emailIDs, "0", ",");
                    EdsBusinessEvent event = baseEventPostProcessor.registerEvent(MessageCenterCustomEventListenerImpl.TYPE, flag, setting, userManager.getUser());
                    event.setCustomStringField(asCommoDelimeted);
                }
            } else {
                emailIDs.forEach(emailID -> {
                    Optional<EdsEmail> emailOptional = emailRepository.findById(emailID);
                    if (emailOptional.isPresent()) {
                        EdsEmail email = emailOptional.get();
                        EdsEmailFolder folder = emailFolderManager.get(email.getFolderId());
                        EdsEmailSetting setting = folder != null ? folder.getEmailSetting() : null;
                        if (setting != null) {
                            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(MessageCenterCustomEventListenerImpl.TYPE, flag, setting, userManager.getUser());
                            event.setCustomStringField(emailID);
                        }
                    }
                });
            }
            if (FLAG_DELETED.equals(flag)) {
                getEmailsByIDs(emailIDs).forEach(email -> {
                    email.setDeleted(true);
                    emailRepository.save(email);
                });
            } else {
                getEmailsByIDs(emailIDs).forEach(email -> {
                    email.setRead(!FLAG_UNREAD.equals(flag));
                    emailRepository.save(email);
                });
            }
        }
    }

    @Override
    public ArrayList<EmailFolder> getEmailAccountFolders(Integer emailSettingID, boolean refresh) {
        EdsEmailSetting setting = emailSettingsManager.get(emailSettingID);
        if (refresh) {
            mailServices.getService(setting.getProvider()).createFolders(setting, false);
        }
        return setting != null ? setting.getFolders().stream().map(EdsEmailFolder::getRPC).collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
    }

    @Override
    public HashMap<EmailAccountItem, HashSet<EmailFolder>> getUserFetchableEmailFolders() {
        List<EdsEmailSetting> settings = userEmailRbacManager.getSharedEmailAccounts(false);
        if (settings != null) {
            HashMap<EmailAccountItem, HashSet<EmailFolder>> map = Maps.newHashMap();

            for (EdsEmailSetting setting : settings) {
                HashSet<EmailFolder> folders = emailFolderManager.getFoldersForFetch(setting.getObjectID(), false).stream().map(EdsEmailFolder::getRPC).collect(Collectors.toCollection(LinkedHashSet::new));

                EmailAccountItem item = setting.getRPC(null);
                Long inboxCount = emailRepository.getUnreadInboxMessageCount(setting.getObjectID(), SecurityContext.getInstance().getCompanyId());
                item.setUnreadCount(inboxCount);

                map.put(item, folders);
            }
            return map;
        }

        return new HashMap<>();
    }

    @Override
    public boolean isEmailAccountSetup() {
        List<EdsEmailSetting> emailSettingList = userEmailRbacManager.getSharedEmailAccounts(false);
        return emailSettingList.size() > 0;
    }

    private List<Integer> getEmailSettingIDs() {
        List<EdsEmailSetting> emailSettingList = userEmailRbacManager.getSharedEmailAccounts(false);
        return emailSettingList.stream().map(EdsEmailSetting::getObjectID).collect(Collectors.toList());
    }

    @Override
    public void saveFetchableFolders(ArrayList<Integer> fetchableFolderIDs, Integer trashFolderID, Integer sentFolderID, Integer emailSettingID) {
        EdsEmailSetting setting = emailSettingsManager.get(emailSettingID);
        for (EdsEmailFolder folder : setting.getFolders()) {
            folder.setFetchable(fetchableFolderIDs.contains(folder.getObjectID()));
            if (trashFolderID.equals(folder.getObjectID())) {
                folder.setType(MCFolderType.TRASH);
            }
            if (sentFolderID.equals(folder.getObjectID())) {
                folder.setType(MCFolderType.SENT);
            }
        }
        emailSettingsManager.update(setting);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getRelatedEmail(String relationType, Integer relationID) {
        if (RelationItem.TYPE_LEAD.equals(relationType) || RelationItem.TYPE_CANDIDATE.equals(relationType) || RelationItem.TYPE_CONTACT.equals(relationType)) {
            EdsCrmContact contact = crmContactManager.get(relationID);
            if (contact != null) {
                return contact.getPrimaryEmail();
            }
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            EdsCrmAccount account = crmAccountManager.get(relationID);
            if (account != null) {
                return account.getEmail();
            }
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            EdsOpportunity opportunity = opportunityManager.get(relationID);
            if (opportunity != null && opportunity.getCrmContact() != null) {
                return opportunity.getCrmContact().getPrimaryEmail();
            }
        }
        return null;
    }

    @Override
    public void saveAsDraft(Email email) {
        EdsEmail edsEmail = email.getObjectID() != null ? emailRepository.findById(email.getObjectID()).get() : new EdsEmail();
        edsEmail.setCompanyId(SecurityContext.getInstance().getCompanyId());
        edsEmail.setSubject(email.getSubject());
        edsEmail.setDescription(email.getContent());
        edsEmail.setFrom(email.getFromEmail());
        edsEmail.setTo(email.getToEmails());
        edsEmail.setToCC(email.getCc());
        edsEmail.setToBCC(email.getBcc());
        edsEmail.setReplyTo(email.getReplyTo());
        edsEmail.setFetched(false);
        edsEmail.setRead(true);
        EdsEmailSetting settings = emailSettingsManager.getActiveEmailSetting(email.getFromEmail());
        settings = settings == null ? EdsMailer.getWhiteLabelingMailer().asEdsEmailSetting() : settings;
        edsEmail.setEmailSettingId(settings.getObjectID());
        if (edsEmail.getFolderId() == null) {
            edsEmail.setFolderId(settings.getFolder(MCFolderType.DRAFT).getObjectID());
            edsEmail.setFolderType(MCFolderType.DRAFT.name());
        }
        if (email.getTrackerID() == null) {
            EdsEmailTracker emailTracker = new EdsEmailTracker();
            emailTrackerManager.create(emailTracker);
            edsEmail.setTrackerId(emailTracker.getObjectID());
        } else {
            edsEmail.setTrackerId(email.getTrackerID());
        }
        edsEmail.setTrackerType(email.getRelationType());
        edsEmail.setEmailTemplateId(email.getTemplateId());

        if (edsEmail.isNew()) {
            edsEmail.setCreatedDate(new Date());
        }
        if (email.isRelationChanged()) {
            allInOneService.saveRelations(RelationItem.TYPE_EMAIL_TRACKER, edsEmail.getTrackerId(), edsEmail.getSubject(), email.getRelations());
        }
        emailRepository.save(edsEmail);
    }

    @Override
    public Email getEmail(String objectID) {
        Optional<EdsEmail> emailOptional = emailRepository.findById(objectID);
        if (emailOptional.isPresent()) {
            EdsEmail email = emailOptional.get();
            Email emailItem = email.getRPC();
            if (email.isHasAttachment()) {
                emailItem.setAttachments(EdsEmailAttachment.asFileResourses(emailAttachmentManager.getEmailAttachments(email.getId())));
            }
            return emailItem;
        }
        return null;
    }

    public void sendReferMessage(Email message) {
        try {
            EdsUser user = userManager.getUser();
            EdsCompany company = user.getCompany();
            String content = message.getContent();
            if (StringUtils.isNotEmpty(message.getToEmails())) {
                for (String toEmail : message.getToEmails().trim().split(",")) {
                    if (StringUtils.isNotEmpty(toEmail)) {
                        messageManager.sendReferSomeoneMessage(company, user, toEmail, content);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendUserPostedToFacebookEmail() {
        try {
            EdsUser user = userManager.getUser();
            EdsCompany company = user.getCompany();
            String content = user.getEmail() + ", " + company.getObjectID() + ", " + company.getName();
            messageManager.sendUserPostedToFacebookMessage(company, user, "munir@kpi.com", content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Integer sendMessage(Email message) {
        EdsUser user = userManager.getUser();
        EdsEmailSetting settings = emailSettingsManager.getActiveEmailSetting(message.getFromEmail());
        settings = settings != null ? settings : EdsMailer.getWhiteLabelingMailer().asEdsEmailSetting();

        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject(message.getSubject());
        mailMessage.setContent(message.getContent());
        mailMessage.setInvisibleTrackerInSubject(message.isInvisibleTrackerInSubject());

        if (StringUtils.isNotEmpty(message.getToEmails())) {
            for (String to : message.getToEmails().trim().split(",")) {
                if (StringUtils.isNotEmpty(to)) {
                    mailMessage.addContactsTo(to);
                }
            }
        }
        if (StringUtils.isNotEmpty(message.getCc())) {
            for (String cc : message.getCc().trim().split(",")) {
                mailMessage.addContactsCc(cc);
            }
        }
        if (StringUtils.isNotEmpty(message.getBcc())) {
            for (String bcc : message.getBcc().trim().split(",")) {
                mailMessage.addContactBcc(bcc);
            }
        }
        if (message.getTemplateId() != null) {
            EdsEmailTemplate template = emailTemplateManager.get(message.getTemplateId());
            mailMessage.addContactsReplyTo(template != null ? template.getReplyTo() : null);
        }

        EdsEmail draftedEmail = MCFolderType.DRAFT.equals(message.getType()) && message.getObjectID() != null ? emailRepository.findById(message.getObjectID()).get() : null;
        Map<Integer, EdsEmailAttachment> draftedAttachments = draftedEmail != null && draftedEmail.isHasAttachment() ? EdsObject.getListAsMapIntegerAndValue(emailAttachmentManager.getEmailAttachments(draftedEmail.getId())) : new HashMap<>();
        List<String> emailIDs = new ArrayList<>();//bu collection emaillarni messageIDsi buyicha to'plangan contentlarni cachedan tozalash uchun.
        for (FileResource attachment : message.getAttachments()) {
            Upload uploadFile = new Upload();
            uploadFile.setFileName(attachment.getFileName());
            uploadFile.setContentType(attachment.getContentType());
            if (attachment.isDownloadFromEmailServer()) {
                EdsEmailAttachment edsAttachment = null;
                if (draftedAttachments.containsKey(attachment.getEmailAttachmentID())) {
                    edsAttachment = draftedAttachments.get(attachment.getEmailAttachmentID());
                } else {
                    edsAttachment = emailAttachmentManager.get(attachment.getEmailAttachmentID());
                }
                if (edsAttachment != null && edsAttachment.getEmailId() != null) {
                    emailIDs.add(edsAttachment.getEmailId());
                }
                uploadFile.setInputStream(getInputStream(edsAttachment));
                uploadFile.setContentType(edsAttachment.getContentType());
                uploadFile.setFileName(edsAttachment.getFileName());
            } else {
                if (Constants.KPI_STORAGE.equals(attachment.getUploadType())) {
                    uploadFile.setInputStream(uploadManager.getInputStream(fileBodyManager.get(attachment.getBodyId())));
                } else {
                    //FileResource kelgan bulsa ham EdsAttachmentdan olish kerak... bu oldingi Logika(EdsAttachment-Upload) bilan ishlaydi...
                    attachment.setBodyId(attachment.getBodyId() == null ? attachment.getObjectId() : attachment.getBodyId());
                    uploadFile.setInputStream(uploadManager.getInputStream((EdsUpload) uploadManager.get(attachment.getBodyId())));
                }
            }
            mailMessage.addAttachment(uploadFile);
        }

        Integer companyID = SecurityContext.getCompanyID();
        if (!emailIDs.isEmpty() && EdsEmail.messageContents.contains(companyID)) {//clear the caches
            for (String emailID : emailIDs) {
                EdsEmail.messageContents.get(companyID).remove(emailID);
            }
        }
        mailMessage.setMessageID(draftedEmail != null ? draftedEmail.getMessageId() : null);
        mailMessage.setTrackerID(message.getTrackerID());
        Integer trackerId = mailServices.getService(settings.getProvider()).sendMessage(settings, mailMessage);

        if (trackerId != null) {
            if (message.getRelations() != null && !message.getRelations().isEmpty()) {
                allInOneService.saveRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerId, mailMessage.getSubject(), message.getRelations());
            }
            if (draftedEmail != null) {
                draftedEmail.setDeleted(true);
                emailRepository.save(draftedEmail);
            }
            if (message.getCaseID() != null) {
                final EdsCase crmCase = caseManager.get(message.getCaseID());
                crmCase.setStatus(referenceManager.findReference(EdsCase._CASE_STATUS, EdsCase.REPLIED));
                caseManager.update(crmCase, true);
                final EdsCaseHistory history = new EdsCaseHistory();
                history.setCreationTime(new Date());
                history.setCrmCase(crmCase);
                history.setUpdater(user);
                history.setMessage((message.isForward() ? ("Forwarded to " + message.getToEmails() + " - \"") : "Replied to Reporter - \"") + message.getSubject() + "\"");
                caseHistoryManager.create(history);
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmCase, user);
                workflowEvent.setEntityType(RelationItem.TYPE_CASE);
            }
        }
        return trackerId;
    }

    @Transactional(readOnly = true)
    public Object getContentOnly(String emailId) {
        Optional<EdsEmail> emailOptional = emailRepository.findById(emailId);
        if (emailOptional.isPresent()) {
            EdsEmail email = emailOptional.get();
            KPIMimeMessage mimeMessage = mailServices.getService(emailId).toMimeMessage(email);
            if (mimeMessage == null) {
                return null;
            }
            try {
                return mimeMessage.getContent();
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public InputStream getInputStream(EdsEmailAttachment attachment) {
        String emailId = attachment.getEmailId();
        Integer companyID = SecurityContext.getCompanyID();
        Object content = null;
        boolean newContent = false;
        if (EdsEmail.messageContents.containsKey(companyID) && EdsEmail.messageContents.get(companyID).containsKey(emailId)) {
            content = EdsEmail.messageContents.get(companyID).get(emailId);
        }
        if (!newContent || content == null) {
            content = getContentOnly(emailId);
            newContent = true;
        }
        if (newContent && content != null) {
            if (!EdsEmail.messageContents.containsKey(companyID)) {
                EdsEmail.messageContents.put(companyID, new HashMap<>());
            }
            if (!EdsEmail.messageContents.get(companyID).containsKey(emailId)) {
                EdsEmail.messageContents.get(companyID).put(emailId, content);
            }
        }
        return EmailUtils.getInputStream(content, attachment);
    }

    public void updateEmailSettingsAfterException(Integer emailSettingsId, String exceptionMessage) {
        if (emailSettingsId == null) {
            return;
        }
        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingsId);
        if (emailSetting == null || emailSetting.isDeleted()) {
            return;
        }
        emailSetting.setAttempts(emailSetting.getAttempts() + 1);
        if (emailSetting.getAttemptLimit() != null ? emailSetting.getAttempts() >= emailSetting.getAttemptLimit() : emailSetting.getAttempts() >= 3) {
            StringBuilder ss = new StringBuilder();
            if (emailSetting.getErrorOfInactivation() != null && !emailSetting.getErrorOfInactivation().isEmpty()) {
                ss.append(emailSetting.getErrorOfInactivation() + ",\n");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String stringDate = dateFormat.format(new Date());
            ss.append(stringDate + " : " + exceptionMessage);
            emailSetting.setErrorOfInactivation(ss.toString());
//            emailSetting.setActive(false);
//            messageManager.sentEmailAccountInactivationEmail(emailSetting.getEmail(), emailSetting.getUser().getFullName());
        }
        emailSettingsManager.update(emailSetting);
    }
}
