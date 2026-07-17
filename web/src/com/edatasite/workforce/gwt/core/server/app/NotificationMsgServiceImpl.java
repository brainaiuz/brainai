package com.edatasite.workforce.gwt.core.server.app;


import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessageSetting;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.NotificationMsgService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgSettingManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.mail.EdsSubjects;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by dilsh0d on 09.07.15.
 */
@Service("notificationMsgService")
public class NotificationMsgServiceImpl implements NotificationMsgService, NotificationMsgServiceLocal {
    private static final DecimalFormat numberFormat = new DecimalFormat(",##0.00");
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("notificationLocalizer")
    private WfmMessageSource notificationWfmMessageSource;
    @Autowired
    @Qualifier("notificationShortLocalizer")
    private WfmMessageSource notificationShortWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private MeetingManager meetingManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private NotificationMsgSettingManager notificationMsgSettingManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private MessageCenterService messageCenterService;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;

    @Override
    @Transactional(readOnly = true)
    public Integer getNewNotifications(ArrayList<NotificationTypeEnum> entityTypes) {
        return notificationMsgManager.getNewNotificationsIds(entityTypes);
    }

    /**
     * Generated new notifications list
     */
    @Override
    @Transactional(readOnly = true)
    public ListResult<NotificationItem> getNewNotifications() {
        Integer total = getNewNotifications(null);
        if (total == null || total <= 0) {
            return new ListResult<>(Lists.newArrayList(), 0);
        }
        List<EdsNotificationMessage> edsNotificationMessageList = notificationMsgManager.getNewLatestTen();
        ArrayList<NotificationItem> notificationItems = new ArrayList<>();
        for (EdsNotificationMessage edsNotificationMessage : edsNotificationMessageList) {
            NotificationItem item = edsNotificationMessage.getNewNotificationItem();
            item.setName(edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : generatedNewNotificationName(edsNotificationMessage, false));
            EdsUser actorUser = userManager.get(edsNotificationMessage.getActorUserID());
            item.setUserInfo(actorUser.getFullName());
            if (actorUser.getPhoto() != null) {
                item.setActorUserImg(commonServiceLocal.getImageUrl(actorUser.getPhoto().getObjectID()));
            }
            generatedActionUrl(edsNotificationMessage, item);
            notificationItems.add(item);

        }
        return new ListResult<>(notificationItems, total);
    }

    /**
     * Generated for notifications listing
     *
     */
    @Override
    @Transactional(propagation = Propagation.NESTED)
    public ListResult<NotificationItem> getNotificationsList(ListingFilterParameter filterParametrs) {
        Long total = notificationMsgManager.getListTotal(filterParametrs, null);
        List<EdsNotificationMessage> notificationMessageList = notificationMsgManager.getList(filterParametrs, null);
        ArrayList<NotificationItem> items = new ArrayList<>();

        for (EdsNotificationMessage edsNotificationMessage : notificationMessageList) {
            EdsEmployee edsEmployee = employeeManager.get(edsNotificationMessage.getActorUserID());

            NotificationItem item = edsNotificationMessage.getNotificationItem();
            item.setName(edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : generatedNotificationNameForListing(edsNotificationMessage));
            generatedActionUrl(edsNotificationMessage, item);
            item.setValue(notificationWfmMessageSource.localize(item.getEntityType()));
            item.setActorUserName(edsEmployee.getFullName());
            EdsUser actorUser = userManager.get(edsNotificationMessage.getActorUserID());
            if (actorUser.getPhoto() != null) {
                item.setActorUserImg(commonServiceLocal.getImageUrl(actorUser.getPhoto().getObjectID()));
            }

            String userInfo = "(";
            if (!ServerUtils.isNullOrEmpty(edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null)) {
                userInfo += edsEmployee.getProfile().getEmployeeCode();
            }
            if (edsEmployee.getTeam() != null) {
                if (!userInfo.equals("(")) {
                    userInfo += ", ";
                }
                userInfo += edsEmployee.getTeam().getName();
            }
            if (edsEmployee.getPosition() != null) {
                if (!userInfo.equals("(")) {
                    userInfo += ", ";
                }
                userInfo += edsEmployee.getPosition().getName();
            }
            userInfo += ")";
            item.setUserInfo(userInfo);

            items.add(item);
        }

//        allInOneServiceLocal.updateNewNotifications(notificationMsgManager.getNewLatestIds());

        return new ListResult<>(items, total.intValue());
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public ListResult<NotificationItem> getNotificationsList(ListingFilterParameter filterParameter, ArrayList<NotificationTypeEnum> entityTypes) {
        Long total = notificationMsgManager.getListTotal(filterParameter, entityTypes);
        List<EdsNotificationMessage> notificationMessageList = notificationMsgManager.getList(filterParameter, entityTypes);
        ArrayList<NotificationItem> items = new ArrayList<>();

        for (EdsNotificationMessage edsNotificationMessage : notificationMessageList) {
            EdsEmployee edsEmployee = employeeManager.get(edsNotificationMessage.getActorUserID());

            NotificationItem item = edsNotificationMessage.getNotificationItem();
            item.setName(edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : generatedNotificationNameForListing(edsNotificationMessage));
            generatedActionUrl(edsNotificationMessage, item);
            item.setActorUserName(edsEmployee.getFullName());
            EdsUser actorUser = userManager.get(edsNotificationMessage.getActorUserID());
            if (actorUser.getPhoto() != null) {
                item.setActorUserImg(commonServiceLocal.getImageUrl(actorUser.getPhoto().getObjectID()));
            }

            //String userInfo = " (";
            StringBuilder userInfo = new StringBuilder();
            userInfo.append(" (");
            if (!ServerUtils.isNullOrEmpty(edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null)) {
                userInfo.append(edsEmployee.getProfile().getEmployeeCode());
            }
            if (edsEmployee.getTeam() != null) {
                if (!userInfo.toString().equals("(")) {
                    userInfo.append(", ");
                }
                userInfo.append(edsEmployee.getTeam().getName());
            }
            if (edsEmployee.getPosition() != null) {
                if (!userInfo.toString().equals("(")) {
                    userInfo.append(", ");
                }
                userInfo.append(edsEmployee.getPosition().getName().trim());
            }
            userInfo.append(")");
            item.setUserInfo(userInfo.toString());

            items.add(item);
        }

        return new ListResult<>(items, total.intValue());
    }

    @Override
    @Transactional(readOnly = true)
    public SelectItem[] getCategoriesList(Boolean isShortName) {
        List<SelectItem> category = new ArrayList<>();
        Map<NotificationTypeEnum, EdsNotificationMessageSetting> mapResult = notificationMsgSettingManager.getNotificationSettingList();
        if (isShortName) {
            for (NotificationTypeEnum typeEnum : NotificationTypeEnum.values()) {
                if (mapResult.containsKey(typeEnum)) {
                    if (mapResult.get(typeEnum).getIsShow()) {
                        category.add(new SelectItem(typeEnum.getId(), notificationShortWfmMessageSource.localize(typeEnum.name()), typeEnum.name()));
                    }
                } else if (typeEnum.isDefaultSentEvent()) {
                    category.add(new SelectItem(typeEnum.getId(), notificationShortWfmMessageSource.localize(typeEnum.name()), typeEnum.name()));
                }
            }
        } else {
            for (NotificationTypeEnum typeEnum : NotificationTypeEnum.values()) {
                if (mapResult.containsKey(typeEnum)) {
                    if (mapResult.get(typeEnum).getIsShow()) {
                        category.add(new SelectItem(typeEnum.getId(), notificationWfmMessageSource.localize(typeEnum.name()), typeEnum.name()));
                    }
                } else if (typeEnum.isDefaultSentEvent()) {
                    category.add(new SelectItem(typeEnum.getId(), notificationWfmMessageSource.localize(typeEnum.name()), typeEnum.name()));
                }
            }
        }
        return category.toArray(new SelectItem[0]);
    }

    @Override
    @Transactional
    public void updateClicked(Integer id) {
        EdsNotificationMessage edsNotificationMessage = notificationMsgManager.get(id);
        edsNotificationMessage.setClicked(true);
        edsNotificationMessage.setRead(true);
        notificationMsgManager.update(edsNotificationMessage);

        Integer viewerUserID = userManager.getUser().getObjectID();
        if (viewerUserID != null && viewerUserID > 0) {
            WebSocketServerObject notification = new WebSocketServerObject();
            notification.setData(null);
            notification.setEventType(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE);
            notification.setUserId(viewerUserID);
            try {
                rabbitMQService.sendWebPushNotification(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void clearAll() {
        notificationMsgManager.clearAllUnreadNotifications();
    }

    @Override
    @Transactional
    public void clearAll(ArrayList<NotificationTypeEnum> entityList) {
        notificationMsgManager.clearUserUnreadMsg(entityList);
        Integer viewerUserID = userManager.getUser().getObjectID();
        if (viewerUserID != null && viewerUserID > 0) {
            WebSocketServerObject notification = new WebSocketServerObject();
            notification.setData(null);
            notification.setEventType(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE);
            notification.setUserId(viewerUserID);
            try {
                rabbitMQService.sendWebPushNotification(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void updateUserToken(String deviceToken, String deviceType) {
        EdsUser user = userManager.getUser();
        if (user != null) {
            user.setDeviceToken(deviceToken);
            user.setMobileDeviceType(DeviceTypeEnum.valueOf(deviceType));
        }
    }

    /**
     * Getting notification by request type
     *
     */
    @Override
    @Transactional
    public ArrayList<NotificationItem> getNotificationsByRequestType(ListingFilterParameter fp) {
        List<EdsNotificationMessage> notificationList = notificationMsgManager.getNotificationsByRequestType(fp);
        EdsUser user = userManager.getUser();
        ArrayList<NotificationItem> result = new ArrayList<>();
        if (notificationList != null && notificationList.size() > 0) {
            EdsUser approver;
            for (EdsNotificationMessage notification : notificationList) {
                NotificationItem item = notification.getNotificationItem();
                if (item.getViewerUserId() != null) {
                    approver = employeeManager.get(item.getViewerUserId());
                    if (approver != null) {
                        item.setApprover(user.getObjectID().equals(approver.getObjectID()) ? "your" : approver.getFirstName() + "'s");
                    }
                }
                item.setUserInfo(employeeManager.get(item.getActorUserId()).getName());
                item.setEntityType(notificationShortWfmMessageSource.localize(notification.getEntityType().name()));
                item.setModuleName(NotificationMsgManager.MODULE_NAME_HR);
                item.setDate(notification.getDate());
                generatedActionUrl(notification, item);
                result.add(item);
            }
        }
        List<EdsStepEmployee> edsStepEmployees = stepEmployeeManager.getListForApprovalWidget(fp);
        if (edsStepEmployees != null && edsStepEmployees.size() > 0) {
            for (EdsStepEmployee edsStepEmployee : edsStepEmployees) {
                if (edsStepEmployee != null) {
                    NotificationItem item = new NotificationItem();
                    if (edsStepEmployee.getEmployee() != null) {
                        item.setUserInfo(edsStepEmployee.getEmployee().getName());
                    }
                    item.setDate(edsStepEmployee.getModificationDate());
                    item.setApprover("your");
                    if (edsStepEmployee.getOnboardingStep() != null) {
                        item.setEntityType(edsStepEmployee.getOnboardingStep().getName() != null ? edsStepEmployee.getOnboardingStep().getName() : notificationShortWfmMessageSource.localize(NotificationTypeEnum.OnboardingStep.name()));
                    } else {
                        item.setEntityType(notificationShortWfmMessageSource.localize(NotificationTypeEnum.OnboardingStep.name()));
                    }
                    EdsNotificationMessage notification = new EdsNotificationMessage();
                    notification.setEntityID(edsStepEmployee.getObjectID());
                    notification.setEntityType(NotificationTypeEnum.OnboardingStep);
                    generatedActionUrl(notification, item);
                    result.add(item);
                }
            }
        }

        if (result.size() > 0) {
            result.sort((o1, o2) -> o2.getSortDate().compareTo(o1.getSortDate()));
        }

        return result;
    }

    @Override
    public HashMap<String, Integer> getNotificationCountByTypes(ListingFilterParameter fp) {
        List<Object[]> objects = notificationMsgManager.getNotificationCountByTypes(fp);
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Object[] obj : objects) {
            resultMap.put((String) obj[0], ((BigInteger) obj[1]).intValue());
        }
        return resultMap;
    }

    /*
        Generated by entity type and action on type names for menu notification event names
         */
    public String generatedNewNotificationName(EdsNotificationMessage edsNotificationMessage, boolean isIOs) {
        String name;
        switch (edsNotificationMessage.getEntityType()) {
            case LeaveRequests -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
            }
            case WhatsApp -> {
                name = "WhatsApp";
            }
            case TimeSheetApproval -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
            }
            case ExpenseClaim -> {
                name = "";
                EdsExpenseReport expenseReport = expenseReportManager.get(edsNotificationMessage.getEntityID());
                String number = expenseReport != null && expenseReport.getNumber() != null ? expenseReport.getNumber() : "";
                String amount = expenseReport != null && expenseReport.getTotal() != null ? numberFormat.format(expenseReport.getTotal()) : "0.00";
                if (ActionOnEntityEnum.WAIT_APPROVAL.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_WAIT_APPROVAL", number, amount);
                } else if (ActionOnEntityEnum.APPROVED.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_APPROVED", number, amount);
                } else if (ActionOnEntityEnum.DECLINE.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_DECLINE", number, amount);
                }
            }
            case GoogleCalendarEvent -> {
                EdsEvent event = eventManager.get(edsNotificationMessage.getEntityID());
                if (event != null && event.getSubject() != null && !"".equals(event.getSubject())) {
                    EdsUser user = userManager.get(edsNotificationMessage.getViewerUserID());
                    EdsEmployeeEvent employeeEvent = employeeEventManager.getEmployeeEvent(user, event);
                    name = commonLocalizer.localize(EdsSubjects.EVENT_REMINDER, "Event reminder") + ": " + event.getSubject() + " - " + (employeeEvent != null ? getDate(event, employeeEvent.getEmployee()) : "");
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
                }
            }
            case TaskDueReminder -> {
                EdsTask task = taskManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && task != null) {
                    name = commonLocalizer.localize(EdsSubjects.TASK_DUE_REMINDER) + ": " + "[" + task.getNumber() + "] " + task.getName();
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
                }
            }
            case MeetingMinutesNotification -> {
                EdsMeetingMinutes meeting = meetingManager.get(edsNotificationMessage.getEntityID());
                if (meeting != null) {
                    name = notificationWfmMessageSource.localize(EdsSubjects.MEETING_MINUTES) + ": " + meeting.getTitle();
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
                }
            }
            case BenefitRequest -> {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(edsNotificationMessage.getEntityID());
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name()) +
                        " " + benefitRequest.getBenefit().getName() + " " + notificationWfmMessageSource.localize(EdsSubjects.BENEFIT_REQUEST);
            }
            case ProjectDueReminder -> {
                EdsProject project = projectManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && project != null) {
                    name = commonLocalizer.localize(EdsSubjects.PROJECT_OVERDUE_REMINDER) + ": " + "[" + project.getNumber() + "] " + project.getName();
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
                }
            }
            case WorkstreamDueReminder -> {
                EdsWorkStream workStream = workStreamManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && workStream != null) {
                    name = commonLocalizer.localize(EdsSubjects.WORKSTREAM_OVERDUE_REMINDER) + ": " + "[" + workStream.getNumber() + "] " + workStream.getName();
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
                }
            }
            case CRMCase -> {
                EdsCase edsCase = caseManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsCase != null) {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), edsCase.getCaseNumberString());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case TimeSheetDueReminder -> {
                if (isIOs) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            ServerUtils.getDateAsString(edsNotificationMessage.getDate()));
                }
            }
            case InvoiceDueReminder -> {
                EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsSaleInvoice != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            edsSaleInvoice.getNumber());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case TaskAssignee -> {
                EdsTask edsTask = taskManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsTask != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            edsTask.getNumber());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case LeadAssignee -> {
                EdsCrmContact edsCrmContact = crmContactManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsCrmContact != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            edsCrmContact.getName());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case SaleInvoiceApproval -> {
                EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsSaleInvoice != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            edsSaleInvoice.getNumber());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case ProjectApproval -> {
                EdsProject edsProject = projectManager.get(edsNotificationMessage.getEntityID());
                if (!isIOs && edsProject != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(),
                            edsProject.getName());
                } else {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name(), "");
                }
            }
            case OnboardingStep -> {
                EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(edsNotificationMessage.getEntityID());
                if (edsStepEmployee != null && edsStepEmployee.getOnboardingStep() != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsStepEmployee.getOnboardingStep().getName() != null ? edsStepEmployee.getOnboardingStep().getName() : "");
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            default -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name());
            }
        }
        return name;
    }

    private String generatedNotificationNameForListing(EdsNotificationMessage edsNotificationMessage) {
        String name;
        switch (edsNotificationMessage.getEntityType()) {
            case LeaveRequests -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
            }
            case TimeSheetApproval -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
            }
            case WhatsApp -> {
                name = "WhatsApp";
            }
            case ExpenseClaim -> {
                name = "";
                EdsExpenseReport expenseReport = expenseReportManager.get(edsNotificationMessage.getEntityID());
                String number = expenseReport != null && expenseReport.getNumber() != null ? expenseReport.getNumber() : "";
                String amount = expenseReport != null && expenseReport.getTotal() != null ? numberFormat.format(expenseReport.getTotal()) : "0.00";
                if (ActionOnEntityEnum.WAIT_APPROVAL.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_WAIT_APPROVAL", number, amount);
                } else if (ActionOnEntityEnum.APPROVED.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_APPROVED", number, amount);
                } else if (ActionOnEntityEnum.DECLINE.equals(edsNotificationMessage.getActionOnEntity())) {
                    name = notificationWfmMessageSource.localizeWithParam("ExpenseClaim_DECLINE", number, amount);
                }
            }
            case GoogleCalendarEvent -> {
                EdsEvent event = eventManager.get(edsNotificationMessage.getEntityID());
                if (event != null && event.getSubject() != null && !"".equals(event.getSubject())) {
                    EdsUser user = userManager.get(edsNotificationMessage.getViewerUserID());
                    EdsEmployeeEvent employeeEvent = employeeEventManager.getEmployeeEvent(user, event);
                    name = commonLocalizer.localize(EdsSubjects.EVENT_REMINDER) + ": " + event.getSubject() + " - " + getDate(event, employeeEvent.getEmployee());
                    if (name.length() > 90) {
                        name = name.substring(0, 90) + "...";
                    }
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case TaskDueReminder -> {
                EdsTask task = taskManager.get(edsNotificationMessage.getEntityID());
                if (task != null) {
                    name = commonLocalizer.localize(EdsSubjects.TASK_DUE_REMINDER) + ": " + "[" + task.getNumber() + "] " + task.getName();
                    if (name.length() > 90) {
                        name = name.substring(0, 90) + "...";
                    }
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case MeetingMinutesNotification -> {
                EdsMeetingMinutes meeting = meetingManager.get(edsNotificationMessage.getEntityID());
                if (meeting != null) {
                    name = notificationWfmMessageSource.localize(EdsSubjects.MEETING_MINUTES) + ": " + meeting.getTitle();
                    if (name.length() > 90) {
                        name = name.substring(0, 90) + "...";
                    }
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case BenefitRequest -> {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(edsNotificationMessage.getEntityID());
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG") +
                        " " + benefitRequest.getBenefit().getName() + " " + notificationWfmMessageSource.localize(EdsSubjects.BENEFIT_REQUEST);
                if (name.length() > 90) {
                    name = name.substring(0, 90) + "...";
                }
            }
            case ProjectDueReminder -> {
                EdsProject project = projectManager.get(edsNotificationMessage.getEntityID());
                if (project != null) {
                    name = commonLocalizer.localize(EdsSubjects.PROJECT_OVERDUE_REMINDER) + ": " + "[" + project.getNumber() + "] " + project.getName();
                    if (name.length() > 90) {
                        name = name.substring(0, 90) + "...";
                    }
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case WorkstreamDueReminder -> {
                EdsWorkStream workStream = workStreamManager.get(edsNotificationMessage.getEntityID());
                if (workStream != null) {
                    name = commonLocalizer.localize(EdsSubjects.WORKSTREAM_OVERDUE_REMINDER) + ": " + "[" + workStream.getNumber() + "] " + workStream.getName();
                    if (name.length() > 90) {
                        name = name.substring(0, 90) + "...";
                    }
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case CRMCase -> {
                EdsCase edsCase = caseManager.get(edsNotificationMessage.getEntityID());
                if (edsCase != null) {
                    name = notificationWfmMessageSource.localizeWithParam(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG", edsCase.getCaseNumberString());
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case TimeSheetDueReminder -> {
                name = notificationWfmMessageSource.localizeWithParam(
                        edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                        ServerUtils.getDateAsString(edsNotificationMessage.getDate()));
            }
            case InvoiceDueReminder -> {
                EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(edsNotificationMessage.getEntityID());
                if (edsSaleInvoice != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsSaleInvoice.getNumber());
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case TaskAssignee -> {
                EdsTask edsTask = taskManager.get(edsNotificationMessage.getEntityID());
                if (edsTask != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsTask.getNumber());
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case LeadAssignee -> {
                EdsCrmContact edsCrmContact = crmContactManager.get(edsNotificationMessage.getEntityID());
                if (edsCrmContact != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsCrmContact.getNumber());
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case SaleInvoiceApproval -> {
                EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(edsNotificationMessage.getEntityID());
                if (edsSaleInvoice != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsSaleInvoice.getNumber());
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            case OnboardingStep -> {
                EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(edsNotificationMessage.getEntityID());
                if (edsStepEmployee != null && edsStepEmployee.getOnboardingStep() != null) {
                    name = notificationWfmMessageSource.localizeWithParam(
                            edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG",
                            edsStepEmployee.getOnboardingStep().getName() != null ? edsStepEmployee.getOnboardingStep().getName() : "");
                } else {
                    name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
                }
            }
            default -> {
                name = notificationWfmMessageSource.localize(edsNotificationMessage.getEntityType().name() + "_" + edsNotificationMessage.getActionOnEntity().name() + "_LONG");
            }
        }
        return name;
    }


    private void generatedActionUrl(EdsNotificationMessage edsNotificationMessage, NotificationItem item) {
        String url = "";
        switch (edsNotificationMessage.getEntityType()) {
            case LeaveRequests: {
                EdsSickRequest edsSickRequest = sickRequestManager.get(edsNotificationMessage.getEntityID());
                if (edsNotificationMessage.getActionOnEntity() == null || ActionOnEntityEnum.WAIT_APPROVAL.equals(edsNotificationMessage.getActionOnEntity())) {
                    if (edsSickRequest != null && edsSickRequest.getOverallStatus() != null && (EdsSickRequest.APPROVED.equals(edsSickRequest.getOverallStatus().getCode()) || EdsSickRequest.DENIED.equals(edsSickRequest.getOverallStatus().getCode()))) {
                        url = "/" + Constants.HRMS_URL + "#leaverequest/" + edsNotificationMessage.getEntityID();
                    } else {
                        url = "/" + Constants.HRMS_URL + "#leaverequest|/" + edsNotificationMessage.getEntityID() + "/" + edsNotificationMessage.getActorUserID();
                    }
                } else if (ActionOnEntityEnum.APPROVED.equals(edsNotificationMessage.getActionOnEntity())
                        || ActionOnEntityEnum.DENIED.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.HRMS_URL + "#leaverequest/" + edsNotificationMessage.getEntityID();
                }
                String leaveReason = commonLocalizer.localize(PdfLocalizationName.other);
                if (edsSickRequest != null && edsSickRequest.getLeaveReason() != null) {
                    try {
                        leaveReason = referenceWfmMessageSource.localize(edsSickRequest.getLeaveReason().getCode());
                    } catch (Exception e) {
                        leaveReason = edsSickRequest.getLeaveReason().getName();
                    }
                }
                item.setUniqueVal(leaveReason);
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_HR));
                break;
            }
            case TimeSheetApproval: {
                if (ActionOnEntityEnum.WAIT_APPROVAL.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.PM_URL + "#timesheetapproval|approve/" + edsNotificationMessage.getEntityID();
                } else if (ActionOnEntityEnum.APPROVED.equals(edsNotificationMessage.getActionOnEntity())
                        || ActionOnEntityEnum.REJECTED.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.PM_URL + "#myworkspace|timesheet";
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                break;
            }
            case ExpenseClaim: {
                EdsExpenseReport expense = expenseReportManager.get(edsNotificationMessage.getEntityID());
                if (ActionOnEntityEnum.WAIT_APPROVAL.equals(edsNotificationMessage.getActionOnEntity())) {
                    if (item.getModuleName() != null && !"".equals(item.getModuleName()) && NotificationMsgManager.MODULE_NAME_HR.equals(item.getModuleName())) {
                        url = "/" + Constants.HRMS_URL + "#expenseReports|previewReport/" + edsNotificationMessage.getEntityID() + "/EXPENSE_VIEW";
                    } else {
                        url = "/" + Constants.ACCOUNTING_URL + "#expenseReports|previewReport/" + edsNotificationMessage.getEntityID() + "/EXPENSE_VIEW/ACCOUNTING";
                    }
                } else if (ActionOnEntityEnum.APPROVED.equals(edsNotificationMessage.getActionOnEntity())
                        || ActionOnEntityEnum.DECLINE.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.ACCOUNTING_URL + "#expenseReports|previewReport/" + edsNotificationMessage.getEntityID() + "/EXPENSE_VIEW/ACCOUNTING";
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + expense.getNumber());
                break;
            }
            case GoogleCalendarEvent: {
                if (ActionOnEntityEnum.EVENT.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.CRM_URL + "#event|summary/" + edsNotificationMessage.getEntityID();
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_CRM));
                break;
            }

            case WhatsApp:{
                url =  "/" + Constants.CRM_URL + "#event|summary/" + edsNotificationMessage.getEntityID();

                break;
            }
            case TaskDueReminder: {
                EdsTask task = taskManager.get(edsNotificationMessage.getEntityID());
                if (ActionOnEntityEnum.TASK_REMINDER.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.PM_URL + "#task|summary/" + edsNotificationMessage.getEntityID() + "/" + false;
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                item.setUniqueVal("#" + task.getNumber());
                break;
            }
            case MeetingMinutesNotification: {
                EdsMeetingMinutes meetingMinutes = meetingManager.get(edsNotificationMessage.getEntityID());
                if (ActionOnEntityEnum.MEETING_MINUTES.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.HRMS_URL + "#meetingMinutes|summary/" + edsNotificationMessage.getEntityID();
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_WORKSPACE));
                item.setUniqueVal(meetingMinutes.getMeetingNumber() != null ? "#" + meetingMinutes.getMeetingNumber() : null);
                break;
            }
            case BenefitRequest: {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(edsNotificationMessage.getEntityID());
                if (NotificationTypeEnum.BenefitRequest.equals(edsNotificationMessage.getEntityType())) {
                    url = "/" + Constants.HRMS_URL + "#benefitRequest|add/" + edsNotificationMessage.getEntityID();
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_HR));
                item.setUniqueVal("#" + benefitRequest.getBenefit().getCode());
                break;
            }
            case ProjectDueReminder: {
                EdsProject project = projectManager.get(edsNotificationMessage.getEntityID());
                if (ActionOnEntityEnum.PROJECT_REMINDER.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.PM_URL + "#project|summary/" + edsNotificationMessage.getEntityID() + "/" + null + "/" + false;
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                item.setUniqueVal("#" + project.getNumber());
                break;
            }
            case WorkstreamDueReminder: {
                if (ActionOnEntityEnum.WORKSTREAM_REMINDER.equals(edsNotificationMessage.getActionOnEntity())) {
                    url = "/" + Constants.PM_URL + "#workstream|summary/" + edsNotificationMessage.getEntityID();
                }
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                break;
            }
            case CRMCase: {
                EdsCase edsCase = caseManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.CRM_URL + "#case|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_CRM));
                item.setUniqueVal(edsCase.getCaseNumberString());
                break;
            }
            case TimeSheetDueReminder: {
                url = "/" + Constants.PM_URL + "#" + Constants.MYWORKSPACE + "|" + Constants.TIMESHEET;
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                break;
            }
            case InvoiceDueReminder: {
                EdsInvoice invoice = invoiceManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.ACCOUNTING_URL + "#" + Constants.SALE_INVOICE + "|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + invoice.getNumber());
                break;
            }
            case TaskAssignee: {
                EdsTask task = taskManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.PM_URL + "#task|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                item.setUniqueVal("#" + task.getNumber());
                break;
            }
            case LeadAssignee: {
                url = "/" + Constants.CRM_URL + "#lead|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_CRM));
                break;
            }
            case CRMContact: {
                url = "/" + Constants.CRM_URL + "#contact|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_CRM));
                break;
            }
            case Employee: {
                EdsEmployee edsEmployee = employeeManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.HRMS_URL + "#employeeProfile|" + Constants.EMPLOYEE_PROFILE_VIEW + "/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_CRM));
                item.setUniqueVal("#" + edsEmployee.getProfile().getEmployeeCode());
                break;
            }
            case SaleInvoiceApproval: {
                EdsInvoice invoice = invoiceManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.ACCOUNTING_URL + "#" + Constants.SALE_INVOICE + "|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + invoice.getNumber());
                break;
            }
            case ProjectApproval: {
                EdsProject project = projectManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.PM_URL + "#project|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PM));
                item.setUniqueVal("#" + project.getNumber());
                break;
            }
            case PurchaseOrder: {
                EdsQuote quote = quoteManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.ACCOUNTING_URL + "#purchaseorder|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + quote.getNumber());
                break;
            }
            case CrmOpportunity: {
                url = "/" + Constants.CRM_URL + "#opportunity|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                break;
            }
            case CashAdvance: {
                EdsCashAdvance cashAdvance = cashAdvanceManager.get(edsNotificationMessage.getEntityID());
                if (cashAdvance != null) {
                    url = "/" + Constants.PAYROLL_URL + "#cashAdvance%7Csummary/" + cashAdvance.getObjectID() + "/" + (cashAdvance.getStatus() != null ? cashAdvance.getStatus().getName() : "");
                    item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_PAYROLL));
                    break;
                }
            }
            case OnboardingStep: {
                EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(edsNotificationMessage.getEntityID());
                if (edsStepEmployee != null) {
                    url = "/" + Constants.HRMS_URL + "#employeeStep|add/add/" + edsStepEmployee.getObjectID() + "/"
                            + (edsStepEmployee.getOnboardingStep() != null ? edsStepEmployee.getOnboardingStep().getObjectID() : "") + "/"
                            + (edsStepEmployee.getOnboardingStep() != null ? edsStepEmployee.getOnboardingStep().getFormID() : "") + "/"
                            + (edsStepEmployee.getOnboardingStep() != null ? edsStepEmployee.getOnboardingStep().getName() : "");

                    item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_HR));
                    break;
                }
            }
            case SalesQuote: {
                EdsQuote quote = quoteManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.ACCOUNTING_URL + "#" + Constants.SALE_QUOTE + "|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + quote.getNumber());
                break;
            }
            case SalesOrder: {
                EdsQuote quote = quoteManager.get(edsNotificationMessage.getEntityID());
                url = "/" + Constants.ACCOUNTING_URL + "#" + Constants.SALE_ORDER_CODE + "|summary/" + edsNotificationMessage.getEntityID();
                item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_ACCOUNTING));
                item.setUniqueVal("#" + quote.getNumber());
                break;
            }
            case CustomFormItem: {
                EdsCustomFormItems edsCustomFormItems = customFormItemManager.get(edsNotificationMessage.getEntityID());
                ModuleEnum moduleEnum = ModuleEnum.getModule(edsCustomFormItems.getCustomForm().getProperty().getModuleCode());
                if (moduleEnum != null) {
                    url = "/" + moduleEnum.getUrl() + "#" +Constants.ITEM_LIST + "|summary/" + edsCustomFormItems.getObjectID() + "/" + edsCustomFormItems.getCustomForm().getObjectID() + "/" + edsCustomFormItems.getCustomForm().getFormID() + "/" + edsCustomFormItems.getCustomForm().getName();
                }
                break;
            }
            case EmployeeDocuments: {
                EdsFileHeader file = fileHeaderManager.get(edsNotificationMessage.getEntityID());
                if (file != null) {
                    url = "/" + Constants.HRMS_URL + "#employeeDocument" + "/" + edsNotificationMessage.getEntityID();
                    item.setModuleName(notificationWfmMessageSource.localize(NotificationMsgManager.MODULE_NAME_HR));
                    item.setUniqueVal("#" + file.getOwner());
                }
                break;
            }
        }
        item.setActionUrl(url);
    }

    public String getDate(EdsEvent event, EdsUser owner) {
        EdsCompany company = owner.getCompany();
        TimeZone timeZone = owner.getUserTimezone();
        Date start = (Date) event.getStartDate().clone();
        Date end = (Date) event.getEndDate().clone();
        Date startDate = new Date(start.getYear(), start.getMonth(), start.getDate(), start.getHours(), start.getMinutes() + (timeZone.getRawOffset() / 60000), start.getSeconds());
        Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate(), end.getHours(), end.getMinutes() + (timeZone.getRawOffset() / 60000), end.getSeconds() + 1);
        final EdsCompanySettings edsCompanySettings = company.getCompanySettings();
        SimpleDateFormat longDateFormat = new SimpleDateFormat(edsCompanySettings != null
                ? edsCompanySettings.getLongDateFormat()
                : "MMM dd, yyyy [HH:mm]");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(edsCompanySettings != null
                ? edsCompanySettings.getShortDateFormat()
                : "MMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String startTime = timeFormat.format(startDate).toLowerCase();
        String endTime = timeFormat.format(endDate).toLowerCase();
        String dateString = "";
        if (event.isMultiDayAppointment()) {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate) + " - " + shortDateFormat.format(endDate);
            } else {
                dateString = longDateFormat.format(startDate) + " - " + longDateFormat.format(endDate);
            }
        } else {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate);
            } else {
                dateString = shortDateFormat.format(startDate) + "," + startTime + " - " + endTime;
            }
        }

        return dateString + " (" + owner.getUserTimezone().getID() + ")";
    }

    @Override
    public Long getNotificationsCount() {
        return notificationMsgManager.getNotificationsCount();
    }

    public Long getUnreadEmailsCount() {
        HashMap<EmailAccountItem, HashSet<EmailFolder>> folderMaps = messageCenterService.getUserFetchableEmailFolders();
        if (folderMaps == null) {
            return null;
        }
        Long total = 0L;
        for (EmailAccountItem emailSettings : folderMaps.keySet()) {
            total += emailSettings.getUnreadCount() != null ? emailSettings.getUnreadCount() : 0;
        }
        return total;
    }

}
