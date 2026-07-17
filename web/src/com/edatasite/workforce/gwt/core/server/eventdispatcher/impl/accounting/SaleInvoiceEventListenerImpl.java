package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: Mar 10, 2011
 * Time: 3:52:03 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class SaleInvoiceEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsSaleInvoice> TYPE = new WfmType<>(EventTypes.saleInvoiceEventListener);

    public static final String EVENT_SALES_INVOICE_SEND_TO_CLIENT = "EVENT_SALES_QUOTE_SEND_TO_CLIENT";
    public static final String EVENT_SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE = "EVENT_SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE";
    public static String EVENT_SALES_INVOICE_ADD_CREDIT_NOTE = "EVENT_SALES_INVOICE_ADD_CREDIT_NOTE";
    public static String EVENT_SALES_INVOICE_MANAGER_REJECT = "EVENT_SALEINVOICE_MANAGER_REJECT";
    public static String EVENT_SALES_INVOICE_MANAGER_APPROVE = "EVENT_SALEINVOICE_MANAGER_APPROVE";
    public static String EVENT_SALES_INVOICE_SUBMITTED_TO_MANAGER = "EVENT_SALEINVOICE_SUBMITTED_TO_MANAGER";

    @Autowired
    GenericSettingsManager genericSettingsManager;

    @Autowired
    QuoteManager quoteManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Autowired
    SolrManager solrManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_SALES_INVOICE_SEND_TO_CLIENT.equalsIgnoreCase(event.getEventType())) {
            onSentToClient(event);
        } else if (EVENT_SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE.equalsIgnoreCase(event.getEventType())) {
            onConvertFromSalesQuote(event);
        } else if (EVENT_SALES_INVOICE_ADD_CREDIT_NOTE.equalsIgnoreCase(event.getEventType())) {
            onAddCreditNoteEvent(event);
        } else if (EVENT_SALES_INVOICE_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_SALES_INVOICE_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_SALES_INVOICE_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        }

    }

    private void onConvertFromSalesQuote(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        String itemName = event.getCustomStringField();
        if (event.getEntityID() != null) {
            EdsSaleInvoice inv = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
                for (EdsQuote quote : inv.getConvertedQuotes()) {
                    quoteManager.calculateCustomerQuoteBalance(((EdsSaleQuote) quote).getClient().getObjectID());
                }
            }
        }
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceConvertedFromSaleQuoteUpdate(event.getEntityID(), itemName, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }


    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            for (EdsQuote quote : invoice.getConvertedQuotes()) {
                quoteManager.calculateCustomerQuoteBalance(((EdsSaleQuote) quote).getClient().getObjectID());
            }
        }

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceAddUpdate(invoice, creator, event.getTime());
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
        if (invoice.getStatus().getCode().equals(Constants.APPROVE)) {
            notificationMsgManager.createSaleInvoiceApprovallNotificationEvent(
                    invoice.getObjectID(),
                    creator.getObjectID(),
                    creator.getObjectID(),
                    ActionOnEntityEnum.APPROVED);
        }

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        String itemName = event.getCustomStringField();

        if (event.getEntityID() != null) {
            EdsSaleInvoice inv = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
                for (EdsQuote quote : inv.getConvertedQuotes()) {
                    quoteManager.calculateCustomerQuoteBalance(((EdsSaleQuote) quote).getClient().getObjectID());
                }
            }

            if (inv.getRelatedProject() != null) {
                if (inv.getRelatedProject().getDeleted() == null || !inv.getRelatedProject().getDeleted()) {
                    try {
                        /*solrManager.indexAddProject(inv.getRelatedProject());*/
                        projectSolrComponent.index(inv.getRelatedProject());
                    } catch (SolrServerException | IOException | InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }

        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsSaleInvoice inv = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
                if (inv != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceDeleteUpdate(inv, itemName, creator, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                    event.setMyUpdatesItemDelete(true);
                }
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            for (EdsQuote quote : invoice.getConvertedQuotes()) {
                quoteManager.calculateCustomerQuoteBalance(((EdsSaleQuote) quote).getClient().getObjectID());
            }
        }
        if (invoice.getStatus().getCode().equals(Constants.APPROVE)) {
            notificationMsgManager.createSaleInvoiceApprovallNotificationEvent(
                    invoice.getObjectID(),
                    creator.getObjectID(),
                    creator.getObjectID(),
                    ActionOnEntityEnum.APPROVED);
        }
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceEditUpdate(invoice, creator, event.getTime());
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


    public void onSentToClient(EdsBusinessEvent event) {
        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        try {
            if (invoice.getStatus().getCode().equals(Constants.APPROVE)) {
                notificationMsgManager.createSaleInvoiceApprovallNotificationEvent(
                        invoice.getObjectID(), user.getObjectID(),
                        user.getObjectID(),
                        ActionOnEntityEnum.APPROVED);
            }
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceSendToClient(invoice, user, event.getTime(), event.getAdditionalSourceID());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }


    }


    public void onAddCreditNoteEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSalesInvoiceAddCreditNote(saleInvoice, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onSubmittedToManager(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceSubmittedToManager(edsSaleInvoice, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceManagerApproveUpdate(edsSaleInvoice, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
        if (manager.getCompany().getObjectID().equals(65159) && edsSaleInvoice != null &&
                edsSaleInvoice.getCurrency() != null && edsSaleInvoice.getCurrency().getName().equals(CurrencyManager.USD) &&
                edsSaleInvoice.getBankAccount() != null && edsSaleInvoice.getBankAccount().getName().equals("STRIPE USD")) {
            accountingServiceLocal.createStripeInvoice(edsSaleInvoice);
        }
    }


    private void onManagerRejectEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleInvoice edsSaleInvoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleInvoiceManagerRejectUpdate(edsSaleInvoice, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }
}
