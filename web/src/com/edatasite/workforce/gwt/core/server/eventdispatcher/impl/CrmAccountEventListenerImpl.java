package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 02.05.12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class CrmAccountEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsCrmAccount> TYPE = new WfmType<>(EventTypes.crmAccountEventListener);
    public static final String EVENT_REINDEX_SQ_SI_PI_ER = "REINDEX_SQ_SI_PI_ER";
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent reportClaimsSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_REINDEX_SQ_SI_PI_ER.equals(event.getEventType())) {
            onReindex(event);
        }
    }

    private void onReindex(EdsBusinessEvent event) {

        EdsCrmAccount account = crmAccountManager.get(event.getEntityID());
        try {
            //Sales Quotes/Orders
            Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
            List<EdsSaleQuote> quoteList = quoteManager.getSaleQuotesByCrmAccountID(account.getObjectID());
            saleQuoteSolrComponent.indexes(quoteList, null);
            //Sales Invoices
            List<EdsSaleInvoice> saleInvoiceList = invoiceManager.getSaleInvoicesByCrmAccountID(account.getObjectID());
            saleInvoiceSolrComponent.indexes(saleInvoiceList);
            //Purchase Invoices
            List<EdsPurchaseInvoice> invoiceList = invoiceManager.getUndeletedPurchaseInvoicesByCrmAccountID(account.getObjectID());
            purchaseInvoiceSolrComponent.indexes(invoiceList);
            //Expense Claims
            List<EdsExpenseReport> expenseReportList = expenseReportManager.getExpensesByCrmAccountID(account.getObjectID());
            reportClaimsSolrComponent.indexes(expenseReportList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsCrmAccount account = crmAccountManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerAccountAddUpdate(account, creator, event.getTime());
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
        EdsCrmAccount account = crmAccountManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerAccountDeleteUpdate(account, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
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
        EdsCrmAccount account = crmAccountManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerAccountEditUpdate(account, creator, event.getTime());
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

