package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
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
 * User: Atabek
 * Date: 30.06.12
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class PurchaseInvoiceEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsPurchaseInvoice> TYPE = new WfmType<>(EventTypes.purchaseInvoiceEventListener);

    public static String EVENT_PURCHASE_INVOICE_APPROVE = "EVENT_PURCHASE_INVOICE_APPROVE";
    public static String EVENT_PURCHASE_INVOICE_ADD_CREDIT_NOTE = "EVENT_PURCHASE_INVOICE_ADD_CREDIT_NOTE";
    public static String EVENT_PURCHASE_INVOICE_PAYMENT_VOID = "EVENT_PURCHASE_INVOICE_PAYMENT_VOID";
    public static String EVENT_PURCHASE_INVOICE_PAYMENT_DELETE = "EVENT_PURCHASE_INVOICE_PAYMENT_DELETE";
    public static String EVENT_PURCHASE_INVOICE_PAYMENT_PAY = "EVENT_PURCHASE_INVOICE_PAYMENT_PAY";
    public static String EVENT_PURCHASE_INVOICE_MANAGER_APPROVE = "EVENT_PURCHASE_INVOICE_MANAGER_APPROVE";
    public static String EVENT_PURCHASE_INVOICE_MANAGER_REJECT = "EVENT_PURCHASE_INVOICE_MANAGER_REJECT";
    public static String EVENT_PURCHASE_INVOICE_SUBMITTED_TO_MANAGER = "EVENT_PURCHASE_INVOICE_SUBMITTED_TO_MANAGER";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    FinancialSettingsManager financialSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_PURCHASE_INVOICE_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onApproveClient(event);
        } else if (EVENT_PURCHASE_INVOICE_ADD_CREDIT_NOTE.equalsIgnoreCase(event.getEventType())) {
            onAddCreditNoteEvent(event);
        } else if (EVENT_PURCHASE_INVOICE_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_PURCHASE_INVOICE_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_PURCHASE_INVOICE_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceAddUpdate(purchaseInvoice, creator, event.getTime());
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
        EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceEditUpdate(purchaseInvoice, creator, event.getTime());
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
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceDeleteUpdate(event.getEntityID(), event.getCustomStringField(), creator, event.getTime());
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

    public void onApproveClient(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceApproveUpdate(purchaseInvoice, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onAddCreditNoteEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceAddCreditNote(purchaseInvoice, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onSubmittedToManager(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseInvoice edsPurchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceSubmittedToManager(edsPurchaseInvoice, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsPurchaseInvoice edsPurchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceManagerApproveUpdate(edsPurchaseInvoice, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    private void onManagerRejectEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsPurchaseInvoice edsPurchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseInvoiceManagerRejectUpdate(edsPurchaseInvoice, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }
}
