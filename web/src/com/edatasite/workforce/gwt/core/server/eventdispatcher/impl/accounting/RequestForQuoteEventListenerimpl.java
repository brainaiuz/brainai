package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RequestForQuoteEventListenerimpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsRFQ> TYPE = new WfmType<>(EventTypes.rfqEventListener);

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private RFQManager rfqManager;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsRFQ rfq = rfqManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfqAddUpdate(rfq, creator, event.getTime());
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

        EdsRFQ rfq = rfqManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfqEditUpdate(rfq, creator, event.getTime());
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
        EdsRFQ rfq = rfqManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerRfqDelete(rfq, creator, event.getTime());
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
