package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Shohruh on 02-Oct-15.
 */
@Transactional
public class CashAdvanceEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCashAdvance> TYPE = new WfmType<>(EventTypes.cashAdvanceEventListener);

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    CashAdvanceManager cashAdvanceManager;
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
        } else if (EdsMyUpdate.STATUS_CHANGE.equals(event.getEventType())) {
            String status = cashAdvanceManager.get(event.getEntityID()).getStatus() != null ? cashAdvanceManager.get(event.getEntityID()).getStatus().getName() : "";
            if (status.equalsIgnoreCase("Submitted")) {
                onSubmitEvent(event);
            } else if (status.equalsIgnoreCase("Approved")) {
                onApproveEvent(event);
            } else if (status.equalsIgnoreCase("Rejected")) {
                onRejectEvent(event);
            }
        }
    }

    private void onApproveEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceApproveUpdate(cashAdvance, creator, event.getTime());
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
        notificationMsgManager.updateClickedNotificationEvent(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.APPROVED, cashAdvance);
        if (isOk(cashAdvance.getOverallStatus()) && !EdsCashAdvance.APPROVED.equals(cashAdvance.getOverallStatus().getCode())) {
            notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.WAIT_APPROVAL, cashAdvance);
        }
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceRejectUpdate(cashAdvance, creator, event.getTime());
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
        notificationMsgManager.updateClickedNotificationEvent(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.REJECTED, cashAdvance);
        if (isOk(cashAdvance.getOverallStatus()) && !EdsCashAdvance.REJECTED.equals(cashAdvance.getOverallStatus().getCode())) {
            notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.WAIT_APPROVAL, cashAdvance);
        }
    }

    private void onSubmitEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceSubmitUpdate(cashAdvance, creator, event.getTime());
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
        notificationMsgManager.updateClickedNotificationEvent(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance, ActionOnEntityEnum.WAIT_APPROVAL);
        //Dont send notification if status=DRAFT
        if (cashAdvance.getStatus() != null && !Constants.DRAFT.equalsIgnoreCase(cashAdvance.getStatus().getCode())) {
            notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.WAIT_APPROVAL, cashAdvance);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceAddUpdate(cashAdvance, creator, event.getTime());
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
        notificationMsgManager.updateClickedNotificationEvent(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance, ActionOnEntityEnum.WAIT_APPROVAL);
        //Dont send notification if status=DRAFT
        if (cashAdvance.getStatus() != null && !Constants.DRAFT.equalsIgnoreCase(cashAdvance.getStatus().getCode())) {
            notificationMsgManager.createCashAdvanceNotification(ActionOnEntityEnum.WAIT_APPROVAL, cashAdvance);
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceEditUpdate(cashAdvance, creator, event.getTime());
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
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCashAdvanceDeleteUpdate(cashAdvance, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
                notificationMsgManager.deleteNotificationEvent(cashAdvance.getObjectID(), NotificationTypeEnum.CashAdvance);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
