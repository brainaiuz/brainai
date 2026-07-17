package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
public class PlacementEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsPlacement> TYPE = new WfmType<>(EventTypes.placementEventListener);

    public static String PLACEMENT_STATUS_SUBMITTED = "PLACEMENT_STATUS_SUBMITTED";
    public static String PLACEMENT_STATUS_APPROVED = "PLACEMENT_STATUS_APPROVED";
    public static String PLACEMENT_STATUS_REJECTED = "PLACEMENT_STATUS_REJECTED";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private PlacementManager placementManager;
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
        } else if (PLACEMENT_STATUS_SUBMITTED.equals(event.getEventType())) {
            onSendToApprover(event);
        } else if (PLACEMENT_STATUS_APPROVED.equals(event.getEventType())) {
            onApprove(event);
        } else if (PLACEMENT_STATUS_REJECTED.equals(event.getEventType())) {
            onDecline(event);
        }
    }

    public void onSendToApprover(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPlacement placement = placementManager.get(event.getEntityID());
        EdsUser receiver = null;
        if (isOk(placement.getCurrentApprover()) && isOk(placement.getCurrentApprover().getExactEmployee())) {
            receiver = placement.getCurrentApprover().getExactEmployee();
        }
        if (receiver != null) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPlacementSendToApprover(placement.getObjectID(), creator, receiver.getObjectID(), event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }

    public void onApprove(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsPlacement placement = placementManager.get(event.getEntityID());

        try {
            hrmsServiceLocal.updateCandidateStatusOnApproval(placement.getCandidate().getObjectID(),PLACEMENT_STATUS_APPROVED);
            EdsMyUpdate myUpdate = myUpdateManager.registerPlacementApproveUpdate(placement.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onDecline(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsPlacement placement = placementManager.get(event.getEntityID());

        try {
            hrmsServiceLocal.updateCandidateStatusOnApproval(placement.getCandidate().getObjectID(),PLACEMENT_STATUS_REJECTED);
            EdsMyUpdate myUpdate = myUpdateManager.registerPlacementDeclineUpdate(placement.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsPlacement placement = placementManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPlacementAddUpdate(placement, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsPlacement placement = placementManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPlacementEditUpdate(placement, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsPlacement placement = placementManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPlacementDelete(placement, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
