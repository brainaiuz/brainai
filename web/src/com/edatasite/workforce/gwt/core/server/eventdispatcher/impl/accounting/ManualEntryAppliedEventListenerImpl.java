package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ManualEntryAppliedEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsInvoicePayment> TYPE = new WfmType<>(EventTypes.manualJournalAppliedEventListener);
    public static final String EVENT_MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE = "EVENT_MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE";


    @Autowired
    ManualJournalManager manualJournalManager;

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    InvoicePaymentManager invoicePaymentManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE.equals(event.getEventType())) {
            onAppliedReceivablePayableEvent(event);
        }
    }


    public void onAppliedReceivablePayableEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerManualEntryAppliedReceivablePayable(invoicePayment, creator, event.getTime());
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