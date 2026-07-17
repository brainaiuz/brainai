package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.SavedAssemblyItemManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
public class BuildAssemblyEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsSavedAssemblyItem> TYPE = new WfmType<>(EventTypes.buildAssemblyEventListener);

    public static String BUILD_ASSEMBLY_STATUS_SUBMITTED = "BUILD_ASSEMBLY_STATUS_SUBMITTED";
    public static String BUILD_ASSEMBLY_STATUS_APPROVED = "BUILD_ASSEMBLY_STATUS_APPROVED";
    public static String BUILD_ASSEMBLY_STATUS_REJECTED = "BUILD_ASSEMBLY_STATUS_REJECTED";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private SavedAssemblyItemManager savedAssemblyItemManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (BUILD_ASSEMBLY_STATUS_SUBMITTED.equals(event.getEventType())) {
            onSendToApprover(event);
        } else if (BUILD_ASSEMBLY_STATUS_APPROVED.equals(event.getEventType())) {
            onApprove(event);
        } else if (BUILD_ASSEMBLY_STATUS_REJECTED.equals(event.getEventType())) {
            onDecline(event);
        }
    }

    public void onSendToApprover(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        EdsUser receiver = null;
        if (isOk(buildAssembly.getCurrentApprover()) && isOk(buildAssembly.getCurrentApprover().getExactEmployee())) {
            receiver = buildAssembly.getCurrentApprover().getExactEmployee();
        }
        if (receiver != null) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblySendToApprover(buildAssembly.getObjectID(), creator, receiver.getObjectID(), event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }

    public void onApprove(EdsBusinessEvent event) {
        EdsUser receiver = userManager.get(event.getSourceID());
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblyApproveUpdate(buildAssembly.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onDecline(EdsBusinessEvent event) {
        EdsUser receiver = userManager.get(event.getSourceID());
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblyDeclineUpdate(buildAssembly.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblyAddUpdate(buildAssembly, creator, event.getTime());
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
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblyEditUpdate(buildAssembly, creator, event.getTime());
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
        EdsSavedAssemblyItem buildAssembly = savedAssemblyItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerBuildAssemblyDelete(buildAssembly, creator, event.getTime());
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
