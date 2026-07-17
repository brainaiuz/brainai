package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Normurod Buriev.
 * Date: 6/17/2021 3:35 PM
 */
public class DeferredTransactionCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static final String EVENT_INVOICE_DEFERRED_TRANSACTION = "EVENT_INVOICE_DEFERRED_TRANSACTION";
    public static final String EVENT_REMOVE_INVOICE_DEFERRED_TRANSACTION = "EVENT_REMOVE_INVOICE_DEFERRED_TRANSACTION";
    public static final String EVENT_EXPENSE_DEFERRED_TRANSACTION = "EVENT_EXPENSE_DEFERRED_TRANSACTION";
    public static final String EVENT_REMOVE_EXPENSE_DEFERRED_TRANSACTION = "EVENT_REMOVE_EXPENSE_DEFERRED_TRANSACTION";
    public static WfmType<EdsObject> TYPE = new WfmType<>(EventTypes.deferredTransactionCustomEventListener);
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoiceServiceLocal invoiceService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_INVOICE_DEFERRED_TRANSACTION.equals(event.getEventType())) {
            onInvoiceDeferredTransaction(event);
        } else if (EVENT_REMOVE_INVOICE_DEFERRED_TRANSACTION.equals(event.getEventType())) {
            onRemoveInvoiceDeferredTransaction(event);
        }
    }

    void onInvoiceDeferredTransaction(EdsBusinessEvent event) {
        EdsInvoice invoice = invoiceManager.get(event.getEntityID());
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        if (fs.isEnableDeferredTransaction() && invoice.getInvoiceItems().stream().anyMatch(EdsInvoiceItem::isDeferredTransasctionItem)) {
            invoiceService.saveDeferredTransactionItemsByInvoice(invoice.getObjectID());
        }
        event.setStatus(EventStatus.FAILED.name());
    }

    void onRemoveInvoiceDeferredTransaction(EdsBusinessEvent event) {
        EdsInvoice invoice = invoiceManager.get(event.getEntityID());
        if (invoice != null) {
            invoiceService.deleteDeferredTransactionItemsByInvoice(invoice.getObjectID());
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
