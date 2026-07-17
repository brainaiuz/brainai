package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.hrms;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.gwt.core.server.db.GroupPlacementManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupPlacementEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsGroupPlacement> TYPE = new WfmType<>(EventTypes.groupPlacementEventListener);

    public static String GROUP_PLACEMENT_APPROVED = "GROUP_PLACEMENT_APPROVED";
    public static String GROUP_PLACEMENT_SUBMITTED = "GROUP_PLACEMENT_SUBMITTED";
    public static String GROUP_PLACEMENT_REJECTED = "GROUP_PLACEMENT_REJECTED";
    public static String GROUP_PLACEMENT_DRAFT = "GROUP_PLACEMENT_DRAFT";
    public static String GROUP_PLACEMENT_ADD = "GROUP_PLACEMENT_ADD";
    public static String GROUP_PLACEMENT_EDIT = "GROUP_PLACEMENT_EDIT";
    public static String GROUP_PLACEMENT_DELETE = "GROUP_PLACEMENT_DELETE";

    @Autowired
    private GroupPlacementManager groupPlacementManager;
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
        } else if (GROUP_PLACEMENT_APPROVED.equals(event.getEventType())) {
            onApproveEvent(event);
        } else if (GROUP_PLACEMENT_REJECTED.equals(event.getEventType())) {
            onRejectEvent(event);
        } else if (GROUP_PLACEMENT_DRAFT.equals(event.getEventType())) {
            onDraftEvent(event);
        }
    }


    private void onDraftEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementDraft(placementObject, creator, event.getTime());
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
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementApprove(placementObject, creator, event.getTime());
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
        hrmsServiceLocal.convertPlacementItems(placementObject);
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementReject(placementObject, creator, event.getTime());
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
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementAdd(placementObject, creator, event.getTime());
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
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementEdit(placementObject, creator, event.getTime());
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
        EdsGroupPlacement placementObject = groupPlacementManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerGroupPlacementDelete(placementObject, creator, event.getTime());
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
