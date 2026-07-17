package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/30/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class AccountingProjectSolrEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.accountingProjectSolrEventListener);

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        Integer companyID = event.getCompanyId();
        Integer projectID = event.getEntityID();

        //Sales Quotes
        List<EdsSaleQuote> salesQuotes = projectManager.getProjectRelatedSalesQuotes(projectID);
        for (EdsSaleQuote quote : salesQuotes) {
            quote.setRelatedProject(null);
            quote.setConvertedToProject(false);
        }
        try {
            saleQuoteSolrComponent.indexes(salesQuotes, null);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        //Purchase Orders
        List<EdsPurchaseOrder> purchaseOrders = projectManager.getProjectRelatedPurchaseOrders(projectID);
        for (EdsPurchaseOrder po : purchaseOrders) {
            po.setRelatedProject(null);
            try {
                purchaseOrderSolrComponent.index(po);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Sales Invoices
        List<EdsSaleInvoice> salesInvoices = projectManager.getProjectRelatedSalesInvoices(projectID);
        for (EdsSaleInvoice saleInvoice : salesInvoices) {
            saleInvoice.setRelatedProject(null);
        }
        try {
            saleInvoiceSolrComponent.indexes(salesInvoices);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        //Purchase Invoices
        List<EdsPurchaseInvoice> purchaseInvoices = projectManager.getProjectRelatedPurchaseInvoices(projectID);
        for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoices) {
            purchaseInvoice.setRelatedProject(null);
        }
        try {
            purchaseInvoiceSolrComponent.indexes(purchaseInvoices);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        List<EdsExpenseReport> expenseReports = projectManager.getProjectRelatedExpenseReports(projectID);
        for (EdsExpenseReport er : expenseReports) {
            er.setProject(null);
            try {
                /*solrManager.addExpenseReportToIndex(er);*/
                expenseReportClaimsSolrComponent.index(er);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        Integer companyID = event.getCompanyId();
        Integer projectID = event.getEntityID();

        //Sales Quotes
        List<EdsSaleQuote> salesQuotes = projectManager.getProjectRelatedSalesQuotes(projectID);
        List<EdsPickList> pickLists = projectManager.getProjectRelatedPickLists(projectID);
        try {
            saleQuoteSolrComponent.indexes(salesQuotes, pickLists);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        //Purchase Orders
        List<EdsPurchaseOrder> purchaseOrders = projectManager.getProjectRelatedPurchaseOrders(projectID);
        for (EdsPurchaseOrder po : purchaseOrders) {
            try {
                purchaseOrderSolrComponent.index(po);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Sales Invoices
        List<EdsSaleInvoice> salesInvoices = projectManager.getProjectRelatedSalesInvoices(projectID);
        try {
            saleInvoiceSolrComponent.indexes(salesInvoices);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        //Purchase Invoices
        List<EdsPurchaseInvoice> purchaseInvoices = projectManager.getProjectRelatedPurchaseInvoices(projectID);
        try {
            purchaseInvoiceSolrComponent.indexes(purchaseInvoices);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        List<EdsExpenseReport> expenseReports = projectManager.getProjectRelatedExpenseReports(projectID);
        for (EdsExpenseReport er : expenseReports) {
            try {
                expenseReportClaimsSolrComponent.index(er);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        event.setStatus(EventStatus.COMPLETED.name());
    }
}
