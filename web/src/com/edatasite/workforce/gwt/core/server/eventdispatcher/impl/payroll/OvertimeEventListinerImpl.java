package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OvertimeEventListinerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsOvertimeObject> TYPE = new WfmType<>("overtimeEventListenerString");

    public static String PAYMENT_APPROVED = "PAYMENT_APPROVED";
    public static String PAYMENT_SUBMITTED = "PAYMENT_SUBMITTED";
    public static String PAYMENT_REJECTED = "PAYMENT_REJECTED";
    public static String PAYMENT_DRAFT = "PAYMENT_DRAFT";
    @Autowired
    private UserManager userManager;

    @Autowired
    private MyUpdateManager myUpdateManager;

    @Autowired
    private OvertimeManager overtimeManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EdsMyUpdate.STATUS_CHANGE.equals(event.getEventType())) {

            String status = overtimeManager.get(event.getEntityID()).getStatus() != null ? overtimeManager.get(event.getEntityID()).getStatus() : "";
            if (status.equals(PAYMENT_SUBMITTED)) {
//                onSubmitEvent(event);
            } else if (status.equals(PAYMENT_APPROVED)) {
                onApproveEvent(event);
            } else if (status.equals(PAYMENT_REJECTED)) {
                onRejectEvent(event);
            } else if (status.equals(PAYMENT_DRAFT)) {
                onDraftEvent(event);
            }
        }
    }

    private void onDraftEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeDraft(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }


    private void onApproveEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeApprove(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeReject(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeAdd(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeEdit(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
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
        EdsOvertimeObject overtimeObject = overtimeManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOvertimeDelete(overtimeObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
