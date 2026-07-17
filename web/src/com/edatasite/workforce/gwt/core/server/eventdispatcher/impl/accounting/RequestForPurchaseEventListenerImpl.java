package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class RequestForPurchaseEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsRFP> TYPE = new WfmType<>(EventTypes.rfpEventListener);

    public static String EVENT_RFP_MANAGER_REJECT = "EVENT_SALEINVOICE_MANAGER_REJECT";
    public static String EVENT_RFP_MANAGER_APPROVE = "EVENT_SALEINVOICE_MANAGER_APPROVE";
    public static String EVENT_RFP_SUBMITTED_TO_MANAGER = "EVENT_SALEINVOICE_SUBMITTED_TO_MANAGER";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private RFPManager rfpManager;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_RFP_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_RFP_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_RFP_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsRFP rfp = rfpManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfpAddUpdate(rfp, creator, event.getTime());
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

        EdsRFP rfp = rfpManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfpEditUpdate(rfp, creator, event.getTime());
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
        EdsRFP rfp = rfpManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfpDelete(rfp, creator, event.getTime());
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

    public void onSubmittedToManager(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsRFP edsRFP = (EdsRFP) rfpManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRFPSubmittedToManager(edsRFP, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsRFP edsRFP = (EdsRFP) rfpManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfpManagerApproveUpdate(edsRFP, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }


    private void onManagerRejectEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsRFP edsRFP = (EdsRFP) rfpManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfpManagerRejectUpdate(edsRFP, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }


}
