package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Omonullo on 16.05.2016.
 */
@Transactional
public class BankTransferEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsBankTransfer> TYPE = new WfmType<>(EventTypes.bankTransferEventListener);

    @Autowired
    SpendReceiveMoneyManager spendReceiveMoneyManager;

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;

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

    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        Integer bankTrasnferId = event.getEntityID();

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankTransferAdd(bankTrasnferId, creator, event.getTime());
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
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        Integer bankTrasnferId = event.getEntityID();

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankTransferEdit(bankTrasnferId, creator, event.getTime());
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
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        Integer bankTrasnferId = event.getEntityID();

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankTransferDelete(bankTrasnferId, creator, event.getTime());
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
    }
}
