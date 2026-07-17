package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 07.07.12
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class BankAccountEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsBankAccount> TYPE = new WfmType<>(EventTypes.bankAccountEventListener);
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Autowired
    private BankAccountManager bankAccountManager;

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

        EdsUser creator = userManager.get(event.getSourceID());
        EdsBankAccount bankAccount = bankAccountManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankAccountAddUpdate(bankAccount, creator, event.getTime());
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
        EdsBankAccount bankAccount = bankAccountManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankAccountEditUpdate(bankAccount, creator, event.getTime());
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

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankAccountDeleteUpdate(event.getEntityID(), event.getCustomStringField(), creator, event.getTime());
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

}
