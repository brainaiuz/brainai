package com.edatasite.workforce.gwt.crm.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.shared.mail.EdsSMTPAuthenticator;
import com.edatasite.shared.mail.Upload;
import com.edatasite.shared.massmailler.MassMailerBody;
import com.edatasite.shared.massmailler.MassMailerCrmEntityBody;
import com.edatasite.shared.massmailler.MassMailerData;
import com.edatasite.shared.massmailler.MassSpamSender;
import com.edatasite.workforce.core.domain.EdsBlackList;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.MassMailParams;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsBrokenLinkTrack;
import com.edatasite.workforce.core.domain.crm.EdsCrmEntityMailList;
import com.edatasite.workforce.core.domain.crm.EdsCrmEntitySendMessageStatus;
import com.edatasite.workforce.core.domain.crm.EdsLink;
import com.edatasite.workforce.core.domain.crm.EdsLinkTrack;
import com.edatasite.workforce.core.domain.crm.EdsLinkTrackDate;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsMailListMessage;
import com.edatasite.workforce.core.domain.crm.EdsMailMessage;
import com.edatasite.workforce.core.domain.crm.EdsMailMessageTrack;
import com.edatasite.workforce.core.domain.crm.EdsMessageUnsubscribers;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmEntityMailListManager;
import com.edatasite.workforce.gwt.core.server.db.CrmEntitySendMessageStatusManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LinkManager;
import com.edatasite.workforce.gwt.core.server.db.LinkTrackManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.db.MailListMessageManager;
import com.edatasite.workforce.gwt.core.server.db.MailMessageManager;
import com.edatasite.workforce.gwt.core.server.db.MailMessageTrackManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.MessageUnsubscribersManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmMailingListEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CrmMailingListCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.client.rpc.MailListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MessageTrackListItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.InputStreamDataSource;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import jakarta.activation.DataHandler;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 13, 2010
 * Time: 4:03:46 PM
 */
@Transactional
@Service("massMailService")
public class MassMailServiceImpl implements MassMailService, MassMailServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(MassMailServiceImpl.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private MailMessageManager mailMessageManager;
    @Autowired
    private CrmEntitySendMessageStatusManager crmEntitySendMessageStatusManager;
    @Autowired
    private MailListMessageManager mailListMessageManager;
    @Autowired
    private MessageUnsubscribersManager messageUnsubscribersManager;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private CrmEntityMailListManager crmEntityMailListManager;
    @Autowired
    private MailMessageTrackManager mailMessageTrackManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private LinkManager linkManager;
    @Autowired
    private LinkTrackManager linkTrackManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private EmailSettingsManager emailSettingManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private BlackListManager blackListManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    private MessageCenterServiceLocal messageCenterServiceLocal;

    @Transactional
    public void sendCrmEntityMessageBounce(String clusterType, String companyID, Integer entityId, Integer msgId) {
        if (clusterType != null) {
            ServerSecurityContext.getInstance().setDatabase(clusterType);
        }
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        if (!companyManager.schemaExists(String.valueOf(companyID))) {
            return;
        }
        try {
            updateSentEntityMessageStatus(companyID, msgId, entityId, MessageStatusEnum.BOUNCED);
        } catch (Exception e) {
            System.err.println("Exception in sendCrmEntityMessageStatus");
            e.printStackTrace();
        }
        ServerSecurityContext.getInstance().setDatabase(null);
        ServerSecurityContext.getInstance().setCompanyId((String) null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SelectItem> getMailListMembers(ListingFilterParameter fp) {
        ArrayList<SelectItem> items = new ArrayList<>();
        List<EdsCrmContact> list = crmEntityMailListManager.getMailListMembers(fp);
        list.forEach(contact -> items.add(new SelectItem(contact.getObjectID(), contact.getFullName(), contact.getPrimaryEmail())));
        Long totalCount = crmEntityMailListManager.getMailListMembersCount(fp);
        return new ListResult<>(items, totalCount.intValue());
    }

    public Integer saveMailMessage(MailMessageItem item, ArrayList<Integer> subscribedMailLists) {
        EdsMailMessage mailMessage = new EdsMailMessage();
        if (item.getObjectID() != null) {
            mailMessage = mailMessageManager.get(item.getObjectID());
        } else {
            mailMessage.setCreationTime(new Date());
            mailMessage.setCreator(mailMessageManager.getUser());
        }
        if (item.getCampaignId() != null) {
            mailMessage.setCampaign(campaignManager.get(item.getCampaignId()));
        }
        mailMessage.setSubject(item.getSubject());
        mailMessage.setPreheader(item.getPreheader());
        mailMessage.setFromemail(item.getFrom());
        mailMessage.setFullName(item.getFullName());
        mailMessage.setReplyTo(item.getReplyTo());
        mailMessage.setSmsMessage(item.isSmsMessage());
        if (item.getSenderID() != null) {
            mailMessage.setSmsSettings(smsManager.get(item.getSenderID()));
        }
        mailMessage.setContent(item.getContent());
        mailMessage.setHtml(item.isHtml());
        mailMessage.setStatusCode(item.getStatus());
        mailMessage.setLastUpdateTime(new Date());
        mailMessage.setScheduled(item.getScheduled());
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsMailMessage.class.getSimpleName());
        if (item.getObjectID() != null) {
            mailMessageManager.update(mailMessage);
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(mailMessage.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update Mail message");
        } else {
            mailMessageManager.create(mailMessage);
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(mailMessage.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add Mail message");
        }
        updateMailListMessage(mailMessage, subscribedMailLists);
        commonServiceLocal.createMailMessageFolder(mailMessage.getObjectID());
        if (item.getTemplateAttachments() != null && item.getTemplateAttachments().size() > 0) {
            for (FileResource fileResource : item.getTemplateAttachments()) {
                attachmentUtilsManager.copyFileWhenConvert(F_MASS_MAILING, fileResource.getFolderId(), fileResource.getObjectId(), mailMessage.getObjectID(), fileResource);
            }
        }
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_MASS_MAILING, mailMessage.getObjectID(), mailMessage.getObjectID(), item.getAttachments());
        }

        if(item.getRecurrenceId() != null && MessageStatusEnum.DRAFT.equals(item.getStatus())){
            EdsRecurrence recurrence = recurrenceManager.get(item.getRecurrenceId());
            recurrence.setDeleted(true);
            recurrence.setChanged(true);
            recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
            recurrenceManager.update(recurrence);
        }
        // recurrence System
        if (item.getScheduled() != null) {
            Date startDate = item.getScheduled();
            Date endDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes() + 10);
            if (item.getRecurrenceId() != null) {
                EdsRecurrence recurrence = recurrenceManager.get(item.getRecurrenceId());
                recurrence.setStartDate(startDate);
                recurrence.setYearlyMonth(startDate.getMonth() + 1);
                recurrence.setMonthlyOrYearlyDay(startDate.getDate());
                recurrence.setEndDate(endDate);
                recurrenceManager.update(recurrence);
                recurrenceService.reLoadTrigger(recurrence);
            } else {
                RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
                recurrenceJobItem.setEnabled(true);
                recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
                recurrenceJobItem.setJobType(SchedulerConstant.MASS_MAILING_RECURRENCE);
                recurrenceJobItem.setBusObjectId(mailMessage.getObjectID());
                recurrenceJobItem.setInterval(1);
                recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                recurrenceJobItem.setEndType(SchedulerConstant.END_BY_DATE);
                recurrenceJobItem.setEndDate(endDate);
                recurrenceJobItem.setStartDate(startDate);
                recurrenceJobItem.setYearlyMonth(startDate.getMonth() + 1);
                recurrenceJobItem.setMonthlyOrYearlyDay(startDate.getDate());
                recurrenceService.saveRecurrenceJob(recurrenceJobItem);
            }
        }
        return mailMessage.getObjectID();
    }

    private void updateMailListMessage(EdsMailMessage mailMessage, List<Integer> checkedMailLists) {
        mailListMessageManager.deleteByMessage(mailMessage.getObjectID());
        for (Integer mailListID : checkedMailLists) {
            EdsMailListMessage mailListMessage = new EdsMailListMessage();
            mailListMessage.setMailList(mailListManager.get(mailListID));
            mailListMessage.setMailMessage(mailMessage);
            mailListMessageManager.create(mailListMessage);
        }
    }

    public void unsubscribeFromMailList(Integer mailListID, boolean unsbuscribe, ArrayList<Integer> subscriberID) {
        crmEntityMailListManager.subscribeOrUnsubscribeUsers(mailListID, subscriberID, unsbuscribe);
    }

    @Override
    public void unsubscribeFromMessage(Integer mailListID, Integer msgID, Integer subscriberID) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(subscriberID);
        crmEntityMailListManager.subscribeOrUnsubscribeUsers(mailListID, ids, true);
        EdsMessageUnsubscribers messageUnsubscribers = messageUnsubscribersManager.getByMsgAndEntity(msgID, subscriberID);
        if (messageUnsubscribers == null) {
            messageUnsubscribersManager.insertUnsubscriber(subscriberID, msgID, mailListID);
        }
    }

    @Override
    public void updateMessageStatusToSent(Integer mailMessageID) {
        EdsMailMessage message = mailMessageManager.get(mailMessageID);
        message.setStatusCode(MessageStatusEnum.SENT);
    }

    @Override
    public void updateSentEntityMessageStatus(String companyID, Integer msgId, Integer entityID, MessageStatusEnum status) {
        EdsCrmEntitySendMessageStatus crmEntitySendStatus = crmEntitySendMessageStatusManager.getEntity(msgId, entityID);
        EdsCrmContact edsCrmContact = crmContactManager.get(entityID);
        if (edsCrmContact != null && MessageStatusEnum.BOUNCED.equals(status)) {
            edsCrmContact.setEmailOptOut(true);
            crmContactManager.update(edsCrmContact);
            if (!StringUtils.isEmpty(edsCrmContact.getPrimaryEmail())) {
                EdsBlackList blackList = new EdsBlackList();
                blackList.setEmail(edsCrmContact.getPrimaryEmail().trim());
                blackList.setHostName(EdsContextParams.getHostname());
                blackListManager.create(blackList);
            }
        }
        if (crmEntitySendStatus == null) {
            crmEntitySendStatus = new EdsCrmEntitySendMessageStatus();
            crmEntitySendStatus.setEntity(edsCrmContact);
            crmEntitySendStatus.setMailmessage(mailMessageManager.get(msgId));
        }
        crmEntitySendStatus.setStatus(status);
        crmEntitySendMessageStatusManager.createOrUpdate(crmEntitySendStatus);
        if (companyID != null) {
            crmEntitySendMessageStatusManager.flushAndClear();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MailListItem> getMailLists(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsMailList.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Mailing list");
        List<Object[]> mailLists = mailListManager.getListOfMailLists(fp);
        Long totalCount = mailListManager.getTotalCountOfMailLists(fp);
        ArrayList<MailListItem> items = new ArrayList<>();
        mailLists.forEach(ob -> {
            MailListItem item = new MailListItem();
            item.setObjectId((Integer) ob[0]);
            item.setName((String) ob[1]);
            item.setMembersCount(((BigInteger) ob[2]).intValue());
            item.setActive((boolean) ob[3]);
            item.setCreatedDate((Date) ob[4]);
            items.add(item);
        });
        return new ListResult<>(items, totalCount.intValue());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MailMessageItem> getMailMessageList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsMailMessage.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Mail message list");
        List<EdsMailMessage> mailMessageList = mailMessageManager.getListOfMailMessages(fp);
        ArrayList<MailMessageItem> result = new ArrayList<>();
        Integer totalCount = mailMessageManager.getCountOfMailMessages(fp);
        for (EdsMailMessage mailMessage : mailMessageList) {
            result.add(mailMessage.getRPC(null));
        }
        return new ListResult<>(result, totalCount);
    }

    @Transactional
    public void createBatchSelectMailingList(ArrayList<Integer> checkedMailingLists, ArrayList<Integer> selectedIds) {
        Map<Integer, List<Integer>> crmEntityIDs = crmEntityMailListManager.getByCrmEntityIDs(selectedIds);

        EdsCrmEntityMailList newLmList;
        EdsMailList mList;
        if (checkedMailingLists != null && checkedMailingLists.size() > 0) {
            for (Integer crmEntityID : selectedIds) {
                for (Integer mlID : checkedMailingLists) {
                    if (crmEntityIDs.get(crmEntityID) == null || !crmEntityIDs.get(crmEntityID).contains(mlID)) {
                        EdsCrmContact entity = crmContactManager.get(crmEntityID);
                        mList = mailListManager.get(mlID);
                        newLmList = new EdsCrmEntityMailList();
                        newLmList.setDeleted(false);
                        newLmList.setEntity(entity);
                        newLmList.setMailList(mList);
                        crmEntityMailListManager.create(newLmList);
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MessageTrackListItem> getMessageViewTrackList(ListingFilterParameter filterParametrs) {
        ArrayList<MessageTrackListItem> results = new ArrayList<>();
        Integer totalCount = mailMessageTrackManager.getViewListCount(filterParametrs);
        if (totalCount != null && totalCount.intValue() > 0) {
            List<EdsMailMessageTrack> tracks = mailMessageTrackManager.getViewList(filterParametrs);
            tracks.forEach(track -> {
                MessageTrackListItem res = new MessageTrackListItem();
                EdsCrmContact contact = track.getEntity();
                res.setEntityID(contact.getObjectID());
                res.setEmail(contact.getPrimaryEmail());
                res.setFirstName(contact.getFirstName());
                res.setLastName(contact.getLastName());
                res.setContactType(contact.getContactType());
                res.setCrmAccountID(contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : null);
                res.setOpenedCount(track.getOpenedCount());
                results.add(res);
            });
        }
        return new ListResult<>(results, totalCount);
    }

    @Override
    public ListResult<MessageTrackListItem> getMessageClickTrackList(ListingFilterParameter fp) {
        List<Object[]> tracks = linkTrackManager.getClickedEntitiesList(fp);
        Integer totalCount = linkTrackManager.getClickedEntitiesCount(fp);
        ArrayList<MessageTrackListItem> results = new ArrayList<>();
        for (Object[] track : tracks) {
            MessageTrackListItem res = new MessageTrackListItem();
            res.setEntityID((Integer) track[0]);
            res.setEmail((String) track[1]);
            res.setFirstName((String) track[2]);
            res.setLastName((String) track[3]);
            res.setContactType((Integer) track[4]);
            res.setCrmAccountID((Integer) track[5]);
            res.setLink((String) track[6]);
            res.setClickCount((Integer) track[7]);
            results.add(res);
        }
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MessageTrackListItem> getMessageBouncedList(ListingFilterParameter fp) {
        List<EdsCrmContact> bouncedMembers = crmEntitySendMessageStatusManager.getBouncedEntitiesList(fp);
        Integer totalCount = crmEntitySendMessageStatusManager.getBouncedEntitiesCount(fp).intValue();
        ArrayList<MessageTrackListItem> results = new ArrayList<>();
        bouncedMembers.forEach(contact -> {
            MessageTrackListItem res = new MessageTrackListItem();
            res.setEntityID(contact.getObjectID());
            res.setEmail(contact.getPrimaryEmail());
            res.setFirstName(contact.getFirstName());
            res.setLastName(contact.getLastName());
            res.setContactType(contact.getContactType());
            res.setCrmAccountID(contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : null);
            res.setCountry(contact.getPrimaryAddressFromAll() != null ? contact.getPrimaryAddressFromAll().getCountry() : null);
            results.add(res);
        });
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<MessageTrackListItem> getUnsubscribedList(ListingFilterParameter fp) {
        List<EdsCrmContact> unsubscribedMembers = messageUnsubscribersManager.getEntitiesByMessageID(fp);
        Integer totalCount = messageUnsubscribersManager.getEntitiesCountByMessageID(fp).intValue();
        ArrayList<MessageTrackListItem> results = new ArrayList<>();
        unsubscribedMembers.forEach(contact -> {
            MessageTrackListItem res = new MessageTrackListItem();
            res.setEntityID(contact.getObjectID());
            res.setEmail(contact.getPrimaryEmail());
            res.setFirstName(contact.getFirstName());
            res.setLastName(contact.getLastName());
            res.setContactType(contact.getContactType());
            res.setCrmAccountID(contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : null);
            res.setCountry(contact.getPrimaryAddressFromAll() != null ? contact.getPrimaryAddressFromAll().getCountry() : null);
            results.add(res);
        });
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long checkMassMailLimit(ArrayList<Integer> mailListIDs, Date scheduled) {
        EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        long limit = settings != null && settings.getMassMailLimit() != null ? settings.getMassMailLimit() : EdsCompanySystemSettings.MASS_MAIL_LIMIT;
        long totalCountOfRecipients = 0L;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduled == null ? new Date() : scheduled);
        Long totalSentByDate = crmEntityMailListManager.getCountByDay(calendar);
        if (mailListIDs != null && mailListIDs.size() > 0) {
            totalCountOfRecipients = crmEntityMailListManager.getCrmEntityCount(mailListIDs);
        }
        if (limit < (totalCountOfRecipients + totalSentByDate)) {
            return limit;
        } else {
            return (long) -1;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getFromEmailsAsSelectItem(String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setLookUp(true);
        filterParametrs.setSearchButton(true);
        filterParametrs.setAllEmployees(true);
        filterParametrs.setSearchKey(searchKey);
        ListResult<EmployeeListItem> employeeFromSolr = employeeServiceLocal.getEmployeeList(filterParametrs);
        ArrayList<SelectItem> items = new ArrayList<>();
        if (employeeFromSolr != null && employeeFromSolr.getList() != null && employeeFromSolr.getList().size() > 0) {
            for (EmployeeListItem employee : employeeFromSolr.getList()) {
                if (employee.getEmail() != null) {
                    items.add(new SelectItem(employee.getObjectID(), employee.getEmail(), employee.getFullName()));
                }
            }
        }
        return items.toArray(new SelectItem[]{});
    }

    public void sendTestEmail(MailMessageItem item, String toEmail) {
        String msg = "";
        if (item.isHtml() && item.getPreheader() != null) {
            msg += "<span style=\"display: none !important; visibility: hidden; opacity: 0; color: transparent; height: 0; width: 0;\">" + item.getPreheader() + " - </span>";
        }
        msg += item.getContent();
        List<Upload> attachments = null;
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            List<EdsFileHeader> fileHeaders = new ArrayList<>();
            attachments = new ArrayList<>();
            if (item.getObjectID() != null) {
                EdsFolder messageCenterFolder = folderManager.getFolderByFolderType(EdsFolder.F_MASS_MAILING);
                if (messageCenterFolder != null && folderManager.getUser() != null) {
                    List<FileResource> fileResources = attachmentUtilsManager.getAttachments(F_MASS_MAILING, messageCenterFolder.getObjectID(), item.getObjectID(), folderManager.getUser());
                    if (fileResources != null && fileResources.size() > 0) {
                        for (FileResource resource : fileResources) {
                            EdsFileHeader fileHeader = fileHeaderManager.get(resource.getObjectId());
                            if (fileHeader != null) {
                                fileHeaders.add(fileHeader);
                            }
                        }
                    }
                }
            } else {
                FolderResource tempFolder = commonServiceLocal.getTempFolder();
                if (tempFolder != null) {
                    for (FileItem file : item.getAttachments()) {
                        EdsFileHeader fileHeader = fileHeaderManager.getFile(tempFolder.getObjectId(), file.getFileName());
                        if (fileHeader != null) {
                            fileHeaders.add(fileHeader);
                        }
                    }
                }
            }
            if (fileHeaders.size() > 0) {
                try {
                    for (EdsFileHeader fileHeader : fileHeaders) {
                        EdsFileBody fileBody = fileHeader.getCurrentBody();
                        Upload uploadItem = new Upload();
                        InputStream inputStream = uploadManager.getInputStream(fileBody);
                        uploadItem.setFileName(fileBody.getOriginalName());
                        uploadItem.setContentType(fileBody.getContentType());
                        uploadItem.setDataHandler(new DataHandler(new InputStreamDataSource(fileBody.getOriginalName(), fileBody.getContentType(), inputStream)));
                        attachments.add(uploadItem);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendMessage(item.getSubject(), item.getFrom(), item.getFullName(), msg, toEmail, item.isHtml(), attachments);
    }

    public void sendMessage(String subject, String from, String fullName, String msg, String to, boolean isHtml, List<Upload> attachments) {
        //Mail Body
        Integer companyID = SecurityContext.getCompanyID();
        MassMailParams settings = companyID != null ? globalAuthJdbcSpringManager.getMassMailParamsByCompany(companyID) : new MassMailParams();
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", settings.getHost());
            properties.put("mail.smtp.user", settings.getLogin());
            properties.put("mail.smtp.auth", settings.isSmtpAuth());
            properties.put("mail.smtp.port", settings.getPort());
            properties.put("mail.smtp.localhost", settings.getHost());

            Authenticator auth = new EdsSMTPAuthenticator(settings.getLogin(), settings.getPassword());
            Session session = Session.getInstance(properties, auth);

            MimeMessage message = new MimeMessage(session);
            /* set mail subjects */
            Multipart multipart = isHtml ? new MimeMultipart("related") : new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            message.setSubject(subject, "UTF-8");
            if (fullName != null && fullName.length() > 0) {
                message.setFrom(new InternetAddress(from, fullName));
            } else {
                message.setFrom(new InternetAddress(from));
            }
            if (isHtml) {
                messageBodyPart.setContent(msg, "text/html; charset=utf-8");
                messageBodyPart.setHeader("Content-Type", "text/html; charset=utf-8");
            } else {
                messageBodyPart.setText(msg, "utf-8");
            }
            multipart.addBodyPart(messageBodyPart);
            if (attachments != null && attachments.size() > 0) {
                for (Upload upload : attachments) {
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(upload.getDataHandler());
                    messageBodyPart.setFileName(upload.getFileName());
                    messageBodyPart.addHeader("Content-Type", upload.getContentType() + "; charset=UTF-8");
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            message.setContent(multipart);
            try {
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerMessageOpening(Integer entityID, Integer mailMessageID, String ipAddress) {
        Date start = new Date();
        EdsMailMessageTrack track = mailMessageTrackManager.getByEntityAndMessage(entityID, mailMessageID);
        if (track != null) {
            track.setOpenedCount(track.getOpenedCount() + 1);//increment view count
            mailMessageTrackManager.update(track);
        } else {
            track = new EdsMailMessageTrack();
            track.setEntity(crmContactManager.load(entityID));
            track.setMessage(mailMessageManager.load(mailMessageID));
            track.setIPAddress(ipAddress);
            track.setOpenedCount(1);
            mailMessageTrackManager.create(track);
        }
        log.info("MassMailingServiceImpl.registerMessageOpening() took :" + ((new Date()).getTime() - start.getTime()) + "ms.");
    }

    @Transactional
    public List<Object[]> getEntitiesForMassMail(Integer mailMessageID, Integer companyID, int loop) {
        if (mailMessageID != null && companyID != null) {
            try {
                List<Object[]> entities = mailListMessageManager.getQueuedMessagesForContact(mailMessageID, companyID, loop);
                EdsMailMessage message = mailMessageManager.get(mailMessageID);
                updateLinkTrackers(message);
                if (message.isSmsMessage() && message.getSmsSettings() != null) {
                    EdsSmsSettings.senderCollector.put(companyID, message.getSmsSettings().initProvider(null));
                }
                message.setStatusCode(MessageStatusEnum.IN_PROGRESS);
                return entities;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private void updateLinkTrackers(EdsMailMessage message) {
        if (message != null) {
            linkManager.deleteAll(message.getObjectID());
            String content = message.getContent();
            StringBuilder newContent = new StringBuilder();
            int theLastEnd = 0;
            if (content != null) {
                Pattern p = Pattern.compile(REGEX_URL);
                Matcher m = p.matcher(content);
                while (m.find()) {
                    String url = m.group();
                    if (url != null && url.contains(".")) {
                        String extension = url.substring(url.lastIndexOf(".") + 1);
                        if (extension.toLowerCase().matches("(jpg|jpeg|png|bmp|tiff|gif|ico|pdf)")) {
                            continue;
                        }
                    }
                    int start = theLastEnd;
                    theLastEnd = m.end();
                    newContent.append(content, start, m.start());
                    if (url.matches(REGEX_URL) && newContent.toString().endsWith("href=\"")) {
                        url = url.replace("&amp;", "&");//temporary fix GWTdagi editor & ni avtomaticheski &amp; deb yozib ketyapti urlda.
                        EdsLink link = new EdsLink();
                        if (url != null && !"".equals(url.trim()) && url.contains("googleanalytics=1")) {
                            url = url.replace("googleanalytics=1", "utm_source=Workforcetrack&utm_medium=MassMail&utm_term=crmEntityBodygetEntityID&utm_campaign=" + (message.getCampaign() != null ? message.getCampaign().getName() : ""));
                        }
                        link.setOriginalLink(url);
                        link.setCompanyID(SecurityContext.getCompanyID());
                        link.setMessageID(message.getObjectID());
                        link.setKpiLink(EncryptionHelper.encryptURL(url));
                        url = EdsContextParams.getHost() + "/" + EdsLink.MASS_MAILING_TRACKER + link.getKpiLink() + "&eid=n&mid=n&c_id=" + EncryptionHelper.encryptURL(SecurityContext.getCompanyID().toString());
                        linkManager.create(link);
                    }
                    newContent.append(url);
                }
                if (content.length() - 1 > theLastEnd) {
                    newContent.append(content.substring(theLastEnd));
                }
            }
            message.setRelinkedMessage(newContent.toString());
        }

    }

    @Transactional
    public void sendMassMail(Integer mailMessageID, Integer companyID, List<Object[]> entities) {
        try {
            EdsMailMessage message = mailMessageManager.get(mailMessageID);
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            List<Integer> fileHeaderIds = new ArrayList<>();
            if (!message.isSmsMessage()) {
                List<FileResource> fileResources = attachmentUtilsManager.getAttachments(F_MASS_MAILING, message.getObjectID(), message.getObjectID(), message.getCreator());
                if (fileResources != null && fileResources.size() > 0) {
                    for (FileResource resource : fileResources) {
                        fileHeaderIds.add(resource.getObjectId());
                    }
                }
            }
            EdsUser sender = userManager.getUserByEmail(message.getFromemail());
            EdsEmployee employee = sender != null ? employeeManager.get(sender.getObjectID()) : null;
            EdsClientContact clientContact = sender != null ? clientContactManager.get(sender.getObjectID()) : null;
            EdsCrmContact senderContact = employee != null ? employee.getContact() : (clientContact != null ? clientContact.getCrmContact() : null);
            String senderTitle = senderContact == null ? "" : senderContact.getTitleRef() == null ? senderContact.getTitle() : referenceWfmMessageSource.localizeRef(senderContact.getTitleRef());
            String senderPhone = senderContact == null ? "" : senderContact.getPrimaryPhone();
            String senderFirstName = sender == null ? "" : sender.getFirstName();
            String senderLastName = sender == null ? "" : sender.getLastName();
            String senderCompanyName = sender != null && sender.getCompany() != null ? sender.getCompany().getName() : "";
            String senderEmail = sender == null ? "" : sender.getEmail();
            //Mail Body
            MassMailerBody mailBody = new MassMailerBody();
            mailBody.setFullName(message.getFullName());
            mailBody.setSubject(message.getSubject());
            mailBody.setPreheader(message.getPreheader());
            mailBody.setFrom(message.getFromemail());
            mailBody.setHtml(message.isHtml());
            mailBody.setFileHeaderIds(fileHeaderIds);
            mailBody.setReplyTo(message.getReplyTo());
            mailBody.setMessage(message.getRelinkedMessage() != null
                    ? message.getRelinkedMessage()
                    : message.getContent());
            MassMailParams settings = companyID != null ? globalAuthJdbcSpringManager.getMassMailParamsByCompany(companyID) : new MassMailParams();
            mailBody.setHost(settings.getHost());
            mailBody.setPort(settings.getPort());
            mailBody.setLogin(settings.getLogin());
            mailBody.setPassword(settings.getPassword());
            mailBody.setBouncerEmail(settings.getBouncedEmail());
            mailBody.setAbuseEmail(settings.getAbuseEmail());
            mailBody.setTolerateText(settings.getTolerateText());
            mailBody.setTolerateHTML(settings.getTolerateHtml());
            mailBody.setUnsubscribeText(settings.getUnsubscribeText());
            mailBody.setUnsubscribeHTML(settings.getUnsubscribeHtml());
            mailBody.setSmtpAuth(settings.isSmtpAuth());
            //Entity Body
            for (Object[] object : entities) {
                Integer entityId = (Integer) object[0];
                String firstName = (String) object[1];
                String lastName = (String) object[2];
                String email = (String) object[3];
                String title = (String) object[4];
                String companyName = (String) object[5];
                Integer mailListId = (Integer) object[6];
                Integer campaignID = (Integer) object[7];
                String campaignName = (String) object[8];
                String phone = String.valueOf(object[9]);
                String mobile = String.valueOf(object[10]);
                phone = phone != null && !"".equals(phone.trim()) ? phone.replace("|", "") : null;
                mobile = mobile != null && !"".equals(mobile.trim()) ? mobile.replace("|", "") : null;

                MassMailerCrmEntityBody crmEntityBody = new MassMailerCrmEntityBody();
                crmEntityBody.setEntityID(entityId);
                if (sender != null) {
                    crmEntityBody.setSenderTitle(senderTitle);
                    crmEntityBody.setSenderFirstName(senderFirstName);
                    crmEntityBody.setSenderSurname(senderLastName);
                    crmEntityBody.setSenderEmail(senderEmail);
                    crmEntityBody.setSenderPhoneNumber(senderPhone);
                    crmEntityBody.setSenderCompanyName(senderCompanyName);
                }
                crmEntityBody.setMailListId(mailListId);
                crmEntityBody.setMsgId(mailMessageID);
                crmEntityBody.setDatabase(settings.getClusterType());
                crmEntityBody.setCompanyID(companyID != null ? String.valueOf(companyID) : null);
                crmEntityBody.setCampaignID(campaignID);
                crmEntityBody.setCampaignName(campaignName);
                crmEntityBody.setRecepientTitle(title);
                crmEntityBody.setRecipientFirstName(firstName);
                crmEntityBody.setRecipientLastName(lastName);
                crmEntityBody.setRecipientCompanyName(companyName);
                crmEntityBody.setRecipientEmail(email);
                crmEntityBody.setRecipientPhone(phone);
                crmEntityBody.setRecipientMobile(mobile);

                try {
                    if (message.isSmsMessage()) {
                        MassMailerData data = new MassMailerData();
                        data.setBody(mailBody);
                        data.setCrmEntityBody(crmEntityBody);
                        data.setSendType(message.isSmsMessage() ? MassSpamSender.SMS : MassSpamSender.EMAIL);
                        rabbitMQService.emailSending(data);
                    } else {
                        messageManager.sendMessageFromUser(message.getFromemail(),email,null, null,message.getSubject(),
                                message.getRelinkedMessage() != null
                                        ? message.getRelinkedMessage()
                                        : message.getContent(),
                                null,message.getReplyTo(),null,false,null,companyID,sender);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MailListItem getMailList(Integer object_id) {
        return mailListManager.get(object_id).getRPC();
    }

    @Override
    public Integer saveMailList(MailListItem item, ListingFilterParameter fp, boolean isLead) {
        EdsUser user = mailListManager.getUser();
        EdsMailList mailList = item.getObjectId() != null ? mailListManager.get(item.getObjectId()) : new EdsMailList();
        mailList.setName(item.getName());
        mailList.setDescription(item.getDescription());
        mailList.setActive(item.isActive());
        if (item.getObjectId() == null) {
            mailList.setOwnerID(user.getObjectID());
            mailList.setCreationTime(new Date());
        }
        mailListManager.createOrUpdate(mailList);
        baseEventPostProcessor.registerEvent(CrmMailingListEventListenerImpl.TYPE, item.getObjectId() == null ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, mailList, user);
        if (item.getObjectId() == null) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CrmMailingListCustomEventListenerImpl.TYPE, CrmMailingListCustomEventListenerImpl.EVENT_MAIL_LIST_ADD, mailList, user);
            List<Integer> ids = new ArrayList<>();
            if (item.getMembers() != null && item.getMembers().length > 0) {
                for (ContactListItem contactItem : item.getMembers()) {
                    ids.add(contactItem.getObjectId());
                }
                event.setCustomStringField(ServerUtils.getAsCommoDelimited(ids, "0", ","));
            } else if (fp != null) {
                fp.setIDsOnly(true);
                fp.setAllByFilter(true);
                //bu leadligini biliw uchun
                fp.setForCSVonly(isLead);
                StringBuilder params = new StringBuilder();
                HashMap<String, String> requestParams = fp.getRequestParams();
                for (Map.Entry<String, String> stringStringEntry : requestParams.entrySet()) {
                    Map.Entry entry = stringStringEntry;
                    params.append(entry.getKey()).append("::").append(entry.getValue()).append("||");
                }
                event.setCustomStringField(params.toString());
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(item.getObjectId() == null ? KpiLog.ActionType.ADD : KpiLog.ActionType.UPDATE);
        ServerUtils.kpiLog(log, kpiLog, item.getObjectId() == null ? "Add MailList" : "Update MailList");
        kpiLog.setEntityName(EdsMailList.class.getSimpleName());
        if (mailList.getObjectID() != null) {
            kpiLog.setEntityId(mailList.getObjectID());
        }
        return mailList.getObjectID();
    }

    @Override
    public void deleteMailList(Integer mailListID) {
        EdsMailList mailList = mailListManager.get(mailListID);
        mailList.setDeleted(Boolean.TRUE);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsMailList.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(mailListID);
        ServerUtils.kpiLog(log, kpiLog, "Delete MailList");
    }

    @Transactional
    public void deleteMails(ArrayList<Integer> mailIDs) {
        mailIDs.forEach(this::deleteMailList);
    }

    @Override
    public void updateCrmEntityMailLists(ListingFilterParameter fp, ArrayList<Integer> checkedMailLists) {
        List<EdsCrmEntityMailList> currentMailLists;
        List<Integer> unsubscribes;
        List<Integer> currentMailListIDs;
        EdsCrmContact entity;
        if (fp != null && fp.getContactID() != null) {
            currentMailLists = crmEntityMailListManager.getList(fp);
            if (CollectionUtils.isEmpty(currentMailLists) && CollectionUtils.isEmpty(checkedMailLists)) {
                return;
            }
            currentMailListIDs = crmEntityMailListManager.getCrmEntitiesSubscribedLists(fp.getContactID());
            unsubscribes = crmEntityMailListManager.getCrmEntitiesUnsubscribedLists(fp.getContactID());
            EdsCrmEntityMailList newLmList;
            EdsMailList mList;
            if (checkedMailLists != null && checkedMailLists.size() > 0) {
                if (currentMailLists != null && currentMailLists.size() > 0) {
                    for (EdsCrmEntityMailList lmList : currentMailLists) {
                        if (lmList.getMailList() == null || !checkedMailLists.contains(lmList.getMailList().getObjectID())) {
                            lmList.setDeleted(true);
                            crmEntityMailListManager.update(lmList);
                        }
                    }
                }
                entity = crmContactManager.get(fp.getContactID());
                for (Integer mlID : checkedMailLists) {
                    if ((currentMailListIDs != null && !currentMailListIDs.contains(mlID)) || currentMailListIDs.size() == 0) {
                        if (unsubscribes.contains(mlID)) {
                            EdsCrmEntityMailList leadMailList = crmEntityMailListManager.getMailListDeletedEntity(mlID, fp.getContactID());
                            leadMailList.setDeleted(false);
                            crmEntityMailListManager.update(leadMailList);
                        } else {
                            mList = mailListManager.get(mlID);
                            newLmList = new EdsCrmEntityMailList();
                            newLmList.setDeleted(false);
                            newLmList.setEntity(entity);
                            newLmList.setMailList(mList);
                            crmEntityMailListManager.create(newLmList);
                        }
                    }
                }
            } else if (currentMailLists != null && currentMailLists.size() > 0) {
                for (EdsCrmEntityMailList lmList : currentMailLists) {
                    lmList.setDeleted(true);
                    crmEntityMailListManager.update(lmList);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getMailListByCrmEntityID(Integer contactID) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        if (user != null) {
            fp.setShowInListing(user.hasRole(roleManager.get(ADMIN)) || user.hasRole(roleManager.get(DR)) || user.hasRole(roleManager.get(SALESMAN)));
        }
        List<EdsMailList> allMailLists = mailListManager.getList(fp);
        List<Integer> subscribedLists = new ArrayList<>();
        if (contactID != null) {
            subscribedLists = crmEntityMailListManager.getCrmEntitiesSubscribedLists(contactID);
        }
        List<SelectItem> result = new ArrayList<>();
        for (EdsMailList ml : allMailLists) {
            SelectItem item = new SelectItem(ml.getObjectID(), ml.getName());
            item.setSelected(subscribedLists.contains(ml.getObjectID()));
            result.add(item);
        }
        return result.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MailMessageItem getMailMessage(Integer objectId, boolean isSMS, boolean isViewMode) {
        EdsMailMessage mailMessage = objectId != null ? mailMessageManager.get(objectId) : null;
        MailMessageItem item = new MailMessageItem();
        if (!isViewMode) {
            item.setTemplates(emailTemplateServiceLocal.getEmailTemplates(CRM_MASS_MAILING_CATEGORY));
            item.setPersonalAttributes(new ArrayList<>(EmailTemplateUtils.getCrmMailListCategoryFields().keySet()));
            if (isSMS) {
                List<EdsSmsSettings> smsSettingses = smsManager.list(new ListingFilterParameter());
                ArrayList<SelectItem> sms = new ArrayList<>();
                smsSettingses.forEach(s -> sms.add(new SelectItem(s.getObjectID(), s.getName())));
                item.setSenders(sms.toArray(new SelectItem[]{}));
            }
        } else {
            List<EdsMailList> mailLists = mailListMessageManager.getMailListsByMessage(objectId);
            StringBuilder mailListNames = new StringBuilder();
            for (EdsMailList ml : mailLists) {
                if (ml.getName() != null && !"".equals(ml.getName().trim())) {
                    mailListNames.append(mailListNames.length() == 0 ? ml.getName() : ", " + ml.getName());
                }
            }
            item.setSubscribedLists(mailListNames.toString());
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setMessageId(objectId);
            Long entitiesCount = mailListManager.getLeadCountByMessageID(objectId);
            Long sentCount = crmEntitySendMessageStatusManager.getStatusCountByMessageID(objectId, null);
            Long deliveryCount = crmEntitySendMessageStatusManager.getStatusCountByMessageID(objectId, MessageStatusEnum.SENT);
            Long bouncedCount = sentCount - deliveryCount;
            Long viewCount = mailMessageTrackManager.getViewCountByMessage(objectId);
            Long clickCount = linkTrackManager.getClickCountByMessageID(objectId);
            BigDecimal sentTemp = BigDecimal.valueOf(sentCount);
            BigDecimal deliveryTemp = BigDecimal.valueOf(deliveryCount);
            BigDecimal bouncedTemp = BigDecimal.valueOf(bouncedCount);
            BigDecimal viewTemp = BigDecimal.valueOf(viewCount);
            BigDecimal clickTemp = BigDecimal.valueOf(clickCount);
            BigDecimal deliveryRateTemp = sentCount > 0 ? deliveryTemp.multiply(new BigDecimal(100)).divide(sentTemp, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal bouncedRateTemp = sentCount > 0 ? bouncedTemp.multiply(new BigDecimal(100)).divide(sentTemp, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal viewRateTemp = deliveryCount > 0 ? viewTemp.multiply(new BigDecimal(100)).divide(deliveryTemp, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal clickRateTemp = viewCount > 0 ? clickTemp.multiply(new BigDecimal(100)).divide(viewTemp, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            item.setEntitiesCount(entitiesCount);
            item.setSentCount(sentCount);
            item.setDeliveryCount(deliveryCount);
            item.setDeliveryRate(deliveryRateTemp.toString());
            item.setBouncedCount(bouncedCount);
            item.setBouncedRate(bouncedRateTemp.toString());
            fp.setObjectId(objectId);
            item.setUnsubscribedCount(messageUnsubscribersManager.getEntitiesCountByMessageID(fp));
            item.setViewCount(viewCount);
            item.setViewRate(viewRateTemp.toString());
            item.setClickCount(clickCount);
            item.setClickRate(clickRateTemp.toString());
        }
        if (objectId != null) {
            EdsCompany company = recurrenceManager.getUser().getCompany();
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.MASS_MAILING_RECURRENCE, objectId, company.getObjectID());
            if (recurrence != null) {
                item.setRecurrenceId(recurrence.getObjectID());
            }
            return mailMessage.getRPC(item);
        }
        return item;
    }

    @Override
    public void deleteMailMessage(Integer objectID) {
        EdsMailMessage message = mailMessageManager.get(objectID);
        message.setDeleted(true);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsMailMessage.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Delete mail message");
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.MASS_MAILING_RECURRENCE, objectID, SecurityContext.getCompanyID());
        if (recurrence != null) {
            recurrence.setDeleted(true);
            recurrence.setChanged(true);
            recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
            recurrenceManager.update(recurrence);
        }
    }

    @Override
    public void cancelSchedule(Integer objectID) {
        EdsMailMessage message = mailMessageManager.get(objectID);
        if (message != null) {
            message.setStatusCode(MessageStatusEnum.DRAFT);
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.MASS_MAILING_RECURRENCE, objectID, SecurityContext.getCompanyID());
            if (recurrence != null) {
                recurrence.setDeleted(true);
                recurrence.setChanged(true);
                recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
                recurrenceManager.update(recurrence);
            }
        }
    }

    @Override
    public boolean trackLink(String kpiUrl, Integer entityID, Integer messageID, String ipAddress) {
        EdsLink link = linkManager.getByKpiLink(kpiUrl, messageID);
        if (link == null) {
            saveBrokenLinks(entityID, messageID, ipAddress);
            return false;
        }
        EdsLinkTrack track = linkTrackManager.getByEntityAndMessageID(messageID, entityID, link.getObjectID());
        track = track == null ? new EdsLinkTrack() : track;
        track.setEntity(entityID);
        track.setMessageID(messageID);
        track.setLinkID(link.getObjectID());
        track.setIPAddress(ipAddress);
        track.setOpenedCount(track.getOpenedCount() + 1);
        linkTrackManager.createOrUpdate(track);

        EdsLinkTrackDate edsLinkTrackDate = new EdsLinkTrackDate();
        edsLinkTrackDate.setLinktrackId(track.getObjectID());
        edsLinkTrackDate.setTrackDate(new Date());
        linkTrackManager.getJpaTemplate().persist(edsLinkTrackDate);

        return true;
    }

    private void saveBrokenLinks(Integer entityID, Integer messageID, String ipAddress) {
        EdsBrokenLinkTrack track = new EdsBrokenLinkTrack();
        track.setEntity(entityID);
        track.setMessageID(messageID);
        track.setIPAddress(ipAddress);
        track.setOpenedCount(track.getOpenedCount() + 1);
        track.setTrackDate(new Date());
        linkTrackManager.getJpaTemplate().persist(track);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getMailListsByMessage(Integer messageID) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowInListing(user.hasRole(roleManager.get(ADMIN)) || user.hasRole(roleManager.get(DR)) || user.hasRole(roleManager.get(SALESMAN)));
        List<EdsMailList> allMailLists = mailListManager.getList(fp);
        List<EdsMailList> subscribedLists = new ArrayList<>();
        if (messageID != null) {
            subscribedLists = mailListMessageManager.getMailListsByMessage(messageID);
        }
        List<SelectItem> result = new ArrayList<>();
        for (EdsMailList ml : allMailLists) {
            SelectItem item = new SelectItem(ml.getObjectID(), ml.getName());
            item.setSelected(subscribedLists.contains(ml));
            result.add(item);
        }
        return result.toArray(new SelectItem[]{});
    }

    public SelectItem[] getSubscribedListsByCrmEntityId(Integer crmEntityID) {
        if (crmEntityID != null) {
            List<EdsCrmEntityMailList> mailList = crmEntityMailListManager.getSubscribedListsByCrmEntityId(crmEntityID);
            SelectItem[] result = new SelectItem[mailList.size()];
            //if (mailList != null && mailList.size() > 0) {
                int i = 0;
                for (EdsCrmEntityMailList item : mailList) {
                    //if (item.getMailList() != null) {
                        result[i++] = new SelectItem(item.getObjectID(), item.getMailList().getName());
                    //}
                }
                return result;
            //}
        }
        return null;
    }
}
