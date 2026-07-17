package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 28.07.12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class InvoicePaymentEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsInvoicePayment> TYPE = new WfmType<>(EventTypes.invoicePaymentEventListener);

    public static final String EVENT_SALES_INVOICE_PAYMENT_RECEIVE = "EVENT_SALES_INVOICE_PAYMENT_RECEIVE";
    public static final String EVENT_PURCHASE_INVOICE_PAYMENT_PAY = "EVENT_PURCHASE_INVOICE_PAYMENT_PAY";
    public static final String EVENT_SALES_INVOICE_PAYMENT_VOID = "EVENT_SALES_INVOICE_PAYMENT_VOID";
    public static final String EVENT_PURCHASE_INVOICE_PAYMENT_VOID = "EVENT_PURCHASE_INVOICE_PAYMENT_VOID";
    public static final String EVENT_SALES_INVOICE_PAYMENT_DELETE = "EVENT_SALES_INVOICE_PAYMENT_DELETE";
    public static final String EVENT_PURCHASE_INVOICE_PAYMENT_DELETE = "EVENT_PURCHASE_INVOICE_PAYMENT_DELETE";
    public static final String EVENT_SI_CREDIT_NOTE_REFUND = "EVENT_SI_CREDIT_NOTE_REFUND";
    public static final String EVENT_PI_CREDIT_NOTE_REFUND = "EVENT_PI_CREDIT_NOTE_REFUND";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private InvoiceManager invoiceManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EVENT_SALES_INVOICE_PAYMENT_RECEIVE.equalsIgnoreCase(event.getEventType())) {
            onSaleInvoicePay(event);
        } else if (EVENT_PURCHASE_INVOICE_PAYMENT_PAY.equalsIgnoreCase(event.getEventType())) {
            onPurchaseInvoicePay(event);
        } else if (EVENT_SALES_INVOICE_PAYMENT_VOID.equalsIgnoreCase(event.getEventType())) {
            onSaleInvoiceVoid(event);
        } else if (EVENT_PURCHASE_INVOICE_PAYMENT_VOID.equalsIgnoreCase(event.getEventType())) {
            onPurchaseInvoiceVoid(event);
        } else if (EVENT_SALES_INVOICE_PAYMENT_DELETE.equalsIgnoreCase(event.getEventType())) {
            onSaleInvoicePaymentDelete(event);
        } else if (EVENT_PURCHASE_INVOICE_PAYMENT_DELETE.equalsIgnoreCase(event.getEventType())) {
            onPurchaseInvoiceDelete(event);
        } else if (EVENT_SI_CREDIT_NOTE_REFUND.equalsIgnoreCase(event.getEventType())) {
            onCreditNoteSIRefund(event);
        } else if (EVENT_PI_CREDIT_NOTE_REFUND.equalsIgnoreCase(event.getEventType())) {
            onCreditNotePIRefund(event);
        }

    }

    public void onSaleInvoicePay(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoicePaymentReceive(payment, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }


    public void onPurchaseInvoicePay(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoicePay(payment, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onSaleInvoiceVoid(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSalesInvoicePaymentVoid(payment, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onPurchaseInvoiceVoid(EdsBusinessEvent event) {

        EdsPurchaseInvoice invoice = invoiceManager.getPurchaseInvoice(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceUpdate(invoice.getObjectID(), user, event.getTime(), EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_PAYMENT_VOID, invoice.getTotal());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onSaleInvoicePaymentDelete(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSalesInvoicePaymentDeleteUpdate(payment, event.getCustomStringField(), creator, event.getTime());
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

    public void onPurchaseInvoiceDelete(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoicePaymentDeleteUpdate(payment, event.getCustomStringField(), creator, event.getTime());
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

    public void onCreditNoteSIRefund(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSICreditNoteRefund(payment, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onCreditNotePIRefund(EdsBusinessEvent event) {

        EdsInvoicePayment payment = invoicePaymentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPICreditNoteRefund(payment, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

}
