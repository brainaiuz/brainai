package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: Faxriddin Taslimov  * Date: 06/12/2017
 */
public interface QuoteServiceLocal {

    TestRPC deleteQuote(Integer objectID, String type);

    NewInvoice getQuoteSummaryData(Integer id);

    SaveResult saveSaleQuote(NewInvoice data);

    SaveResult saveSaleQuoteForBatchImport(NewInvoice data, Set<GenericSettingsEnum> genericSettings, EdsFinancialSettings financialSettings, EdsWarehouse defaultWarehouse);

    SaveResult updateSaleQuote(NewInvoice data);

    void indexCompanySaleQuoteToSolr(SolrReindexRpc solrReindex);

    void purchaseOrderToSolrIndex(SolrReindexRpc solrReindex);

    void purchaseInvoiceToSolrIndex(SolrReindexRpc solrReindex);

    InvoiceNumberData getSalesOrderNumber();

    Integer getSalesQuoteDue();

    Integer getPurchaseOrderDue();

    InvoiceNumberData getOrderNumber();

    PickList getPickList(Integer id);

    Boolean updatePickList(PickList data);

    void updatePurchaseOrdersAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID);

    void updateSaleQuoteByQB(NewInvoice newQuote, int synchItemId);

    InvoiceList getPurchaseOrderData(ListingFilterParameter filterParametrs);

    SaveResult savePurchaseOrder(NewInvoice data);

    SaveResult updatePurchaseOrder(NewInvoice data, boolean checkForUnallocatedExpenses);

    SelectItem[] getPurchaseOrders(ListingFilterParameter filterParameter);

    List<CompanyCustomFieldItem> getQuoteCustomFields(Integer entityId, ViewName viewName);

    void createQuoteCustomFields(Integer entityId, List<CompanyCustomFieldItem> customFieldTO);

    ListResult<RFPData> getRFPList(ListingFilterParameter fp);

    InvoiceList getSaleQuoteData(ListingFilterParameter filterParametrs);

    Integer getGdnGrnCount(Integer picklistId, boolean isGdn);

    ShippingData getShippingData(Integer id, boolean isGdn);

    InvoiceList getSaleOrderData(ListingFilterParameter filterParametrs);

    ShippingData getShippingData(Integer id, boolean isGdn, boolean forExcel);

    String getShippingDataSolrQuery(ListingFilterParameter filterParametrs, EdsUser user);

    String getRFQSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, String selectedDate);
}
