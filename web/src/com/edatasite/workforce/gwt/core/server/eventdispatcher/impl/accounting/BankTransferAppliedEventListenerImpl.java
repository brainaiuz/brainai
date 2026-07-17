package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BankTransferAppliedEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsInvoicePayment> TYPE = new WfmType<>(EventTypes.bankTransferAppliedEventListener);
    public static final String EVENT_BANK_TRANSFER_APPLIED_PAYABLE = "EVENT_BANK_TRANSFER_APPLIED_PAYABLE";
    public static final String EVENT_BANK_TRANSFER_APPLIED_RECEIVABLE = "EVENT_BANK_TRANSFER_APPLIED_RECEIVABLE";


    @Autowired
    SpendReceiveMoneyManager spendReceiveMoneyManager;

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    InvoicePaymentManager invoicePaymentManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_BANK_TRANSFER_APPLIED_PAYABLE.equals(event.getEventType())) {
            onAppliedPayableEvent(event);
        } else if (EVENT_BANK_TRANSFER_APPLIED_RECEIVABLE.equals(event.getEventType())) {
            onAppliedReceivableEvent(event);
        }
    }


    public void onAppliedPayableEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankTransferAppliedPayable(invoicePayment, creator, event.getTime());
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

    public void onAppliedReceivableEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerBankTransferAppliedReceivable(invoicePayment, creator, event.getTime());
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