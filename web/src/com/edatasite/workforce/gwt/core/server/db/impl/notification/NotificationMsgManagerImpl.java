package com.edatasite.workforce.gwt.core.server.db.impl.notification;

import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.fcm.FirebasePushNotificationService;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgSettingManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.workforcetrack.mobile.services.ApnsSenderWebService;
import com.workforcetrack.mobile.services.GcmSenderWebService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by dilsh0d on 09.07.15.
 */
@Repository("notificationMsgManager")
public class NotificationMsgManagerImpl extends BaseManager<EdsNotificationMessage> implements NotificationMsgManager {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Dedup window: if a non-deleted notification with the same (entity, type, action, viewer) already exists
     * within this window, creation is skipped. A single user action can raise several business events
     * (e.g. expense claim add + send-to-approver + approve) which would otherwise each create their own
     * notification row and push, producing duplicate alerts.
     */
    private static final long NOTIFICATION_DEDUP_WINDOW_MS = 2 * 60 * 1000L;

    /**
     * In-memory TTL set of recently created notification keys, so dedup needs no database query.
     * Thread-safe; entries auto-expire after {@link #NOTIFICATION_DEDUP_WINDOW_MS}.
     */
    private static final Cache<String, Boolean> RECENT_NOTIFICATIONS = CacheBuilder.newBuilder()
            .expireAfterWrite(NOTIFICATION_DEDUP_WINDOW_MS, TimeUnit.MILLISECONDS)
            .maximumSize(100_000)
            .build();

    @Autowired
    FirebasePushNotificationService firebasePushNotificationService;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private GcmSenderWebService gcmSenderWebService;
    @Autowired
    private ApnsSenderWebService apnsSenderWebService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private NotificationMsgSettingManager notificationMsgSettingManager;
    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    @Qualifier("notificationLocalizer")
    private WfmMessageSource notificationWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private RabbitMQService rabbitMQService;

    public NotificationMsgManagerImpl() {
        super(EdsNotificationMessage.class);
    }

    @Override
    public Integer getNewNotificationsIds(ArrayList<NotificationTypeEnum> entityTypes) {
        EdsUser edsUser = getUser();
        if (edsUser == null) {
            return 0;
        }
        StringBuilder sql = new StringBuilder("select count(n.id) from ").append(getCompanyId()).append(".notification_msg n ");
        sql.append(" where (n.read is null OR n.read=false) and (n.deleted is null OR n.deleted=false) and n.viewer_userid=").append(edsUser.getObjectID());
        if (entityTypes != null && entityTypes.size() > 0) {
            sql.append(" and n.entity_type in ('").append(ServerUtils.getAsCommoDelimited(entityTypes, "0", "','")).append("')");
        }
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsNotificationMessage> getNewLatestTen() {
        String hqlQuery = "SELECT n FROM EdsNotificationMessage n WHERE (n.read is null OR n.read=false) AND (n.deleted is null OR n.deleted=false) AND n.viewerUserID=? ORDER BY id DESC";
        return findLimited(hqlQuery, 10, getUser().getObjectID());
    }

    public Long getNotificationsCount() {
        String hqlQuery = "SELECT count(n.objectID) FROM EdsNotificationMessage n WHERE (n.read is null OR n.read=false) AND (n.deleted is null OR n.deleted=false) AND n.viewerUserID = ?";
        return (Long) findSingle(hqlQuery, getUser().getObjectID());
    }

    public void clearAllUnreadNotifications() {
        update("update EdsNotificationMessage n set n.read = true WHERE ( n.read is null OR n.read=false) AND (n.deleted is null OR n.deleted=false) AND n.viewerUserID = ?", getUser().getObjectID());
    }

    @Override
    public List<EdsNotificationMessage> getList(ListingFilterParameter fp, ArrayList<NotificationTypeEnum> entityTypes) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hqlQuery = new StringBuilder("SELECT n FROM EdsNotificationMessage n, EdsEmployee emp WHERE n.actorUserID = emp.objectID ");
        hqlQuery.append(" AND n.viewerUserID=:viewerUserID AND (n.deleted is null OR n.deleted=false) AND (emp.deleted is null OR emp.deleted = false) ");
        if (Constants.VIEW_READ.equals(fp.getViewType())) {
            hqlQuery.append(" AND n.read=:read ");
            params.put("read", fp.isActive());
        } else if (fp.isActive() || (fp.isPresentActive() != null && fp.isPresentActive())) {
            hqlQuery.append(" AND n.read is not true ");
        }
        if (fp.getCategoryID() != null && fp.getCategoryID() != 0) {
            hqlQuery.append(" AND n.entityType=:entityType ");
            params.put("entityType", NotificationTypeEnum.getByIdType(fp.getCategoryID()));
        } else if (entityTypes != null && entityTypes.size() > 0) {
            hqlQuery.append(" AND n.entityType IN ( '" + ServerUtils.getAsCommoDelimited(entityTypes, "0", "','") + "')");
            //params.put("entityTypes", ServerUtils.getAsCommoDelimited(entityTypes, "0", "','"));
        }
        if (fp.getAvoidType() != null) {
            hqlQuery.append(" AND n.actionOnEntity != 'APPROVED' ");
        }
        params.put("viewerUserID", getUser().getObjectID());

        if (fp.getSqlSearchKey() != null) {
            hqlQuery.append(" and ( ");
            hqlQuery.append(" lower(n.subject) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(n.entityType) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.firstName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.lastName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.userName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(") ");
        }
        if (fp.isFromMobile() && fp.getStart() > 0) {
            params.put("lastLoadId", fp.getStart());
            hqlQuery.append(" AND n.id <:lastLoadId ");
            fp.setStart(0);
        }
        if (fp.getSortField() == null) {
            hqlQuery.append(" ORDER BY n.read ASC, n.date DESC ");
        } else {
            String sortDir = fp.getSortDir() != null && fp.getSortDir() == 2 ? "desc" : "asc";
            if ("fullDescription".equals(fp.getSortField())) {
                hqlQuery.append(" ORDER BY n.subject " + sortDir + ", n.date DESC ");
            } else if ("type".equals(fp.getSortField())) {
                hqlQuery.append(" ORDER BY n.entityType " + sortDir + ", n.date DESC ");
            }
        }

        List<EdsNotificationMessage> result = findIntervalByNamedParams(hqlQuery.toString(), fp.getStart(), (fp.getLimit() > 0 ? fp.getLimit() : 20), params);

        if (fp.isFromMobile() && result.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (EdsNotificationMessage notificationMessage : result) {
                sb.append(notificationMessage.getObjectID()).append(",");
            }
            updateNotifications(null, sb.substring(0, sb.toString().length() - 1));
        }
        return result;
    }

    @Override
    public Long getListTotal(ListingFilterParameter fp, ArrayList<NotificationTypeEnum> entityTypes) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hqlQuery = new StringBuilder("SELECT count(n.id) FROM EdsNotificationMessage n, EdsEmployee emp WHERE n.actorUserID = emp.objectID ");
        hqlQuery.append(" AND n.viewerUserID=:viewerUserID AND (n.deleted is null OR n.deleted=false) AND (emp.deleted is null OR emp.deleted = false) ");
        if (Constants.VIEW_READ.equals(fp.getViewType())) {
            hqlQuery.append(" AND n.read=:read ");
            params.put("read", fp.isActive());
        } else if (fp.isActive() || (fp.isPresentActive() != null && fp.isPresentActive())) {
            hqlQuery.append(" AND n.read is not true ");
        }
        if (fp.getCategoryID() != null && fp.getCategoryID() != 0) {
            hqlQuery.append(" AND n.entityType=:entityType ");
            params.put("entityType", NotificationTypeEnum.getByIdType(fp.getCategoryID()));
        } else if (entityTypes != null && entityTypes.size() > 0) {
            hqlQuery.append(" AND n.entityType IN ( '" + ServerUtils.getAsCommoDelimited(entityTypes, "0", "','") + "')");
            //params.put("entityTypes", ServerUtils.getAsCommoDelimited(entityTypes, "0", "','"));
        }
        params.put("viewerUserID", getUser().getObjectID());

        if (fp.getSqlSearchKey() != null) {
            hqlQuery.append(" and ( ");
            hqlQuery.append(" lower(n.subject) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(n.entityType) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.firstName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.lastName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(" or lower(emp.userName) like '" + fp.getSqlSearchKey() + "' ");
            hqlQuery.append(") ");
        }

        return (Long) findSingleByNamedParams(hqlQuery.toString(), params);
    }

    @Override
    public void deleteNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum, ActionOnEntityEnum actionOnEntityEnum) {
        StringBuilder sql = new StringBuilder();
        sql
                .append("UPDATE ").append(getCompanyId()).append(".notification_msg SET deleted=TRUE,clicked=TRUE,read=TRUE WHERE entity_id=").append(entityID)
                .append(" AND entity_type='").append(notificationTypeEnum.name()).append("' ");
        if (actionOnEntityEnum != null) {
            sql.append(" AND action_on_entity='").append(actionOnEntityEnum.name()).append("'");
        }
        updateNative(sql.toString());
    }


    public void deleteNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum) {
        deleteNotificationEvent(entityID, notificationTypeEnum, null);
    }

    @Override
    public void updateClickedNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum, ActionOnEntityEnum actionOnEntityEnum) {
        updateNative("UPDATE " + getCompanyId() + ".notification_msg SET read=TRUE,clicked=TRUE WHERE entity_id=" + entityID
                + " AND entity_type='" + notificationTypeEnum.name() + "' AND action_on_entity='" + actionOnEntityEnum.name() + "'");
    }

    @Override
    public boolean isHasNotification(Integer entityID, NotificationTypeEnum type, ActionOnEntityEnum actionOnEntity) {
        List<EdsNotificationMessage> notificationMessageList = find("SELECT n.id FROM EdsNotificationMessage n " +
                "WHERE (n.deleted is null OR n.deleted=false) AND n.entityID=? AND n.entityType=? AND n.actionOnEntity=?", entityID, type, actionOnEntity);
        return notificationMessageList.size() != 0;
    }

    @Override
    public void createLeaveRequestNotification(ActionOnEntityEnum actionOnEntityEnum, EdsSickRequest edsSickRequest) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.LeaveRequests)) {
            Integer viewerUserID, actorUserID;
            EdsUser approver = isOk(edsSickRequest.getCurrentApprover()) ? edsSickRequest.getCurrentApprover().getExactEmployee() : null;
            actorUserID = approver != null ? approver.getObjectID() : null;
            viewerUserID = edsSickRequest.getEmployee().getObjectID();
            if (ActionOnEntityEnum.WAIT_APPROVAL.equals(actionOnEntityEnum)) {
                actorUserID = edsSickRequest.getEmployee().getObjectID();
                viewerUserID = approver.getObjectID();
            }
            createNotification(edsSickRequest.getObjectID(), NotificationTypeEnum.LeaveRequests, actionOnEntityEnum, viewerUserID, actorUserID, null);
            //Send Push Notifications for CoockieDev (Stepan)
            //When push notification is enabled, sent notification to mobile apps.

            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.LeaveRequests.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.LeaveRequests.name() + "_" + actionOnEntityEnum.name() + "_LONG");

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);
        }

    }

    @Override
    public void createEventNotification(ActionOnEntityEnum actionOnEntityEnum, EdsEmployeeEvent edsEmployeeEvent) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.GoogleCalendarEvent)) {
            Integer viewerUserID;
            viewerUserID = edsEmployeeEvent.getEmployee().getObjectID();
            createNotification(edsEmployeeEvent.getEvent().getObjectID(), NotificationTypeEnum.GoogleCalendarEvent, actionOnEntityEnum, viewerUserID, viewerUserID, null);
            //Send Push Notifications for CoockieDev (Stepan)
            //When push notification is enabled, sent notification to mobile apps.

            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.GoogleCalendarEvent.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.GoogleCalendarEvent.name() + "_" + actionOnEntityEnum.name() + "_LONG");

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.CRM_MODULE);
        }

    }

    @Override
    public void createFixedAssetUpdateDeprecationNotification(EdsUser creator) {
        if (creator != null && creator.getObjectID() != null)
            createNotification(0, NotificationTypeEnum.FixedAssets, ActionOnEntityEnum.COMPLATE, creator.getObjectID(), creator.getObjectID(), "Fixed assets update deprecation has been finished!");
    }

    private void sendPushNotificationForCoockieDev(String msgTitle, String msgBody, Integer viewerUserID, String modulecode) {
        //Send Push Notification to Firebase
        try {
            EdsUser vierwerUser = userManager.get(viewerUserID);
            if (vierwerUser != null) {
                String username = globalAuthJdbcSpringManager.getUsername(vierwerUser.getCompany().getObjectID(), vierwerUser.getObjectID());

                if (StringUtils.isNotBlank(username)) {
                    List<String[]> firebaseTokens = globalAuthJdbcSpringManager.getUserPushNotificationTokens(username, EdsContextParams.getHostname(), modulecode);
                    if (firebaseTokens == null || firebaseTokens.size() == 0) {
                        log.info("No Push Notification Tokens found!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                    for (String[] token : firebaseTokens) {
                        if (token.length == 2 && StringUtils.isNotBlank(token[0]) && StringUtils.isNotBlank(token[1])) {
                            if ("ANDROID".equalsIgnoreCase(token[1])) {
                                log.info("Sending Push Notification to ANDROID " + token[0]);
                                if (firebasePushNotificationService.pushDataNotification(token[0], msgTitle, msgBody)) {
                                    log.info("Firebase msg sent to: " + token[0]);
                                } else {
                                    log.info("Couldnt sent Firebase msg to: " + token[0]);
                                }
                            } else if ("IOS".equalsIgnoreCase(token[1])) {
                                log.info("Sending Push Notification to IOS " + token[0]);
                                apnsSenderWebService.sendMessageForCookieDev(token[0], msgTitle, msgBody);
//                                firebasePushNotificationService.pushDataNotification(token[0], title, msgBody);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void createCashAdvanceNotification(ActionOnEntityEnum actionOnEntityEnum, EdsCashAdvance cashAdvance) {

        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.CashAdvance)) {
            Integer viewerUserID, actorUserID;
            EdsUser approver = isOk(cashAdvance.getCurrentApprover()) ? cashAdvance.getCurrentApprover().getExactEmployee() : null;
            actorUserID = approver != null ? approver.getObjectID() : null;
            viewerUserID = cashAdvance.getEmployee().getObjectID();
            if (ActionOnEntityEnum.WAIT_APPROVAL.equals(actionOnEntityEnum)) {
                actorUserID = cashAdvance.getEmployee().getObjectID();
                viewerUserID = approver.getObjectID();
            }
            createNotification(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance, actionOnEntityEnum, viewerUserID, actorUserID, null);

            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.CashAdvance.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.CashAdvance.name() + "_" + actionOnEntityEnum.name());

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);

//        sendPushNotificationForCoockieDev(actionOnEntityEnum, viewerUserID);
        }
    }

    @Override
    public void createStepEmployeeNotification(ActionOnEntityEnum actionOnEntityEnum, EdsStepEmployee edsStepEmployee) {
        Integer viewerUserID = null, actorUserID = null;
        EdsUser approver = isOk(edsStepEmployee.getCurrentApprover()) ? edsStepEmployee.getCurrentApprover().getExactEmployee() : null;
        actorUserID = approver != null ? approver.getObjectID() : null;
        viewerUserID = edsStepEmployee.getEmployee().getObjectID();
        if (ActionOnEntityEnum.WAIT_APPROVAL.equals(actionOnEntityEnum)) {
            actorUserID = edsStepEmployee.getEmployee().getObjectID();
            viewerUserID = approver.getObjectID();
        }
        createNotification(edsStepEmployee.getObjectID(), NotificationTypeEnum.OnboardingStep, actionOnEntityEnum, viewerUserID, actorUserID, null);
    }

    @Override
    public void createTimeSheetApprovalNotification(ActionOnEntityEnum actionOnEntityEnum, EdsTimeSheetApprovalSession timesheetSession, Integer userID) {
        Integer viewerUserID = null, actorUserID = null;
        if (ActionOnEntityEnum.WAIT_APPROVAL.equals(actionOnEntityEnum)) {
            actorUserID = timesheetSession.getEmployee().getObjectID();
            viewerUserID = userID;
        } else {
            actorUserID = userID;
            viewerUserID = timesheetSession.getEmployee().getObjectID();
        }
        createNotification(timesheetSession.getObjectID(), NotificationTypeEnum.TimeSheetApproval, actionOnEntityEnum, viewerUserID, actorUserID, null);
    }

    @Override
    public void createExpenseClaimNotification(ActionOnEntityEnum actionOnEntityEnum, EdsExpenseReport expense) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.ExpenseClaim)) {
            Integer viewerUserID, actorUserID;
            EdsUser approver = isOk(expense.getCurrentApprover()) ? expense.getCurrentApprover().getExactEmployee() : null;
            actorUserID = approver != null ? approver.getObjectID() : null;
            viewerUserID = expense.getReporter().getObjectID();
            if (ActionOnEntityEnum.WAIT_APPROVAL.equals(actionOnEntityEnum)) {
                actorUserID = expense.getReporter().getObjectID();
                viewerUserID = approver.getObjectID();
            }
            createNotification(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, actionOnEntityEnum, viewerUserID, actorUserID, null);

            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.ExpenseClaim.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.ExpenseClaim.name() + "_" + actionOnEntityEnum.name());

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);
//        sendPushNotificationForCoockieDev(actionOnEntityEnum, viewerUserID);
        }
    }

    @Override
    public void createTaskOverDueDateReminderNotification(EdsTask task, EdsEmployee employee) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.TaskDueReminder)) {
            Integer viewerUserID, actorUserID;
            actorUserID = task.getCreator().getObjectID();
            viewerUserID = employee.getObjectID();
            createNotification(task.getObjectID(), NotificationTypeEnum.TaskDueReminder, ActionOnEntityEnum.TASK_REMINDER, viewerUserID, actorUserID, null);

            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.TaskDueReminder.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.TaskDueReminder.name() + "_" + ActionOnEntityEnum.TASK_REMINDER.name());
            if (StringUtils.isNotBlank(task.getNumber())) {
                msgBody = commonLocalizer.localize(EdsSubjects.TASK_DUE_REMINDER) + ": " + "[" + task.getNumber() + "] " + task.getName();
            }
            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);
//        sendPushNotificationForCoockieDev(ActionOnEntityEnum.TASK_REMINDER, viewerUserID);
        }
    }

    @Override
    public void createMeetingMinutesNotification(EdsMeetingMinutes meetingMinutes, EdsUser creator, EdsUser attendeesEmployee) {
        Integer viewerUserID = null, actorUserID = null;
        actorUserID = creator.getObjectID();
        viewerUserID = attendeesEmployee.getObjectID();
        createNotification(meetingMinutes.getObjectID(), NotificationTypeEnum.MeetingMinutesNotification, ActionOnEntityEnum.MEETING_MINUTES, viewerUserID, actorUserID, null);
    }

    @Override
    public void createBenefitRequestNotification(ActionOnEntityEnum actionOnEntityEnum, Integer requestID) {

        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.BenefitRequest)) {
            Integer viewerUserID, actorUserID;
            String requestName = "";
            if (actionOnEntityEnum.equals(ActionOnEntityEnum.WAIT_APPROVAL)) {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(requestID);
                actorUserID = benefitRequest.getRequester().getObjectID();
                viewerUserID = benefitRequest.getApprover().getObjectID();
                if (benefitRequest.getBenefit() != null) {
                    requestName = benefitRequest.getBenefit().getName();
                }
            } else if (actionOnEntityEnum.equals(ActionOnEntityEnum.APPROVED)) {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(requestID);
                actorUserID = benefitRequest.getApprover().getObjectID();
                viewerUserID = benefitRequest.getRequester().getObjectID();
                if (benefitRequest.getBenefit() != null) {
                    requestName = benefitRequest.getBenefit().getName();
                }
            } else {
                EdsBenefitRequest benefitRequest = benefitRequestManager.get(requestID);
                actorUserID = benefitRequest.getApprover().getObjectID();
                viewerUserID = benefitRequest.getRequester().getObjectID();
                if (benefitRequest.getBenefit() != null) {
                    requestName = benefitRequest.getBenefit().getName();
                }
            }
            createNotification(requestID, NotificationTypeEnum.BenefitRequest, actionOnEntityEnum, viewerUserID, actorUserID, null);
            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.BenefitRequest.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.BenefitRequest.name() + "_" + actionOnEntityEnum.name() + "_LONG") +
                    " " + requestName + " " + notificationWfmMessageSource.localize(EdsSubjects.BENEFIT_REQUEST);
        /*if (msgTitle.length() > 90) {
            msgTitle = msgTitle.substring(0, 90) + "...";
        }*/

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);
//        sendPushNotificationForCoockieDev(actionOnEntityEnum, viewerUserID);
        }
    }

    @Override
    public void createProjectOverDueDateReminderNotification(EdsProject project, EdsEmployee employee) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.ProjectDueReminder)) {
            Integer viewerUserID = null, actorUserID = null;
            actorUserID = project.getCreator().getObjectID();
            viewerUserID = employee.getObjectID();
            createNotification(project.getObjectID(), NotificationTypeEnum.ProjectDueReminder, ActionOnEntityEnum.PROJECT_REMINDER, viewerUserID, actorUserID, null);
            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.ProjectDueReminder.name());
            String msgBody = notificationWfmMessageSource.localize(NotificationTypeEnum.ProjectDueReminder.name() + "_" + ActionOnEntityEnum.PROJECT_REMINDER.name());

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, PermissionConstants.HRMS_MODULE);
//        sendPushNotificationForCoockieDev(ActionOnEntityEnum.PROJECT_REMINDER, viewerUserID);
        }
    }

    @Override
    public void createWorkstreamOverDueDateReminderNotification(EdsWorkStream workstream, EdsEmployee employee) {
        Integer viewerUserID = null, actorUserID = null;
        actorUserID = workstream.getCreator().getObjectID();
        viewerUserID = employee.getObjectID();
        createNotification(workstream.getObjectID(), NotificationTypeEnum.WorkstreamDueReminder, ActionOnEntityEnum.WORKSTREAM_REMINDER, viewerUserID, actorUserID, null);
    }

    @Override
    public void createTimeSheetOverDueReminderNotificationEvent(EdsUser user, ActionOnEntityEnum actionOnEntityEnum) {
        Integer viewerUserID = user.getObjectID(), actorUserID = user.getObjectID();
        createNotification(user.getObjectID(), NotificationTypeEnum.TimeSheetDueReminder, actionOnEntityEnum, viewerUserID, actorUserID, null);
    }

    @Override
    public void createInvoiceOverDueNotificationEvent(Integer invoiceId, EdsUser user, ActionOnEntityEnum actionOnEntityEnum) {
        Integer viewerUserID = user.getObjectID(), actorUserID = user.getObjectID();
        createNotification(invoiceId, NotificationTypeEnum.InvoiceDueReminder, actionOnEntityEnum, viewerUserID, actorUserID, null);
    }

    @Override
    public void createTaskAssigneeNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, ActionOnEntityEnum actionOnEntityEnum) {
        if (notificationMsgSettingManager.isEnableNotification(NotificationTypeEnum.TaskAssignee)) {
            createNotification(entityID, NotificationTypeEnum.TaskAssignee, actionOnEntityEnum, viewerID, actorID, null);
            //Send Push Notifications for CoockieDev (Stepan)
            String msgTitle = notificationWfmMessageSource.localize(NotificationTypeEnum.TaskAssignee.name());
            EdsTask edsTask = taskManager.get(entityID);
            String msgBody = notificationWfmMessageSource.localizeWithParam(
                    NotificationTypeEnum.TaskAssignee.name() + "_" + actionOnEntityEnum.name(),
                    edsTask != null ? edsTask.getNumber() : "");
            //notificationWfmMessageSource.localize(NotificationTypeEnum.TaskAssignee.name() + "_" + actionOnEntityEnum.name());

            sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerID, PermissionConstants.HRMS_MODULE);
            //Send WEB Push Notifications
            /*try {
                WebSocketServerObject pushMessage = new WebSocketServerObject();
                pushMessage.setEventType(WfmUiEventType.ON_TASK_ADD);
                pushMessage.setUserId(edsUser.getObjectID());
                String subject = commonLocalizer.localize(EdsSubjects.TASK_ASSIGN_NOTIFICATION) + " [" + task.getNumber() + "] " + task.getName();
                pushMessage.setData(subject);
                webPushNotificationsMQComponent.sendWebPushNotification(pushMessage, SecurityContext.getCompanyID(), SecurityContext.getInstance().getDatabase());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
//        sendPushNotificationForCoockieDev(actionOnEntityEnum, viewerID);
    }

    @Override
    public void createSaleInvoiceApprovallNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, ActionOnEntityEnum actionOnEntityEnum) {
        createNotification(entityID, NotificationTypeEnum.SaleInvoiceApproval, actionOnEntityEnum, viewerID, actorID, null);
    }

    @Override
    public void createWorkflowNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, NotificationTypeEnum entityType, String subject, ActionOnEntityEnum actionOnEntity) {

        if (NotificationTypeEnum.LeadAssignee.equals(entityType) || NotificationTypeEnum.CrmOpportunity.equals(entityType)/*RelationItem.TYPE_OPPORTUNITY.equals(entityType) || RelationItem.TYPE_LEAD.equals(entityType)*/) {
            //Send push notifications for leads and opportunities
            createNotification(entityID, entityType, actionOnEntity, viewerID, actorID, subject, PermissionConstants.CRM_MODULE);
        } else {
            createNotification(entityID, entityType, actionOnEntity, viewerID, actorID, subject);
        }
    }

    @Override
    public void createWorkflowNotificationEventToUsers(Integer entityID, Integer actorID, List<Integer> viewerIDs, NotificationTypeEnum entityType, String subject) {
        if (viewerIDs != null) {
            for (Integer viewerID : viewerIDs) {
                if (viewerID != null) {
                    createNotification(entityID, entityType, null, viewerID, actorID, subject);
                }
            }
        }
    }

    @Override
    public void createSalesQuoteNotification(EdsSaleQuote quote, EdsUser creator) {
        Integer viewerUserID, actorUserID;
        EdsUser approver = isOk(quote.getCurrentApprover()) ? quote.getCurrentApprover().getExactEmployee() : null;
        viewerUserID = approver != null ? approver.getObjectID() : null;
        actorUserID = creator.getObjectID();
        createNotification(quote.getObjectID(), NotificationTypeEnum.SalesQuote, ActionOnEntityEnum.WAIT_APPROVAL, viewerUserID, actorUserID, null);
    }

    @Override
    public void createWhatsappMessageNotification(EdsWhatsAppMessage message, EdsUser creator) {
        createNotification(message.getObjectID(), NotificationTypeEnum.WhatsApp, ActionOnEntityEnum.EVENT, creator.getObjectID(), creator.getObjectID(), message.getText());
    }

    @Override
    public void createSalesOrderNotification(EdsSaleQuote quote, EdsUser creator) {
        Integer viewerUserID, actorUserID;
        EdsUser approver = isOk(quote.getCurrentApprover()) ? quote.getCurrentApprover().getExactEmployee() : null;
        viewerUserID = approver != null ? approver.getObjectID() : null;
        actorUserID = creator.getObjectID();
        createNotification(quote.getObjectID(), NotificationTypeEnum.SalesOrder, ActionOnEntityEnum.WAIT_APPROVAL, viewerUserID, actorUserID, null);
    }

    @Override
    public void createProjectApprovalNotificationEvent(Integer entityID, Integer viewerID, Integer actorID, ActionOnEntityEnum actionOnEntityEnum) {
        createNotification(entityID, NotificationTypeEnum.ProjectApproval, actionOnEntityEnum, viewerID, actorID, null);
    }

    @Override
    public void clearUserUnreadMsg(ArrayList<NotificationTypeEnum> entityList) {
        StringBuilder sql = new StringBuilder("update ").append(getCompanyId()).append(".notification_msg set read = true, clicked = true ");
        sql.append("where (deleted is null or deleted is not true) and (clicked = null or clicked = FALSE OR read = null or read = FALSE) and viewer_userid = ").append(getUser().getObjectID());
        if (entityList != null && entityList.size() > 0) {
            sql.append(" and entity_type in ('").append(ServerUtils.getAsCommoDelimited(entityList, "0", "','")).append("')");
        }
        updateNative(sql.toString());
    }

    @Override
    public List<EdsNotificationMessage> getNotificationsByRequestType(ListingFilterParameter fp) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT n.id, n.*,n.date, case true when n.viewer_userid=").append(fp.getUserID()).append(" then 0 else 1 end orderL, 0 as clazz_ ");
        query.append(" FROM ").append(getCompanyId()).append(".notification_msg n ");
        query.append(" JOIN (" + getUnionTables() + ") n2 ON n2.id = n.id ");
        query.append(" LEFT JOIN ").append(getCompanyId()).append(".myuser mu ON mu.id = n.actor_userid ");
        query.append(" WHERE (n.deleted is null OR n.deleted <> true) ");
        query.append(" AND (mu.deleted is null OR mu.deleted <> true) ");
        query.append(" AND n.action_on_entity='").append(ActionOnEntityEnum.WAIT_APPROVAL.name()).append("' ");
        if (fp.getCategoryID() != null) {
            query.append(" AND n.entity_type='").append(NotificationTypeEnum.getByIdType(fp.getCategoryID()).name()).append("' ");
        }
        query.append(" AND n.entity_type<>'").append(NotificationTypeEnum.OnboardingStep.name()).append("'");
        query.append(" AND n.clicked is not true ");
        query.append(" AND n.read is not true ");
        query.append(" ORDER BY orderL, n.date DESC ");
        if (fp.getLimit() != null) {
            query.append(" limit ").append(fp.getLimit());
        }
        return findNative(query.toString(), EdsNotificationMessage.class);
    }

    private String getUnionTables() {
        String notificationTableWithUnion = " union all select distinct n.id FROM " + getCompanyId() + ".notification_msg n ";

        StringBuilder unionQuery = new StringBuilder();
        unionQuery.append("select distinct n.id FROM ").append(getCompanyId()).append(".notification_msg n ");
        unionQuery.append(" join ").append(getCompanyId()).append(".sickrequest sr on n.entity_id=sr.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on sr.overallStatus=st.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".approvers app on sr.currentApprover=app.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.LeaveRequests.name() + "' ");
        unionQuery.append(" and st.code !='" + EdsSickRequest.APPROVED + "' and  st.code !='" + EdsSickRequest.DENIED + "' and app.exactApprover=" + getUser().getObjectID());
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" join ").append(getCompanyId()).append(".expenseReport er on n.entity_id=er.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on er.overallStatus=st.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".approvers app on er.currentApprover=app.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.ExpenseClaim.name() + "' ");
        unionQuery.append(" and st.code !='" + EdsExpenseReport.EXPENSE_APPROVED + "' and  st.code !='" + EdsExpenseReport.EXPENSE_DECLINED + "' and app.exactApprover=" + getUser().getObjectID());
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" join ").append(getCompanyId()).append(".timesheet tsh on n.entity_id=tsh.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on tsh.statusId=st.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.TimeSheetApproval.name() + "' ");
        unionQuery.append(" and st.code !='" + EdsTimeSheet._APPROVE + "' and  st.code !='" + EdsTimeSheet._REJECT + "'");
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" join ").append(getCompanyId()).append(".benefitRequest br on n.entity_id=br.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on br.status=st.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.BenefitRequest.name() + "' ");
        unionQuery.append(" and st.code !='" + EdsBenefitRequest.APPROVED + "' and  st.code !='" + EdsBenefitRequest.REJECTED + "'");
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" join ").append(getCompanyId()).append(".invoice inv on n.entity_id=inv.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on inv.status_id=st.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.SaleInvoiceApproval.name() + "' ");
        unionQuery.append(" and st.code !='" + Constants.CLIENT_APPROVE + "'");
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" join ").append(getCompanyId()).append(".project pr on n.entity_id=pr.id ");
        unionQuery.append(" join ").append(getCompanyId()).append(".reference st on pr.statusid=st.id ");
        unionQuery.append(" where n.entity_type='" + NotificationTypeEnum.ProjectApproval.name() + "' ");
        unionQuery.append(" and st.code !='" + EdsProject.APPROVED_BY_CLIENT + "' and  st.code !='" + EdsProject.REJECTED_BY_CLIENT + "'");
        unionQuery.append(notificationTableWithUnion);
        unionQuery.append(" where n.entity_type not in ('" + NotificationTypeEnum.LeaveRequests.name() + "','" + NotificationTypeEnum.ExpenseClaim.name() + "','" + NotificationTypeEnum.TimeSheetApproval.name() + "',");
        unionQuery.append(" '" + NotificationTypeEnum.BenefitRequest.name() + "','" + NotificationTypeEnum.SaleInvoiceApproval.name() + "','" + NotificationTypeEnum.ProjectApproval.name() + "')");
        return unionQuery.toString();
    }

    @Override
    public void updateNotifications(List<Integer> newLatestIds, String ids) {
        if (newLatestIds != null) {
            ids = ServerUtils.integerListToString(newLatestIds);
        }
        if (ServerUtils.isNullOrEmpty(ids)) {
            ids = "0";
        }
        update("UPDATE EdsNotificationMessage SET read=TRUE WHERE objectID IN (" + ids + ")");
    }

    /**
     * In-memory dedup: atomically records this notification key and reports whether an identical one
     * (same company/entity/type/action/viewer) was already created within {@link #NOTIFICATION_DEDUP_WINDOW_MS}.
     * Returning true means "skip as duplicate". This avoids any database query on the hot path: a single user
     * action can fire several business events (e.g. expense claim add + send-to-approver + approve), and this
     * suppresses the resulting duplicate alerts. Generic/broadcast notifications (null/zero entity or viewer,
     * null type/action, or no company context) bypass dedup so they are never wrongly collapsed.
     */
    private boolean isRecentlyNotified(Integer entityID, NotificationTypeEnum entityType, ActionOnEntityEnum actionOnEntity, Integer viewerUserID) {
        if (entityID == null || entityID <= 0 || viewerUserID == null || viewerUserID <= 0 || entityType == null || actionOnEntity == null) {
            return false;
        }
        String companyId;
        try {
            companyId = getCompanyId();
        } catch (Exception e) {
            return false; // no company context available (e.g. system task) → don't dedup
        }
        if (companyId == null) {
            return false;
        }
        String dedupKey = companyId + "|" + entityID + "|" + entityType.name() + "|" + actionOnEntity.name() + "|" + viewerUserID;
        // putIfAbsent returns the previous value; non-null means the key already existed (a duplicate within the window)
        return RECENT_NOTIFICATIONS.asMap().putIfAbsent(dedupKey, Boolean.TRUE) != null;
    }

    private EdsNotificationMessage createNotification(Integer entityID, NotificationTypeEnum entityType, ActionOnEntityEnum actionOnEntity, Integer viewerUserID, Integer actorUserID, String subject) {
        if (notificationMsgSettingManager.isEnableNotification(entityType)) {
            if (isRecentlyNotified(entityID, entityType, actionOnEntity, viewerUserID)) {
                log.warn(
                        "-- -- -- -- -- -- --  Duplicate notification prevented check the sender code to fix duplication [entityId={}, entityType={}, action={}, viewerUserId={}] -- -- -- -- -- -- -- -- ",
                        entityID,
                        entityType,
                        actionOnEntity,
                        viewerUserID
                );
                return null;
            }

            EdsNotificationMessage edsNotificationMessage = new EdsNotificationMessage();
            edsNotificationMessage.setEntityID(entityID);
            edsNotificationMessage.setSubject(subject);
            edsNotificationMessage.setEntityType(entityType);
            edsNotificationMessage.setActionOnEntity(actionOnEntity);
            edsNotificationMessage.setViewerUserID(viewerUserID);
            edsNotificationMessage.setActorUserID(actorUserID);
            edsNotificationMessage.setDate(new Date());
            create(edsNotificationMessage);
            EdsUser vierwerUser = userManager.get(viewerUserID);
            try {
                if (DeviceTypeEnum.isMobile(vierwerUser.getMobileDeviceType()) && !ServerUtils.isNullOrEmpty(vierwerUser.getDeviceToken())
                        && NotificationTypeEnum.isMobileEvent(entityType)) {
                    if (DeviceTypeEnum.Android.equals(vierwerUser.getMobileDeviceType())) {
                        gcmSenderWebService.sendMessage(vierwerUser.getDeviceToken(), edsNotificationMessage);
                    } else if (DeviceTypeEnum.IPhone.equals(vierwerUser.getMobileDeviceType())) {
                        apnsSenderWebService.sendMessage(vierwerUser.getDeviceToken(), edsNotificationMessage);
                    }
                }

                WebSocketServerObject notification = new WebSocketServerObject();
                notification.setUserId(vierwerUser.getObjectID());

                EdsEmployee emp = (EdsEmployee) vierwerUser;
                ContactTo contactTo =  new ContactTo();
                contactTo.setPhone(emp.getPrimaryPhone());
                contactTo.setFirst_name(vierwerUser.getFirstName());
                contactTo.setLast_name(vierwerUser.getLastName() );
                contactTo.setName(vierwerUser.getFullName());
                contactTo.setContactType(String.valueOf(CrmConstants.TYPE_ACCOUNT));

                notification.setData(edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : notificationMsgServiceLocal.generatedNewNotificationName(edsNotificationMessage, false));
                notification.setEventType(WfmUiEventType.ON_PUSH_NOTIFICATION_POPUP);

//                RedisSocketObject redisSocketObject = new RedisSocketObject();
//                redisSocketObject.setCompanyId(Integer.parseInt(SecurityContext.getInstance().getCompanyId()));
//                redisSocketObject.setWebSocketServerObject(notification);
//                RedisClient.publish(redisSocketObject);

                if (viewerUserID != null && viewerUserID > 0) {
                    notification.setEventType(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE);
                    notification.setUserId(viewerUserID);
                    try {
                        rabbitMQService.sendWebPushNotification(notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return edsNotificationMessage;
        }
        return null;
    }

    private EdsNotificationMessage createNotification(Integer entityID, NotificationTypeEnum entityType, ActionOnEntityEnum actionOnEntity,
                                                      Integer viewerUserID, Integer actorUserID, String subject, String module) {
        if (notificationMsgSettingManager.isEnableNotification(entityType)) {
            if (isRecentlyNotified(entityID, entityType, actionOnEntity, viewerUserID)) {
                log.warn(
                        "-- -- -- -- -- -- --  Duplicate notification prevented check the sender code to fix duplication [entityId={}, entityType={}, action={}, viewerUserId={}] -- -- -- -- -- -- -- -- ",
                        entityID,
                        entityType,
                        actionOnEntity,
                        viewerUserID
                );
                return null;
            }
            EdsNotificationMessage edsNotificationMessage = new EdsNotificationMessage();
            edsNotificationMessage.setEntityID(entityID);
            edsNotificationMessage.setSubject(subject);
            edsNotificationMessage.setEntityType(entityType);
            edsNotificationMessage.setActionOnEntity(actionOnEntity);
            edsNotificationMessage.setViewerUserID(viewerUserID);
            edsNotificationMessage.setActorUserID(actorUserID);
            edsNotificationMessage.setDate(new Date());
            create(edsNotificationMessage);
            try {
                String msgTitle = notificationWfmMessageSource.localize(entityType.name());
                String msgBody = edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : notificationMsgServiceLocal.generatedNewNotificationName(edsNotificationMessage, false);

                sendPushNotificationForCoockieDev(msgTitle, msgBody, viewerUserID, module);

                WebSocketServerObject notification = new WebSocketServerObject();
                notification.setData(msgBody);

                if (viewerUserID != null && viewerUserID > 0) {
                    notification.setEventType(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE);
                    notification.setUserId(viewerUserID);
                    try {
                        rabbitMQService.sendWebPushNotification(notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return edsNotificationMessage;
        }
        return null;
    }

    @Override
    public List<Object[]> getNotificationCountByTypes(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select '" + NotificationTypeEnum.LeaveRequests.name() + "' LeaveRequests ,count(distinct sr.id) count from ").append(getCompanyId()).append(".sickrequest sr ");
        sql.append("left join ").append(getCompanyId()).append(".employee e  on e.id = sr.employeeid ");
        sql.append("inner join ").append(getCompanyId()).append(".myuser u on e.id = u.id ");
        sql.append("left join ").append(getCompanyId()).append(".approvers currentapprover on currentapprover.id = sr.currentapprover ");
        sql.append("left join ").append(getCompanyId()).append(".myuser exactapprover on exactapprover.id = currentapprover.exactApprover ");
        sql.append("left join").append(getCompanyId()).append(".reference r on r.id = sr.overallstatus ");
        sql.append("where u.deleted is not true and r.code ='" + EdsSickRequest.NOT_DEFINED + "' ");
        sql.append("and ( sr.employeeid =").append(fp.getEmployeeId());
        sql.append(" or currentapprover.exactApprover in (").append(fp.getEmployeeId()).append(")) ");
        sql.append(" union all ");
        sql.append("select '" + NotificationTypeEnum.ExpenseClaim + "' ,count(distinct exp.id) from ").append(getCompanyId()).append(".expenseReport exp ");
        sql.append("left join ").append(getCompanyId()).append(".employee e on e.id = exp.reporterId ");
        sql.append("inner join ").append(getCompanyId()).append(".myuser u on e.id = u.id ");
        sql.append("left join ").append(getCompanyId()).append(".approvers currentapprover on currentapprover.id = exp.currentapprover ");
        sql.append("left join ").append(getCompanyId()).append(".myuser exactapprover on exactapprover.id = currentapprover.exactApprover ");
        sql.append("left join ").append(getCompanyId()).append(".reference r on r.id = exp.overallstatus ");
        sql.append("where u.deleted is not true and r.code !='" + Constants.EXPENSE_REVERSED + "' ");
        sql.append("and ( exp.reporterId = ").append(fp.getEmployeeId()).append(" or currentapprover.exactApprover in (").append(fp.getEmployeeId()).append("))");

        return findNative(sql.toString());
    }
}
