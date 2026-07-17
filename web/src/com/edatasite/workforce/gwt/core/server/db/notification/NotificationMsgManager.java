package com.edatasite.workforce.gwt.core.server.db.notification;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilsh0d on 09.07.15.
 */
public interface NotificationMsgManager extends Manager<EdsNotificationMessage> {
    /*Uses for localization*/
    String YOU_HAVE_NOTIFICATIONS = "YOU_HAVE_NOTIFICATIONS";
    String MODULE_NAME_HR = "MODULE_NAME_HR";
    String MODULE_NAME_PM = "MODULE_NAME_PM";
    String MODULE_NAME_CRM = "MODULE_NAME_CRM";
    String MODULE_NAME_ACCOUNTING = "MODULE_NAME_ACCOUNTING";
    String MODULE_NAME_PAYROLL = "MODULE_NAME_PAYROLL";
    String MODULE_NAME_WORKSPACE = "MODULE_NAME_WORKSPACE";

    Integer getNewNotificationsIds(ArrayList<NotificationTypeEnum> entityTypes);

    List<EdsNotificationMessage> getNewLatestTen();

    Long getNotificationsCount();

    List<EdsNotificationMessage> getList(ListingFilterParameter fp, ArrayList<NotificationTypeEnum> entityTypes);

    Long getListTotal(ListingFilterParameter fp, ArrayList<NotificationTypeEnum> entityTypes);

    void deleteNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum, ActionOnEntityEnum actionOnEntityEnum);

    void deleteNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum);

    void updateClickedNotificationEvent(Integer entityID, NotificationTypeEnum notificationTypeEnum, ActionOnEntityEnum actionOnEntityEnum);

    boolean isHasNotification(Integer objectID, NotificationTypeEnum expenseClaim, ActionOnEntityEnum waitApproval);

    void updateNotifications(List<Integer> newLatestIds, String ids);

    void createLeaveRequestNotification(ActionOnEntityEnum actionOnEntityEnum, EdsSickRequest edsSickRequest);

    void createEventNotification(ActionOnEntityEnum actionOnEntityEnum, EdsEmployeeEvent edsEmployeeEvent);

    void createFixedAssetUpdateDeprecationNotification(EdsUser creator);

    void createCashAdvanceNotification(ActionOnEntityEnum actionOnEntityEnum, EdsCashAdvance cashAdvance);

    void createStepEmployeeNotification(ActionOnEntityEnum actionOnEntityEnum, EdsStepEmployee edsStepEmployee);

    void createTimeSheetApprovalNotification(ActionOnEntityEnum actionOnEntityEnum, EdsTimeSheetApprovalSession timesheetSession, Integer userID);

    void createExpenseClaimNotification(ActionOnEntityEnum actionOnEntityEnum, EdsExpenseReport expense);

    void createTaskOverDueDateReminderNotification(EdsTask task, EdsEmployee employee);

    void createMeetingMinutesNotification(EdsMeetingMinutes meetingMinutes, EdsUser creator, EdsUser attendeesEmployee);

    void createBenefitRequestNotification(ActionOnEntityEnum actionOnEntityEnum, Integer requestID);

    void createProjectOverDueDateReminderNotification(EdsProject project, EdsEmployee employee);

    void createWorkstreamOverDueDateReminderNotification(EdsWorkStream workstream, EdsEmployee employee);

    void createTimeSheetOverDueReminderNotificationEvent(EdsUser user, ActionOnEntityEnum actionOnEntityEnum);

    void createInvoiceOverDueNotificationEvent(Integer invoiceId, EdsUser user, ActionOnEntityEnum actionOnEntityEnum);

    void createTaskAssigneeNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, ActionOnEntityEnum actionOnEntityEnum);

    void createSaleInvoiceApprovallNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, ActionOnEntityEnum actionOnEntityEnum);

    void createWorkflowNotificationEvent(Integer entityID, Integer actorID, Integer viewerID, NotificationTypeEnum entityType, String subject, ActionOnEntityEnum actionOnEntity);

    void createSalesOrderNotification(EdsSaleQuote quote, EdsUser creator);

    void createProjectApprovalNotificationEvent(Integer entityID, Integer viewerID, Integer actorID, ActionOnEntityEnum actionOnEntityEnum);

    void clearUserUnreadMsg(ArrayList<NotificationTypeEnum> notificationTypes);

    List<EdsNotificationMessage> getNotificationsByRequestType(ListingFilterParameter fp);

    void createWorkflowNotificationEventToUsers(Integer entityID, Integer actorID, List<Integer> viewerIDs, NotificationTypeEnum entityType, String subject);

    List<Object[]> getNotificationCountByTypes(ListingFilterParameter fp);

    void createSalesQuoteNotification(EdsSaleQuote quote, EdsUser creator);

    void createWhatsappMessageNotification(EdsWhatsAppMessage message,EdsUser creator);

    void clearAllUnreadNotifications();

}
