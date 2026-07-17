package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EXPENSE_SUBMITTED;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 3/25/11
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ExpenseReportEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsExpenseReport> TYPE = new WfmType<>(EventTypes.expenseReportEventListener);

    public static String EVENT_EXPENSE_REPORT_SEND_TO_APPROVER = "EVENT_EXPENSE_REPORT_SEND_TO_APPROVER";
    public static String EVENT_EXPENSE_REPORT_APPROVE = "EVENT_EXPENSE_REPORT_APPROVE";
    public static String EVENT_EXPENSE_REPORT_DECLINE = "EVENT_EXPENSE_REPORT_DECLINE";
    public static String EVENT_EXPENSE_REPORT_VOID = "EVENT_EXPENSE_REPORT_VOID";
    @Autowired
    ExpenseReportManager reportManager;
    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;

    @Autowired
    private NotificationMsgManager notificationMsgManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_EXPENSE_REPORT_SEND_TO_APPROVER.equals(event.getEventType())) {
            onSendToApprover(event);
        } else if (EVENT_EXPENSE_REPORT_APPROVE.equals(event.getEventType())) {
            onApprove(event);
        } else if (EVENT_EXPENSE_REPORT_DECLINE.equals(event.getEventType())) {
            onDecline(event);
        } else if (EVENT_EXPENSE_REPORT_VOID.equals(event.getEventType())) {
            onVoid(event);
        }
    }

    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportAddUpdate(expense, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
//        if(expense.getStatus()!=null && EXPENSE_SUBMITTED.equalsIgnoreCase(expense.getStatus().getCode())) {
//            notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
//            notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.WAIT_APPROVAL, expense);
//        }

    }

    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportEditUpdate(expense, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        if(expense.getStatus()!=null && EXPENSE_SUBMITTED.equalsIgnoreCase(expense.getStatus().getCode())) {
            notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.WAIT_APPROVAL, expense);
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());
        String itemName = event.getCustomStringField();

        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportDeleteUpdate(expense, itemName, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        if (expense != null && expense.getObjectID() != null) {
            notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
        }
    }

    public void onSendToApprover(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());
        EdsUser receiver = null;
        if (isOk(expense.getCurrentApprover()) && isOk(expense.getCurrentApprover().getExactEmployee())) {
            receiver = expense.getCurrentApprover().getExactEmployee();
        }
        if (receiver != null) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportSendToApprover(expense.getObjectID(), creator, receiver.getObjectID(), event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.WAIT_APPROVAL, expense);
    }

    public void onApprove(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportApproveUpdate(expense.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
        notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.APPROVED, expense);
        if (isOk(expense.getOverallStatus()) && !EdsExpenseReport.EXPENSE_APPROVED.equals(expense.getOverallStatus().getCode())) {
            notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.WAIT_APPROVAL, expense);
        }
    }

    public void onDecline(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerExpenseReportDeclineUpdate(expense.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
        notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.DECLINE, expense);
        if (isOk(expense.getOverallStatus()) && !EdsExpenseReport.EXPENSE_DECLINED.equals(expense.getOverallStatus().getCode())) {
            notificationMsgManager.createExpenseClaimNotification(ActionOnEntityEnum.WAIT_APPROVAL, expense);
        }
    }

    public void onVoid(EdsBusinessEvent event) {
        //EdsUser receiver = userManager.get(event.getSourceID());
        EdsExpenseReport expense = reportManager.getExpenseReport(event.getEntityID());

        /*try {
            myUpdateManager.registerExpenseReportVoidUpdate(expense.getObjectID(), receiver, event.getTime());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setProcessed(false);
        }*/
        notificationMsgManager.updateClickedNotificationEvent(expense.getObjectID(), NotificationTypeEnum.ExpenseClaim, ActionOnEntityEnum.WAIT_APPROVAL);

    }

}
