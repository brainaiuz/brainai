package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.hrms;

import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ShiftEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsShift> TYPE = new WfmType<>(EventTypes.shiftEventListener);

    public static String SHIFT_APPROVED = "SHIFT_APPROVED";
    public static String SHIFT_SUBMITTED = "SHIFT_SUBMITTED";
    public static String SHIFT_REJECTED = "SHIFT_REJECTED";
    public static String SHIFT_DRAFT = "SHIFT_DRAFT";
    public static String SHIFT_ADD = "SHIFT_ADD";
    public static String SHIFT_EDIT = "SHIFT_EDIT";
    public static String SHIFT_DELETE = "SHIFT_DELETE";

    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (SHIFT_APPROVED.equals(event.getEventType())) {
            onApproveEvent(event);
        } else if (SHIFT_REJECTED.equals(event.getEventType())) {
            onRejectEvent(event);
        } else if (SHIFT_DRAFT.equals(event.getEventType())) {
            onDraftEvent(event);
        }
    }

    private void onDraftEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftDraft(overtimeObject, creator, event.getTime());
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
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftApprove(overtimeObject, creator, event.getTime());
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
        hrmsServiceLocal.insertEmployeePresentTime(shiftManager.get(event.getEntityID()));
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftReject(overtimeObject, creator, event.getTime());
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
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftAdd(overtimeObject, creator, event.getTime());
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
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftEdit(overtimeObject, creator, event.getTime());
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
        EdsShift overtimeObject = shiftManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerShiftDelete(overtimeObject, creator, event.getTime());
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
        hrmsServiceLocal.deleteTimeRecordsByShiftId(overtimeObject.getObjectID());
    }

}
