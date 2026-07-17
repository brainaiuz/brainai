package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 18:53:15
 */
public interface QuoteManager extends Manager<EdsQuote> {

    Long getCountSaleQuoteList(boolean isSalesQuote);

    Long getCountPurchaseOrderList();

    List<EdsSaleQuote> getSaleQuoteList(ListingFilterParameter fp, ListLoadConfig config);

    List<EdsSaleQuote> getSaleQuoteList(ListingFilterParameter fp, EdsCompany company, boolean isRecurringInvoice);

    List<EdsSaleQuote> getSaleQuoteListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsPurchaseOrder> getPurchaseOrderListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsPurchaseOrder> getPurchaseOrderList(ListingFilterParameter fp, ListLoadConfig config);

    EdsPurchaseOrder getPurchaseOrderByID(Integer purchaseOrderID);

    String getLastBillingInformation();

    Integer getQuoteFourDigitNumber(boolean isSalesOrder, DateNonConvertable quoteOrderDate);

    Integer getOrderFourDigitNumber(DateNonConvertable orderDate);

    List<EdsSaleQuote> getSalesQuoteByNumber(String number, Date creationDate, boolean isOrder);

    List<EdsSaleQuote> getSalesQuoteByNumberGlobal(String number, boolean isOrder);

    List<EdsPurchaseOrder> getPurchaseOrderByNumber(String number, Date creationDate);

    List<EdsPurchaseOrder> getPurchaseOrderByNumberGlobal(String number);

    List<Integer> getQuoteIdsByIDs(String ids);

    List<Integer> getPurchaseOrderIdsByIDs(String ids);

    List<Integer> getCompanyQuoteIdsWithLimit(Integer companyID, int startat, int limit);

    List<Integer> getPurchaseOrderIdsWithLimit(int startat, int limit);

    void deleteQuoteOldTaxTotals(EdsQuote quote);

    List<Integer> deleteQuoteItems(Integer quoteID, ArrayList<Integer> qiIds);

    EdsSaleQuote getSaleQuote(Integer saleQuoteID);
//    List<EdsSaleQuote> getAllSalesQuotes();
//    List<EdsPurchaseOrder> getAllPurchaseOrders();

    void removeRelationFromInvoice(Integer quoteID);

    List<EdsSaleQuote> getSaleQuotesByCrmAccountID(Integer crmAccountID);

    List<EdsSaleQuote> getUnDeletedSaleQuotesByCrmAccountID(Integer crmAccountID);

    List<EdsPurchaseOrder> getUnDeletedPurchaseOrdersByCrmAccountID(Integer crmAccountID);

    List<EdsPurchaseOrder> getPurchaseOrdersByCrmAccountID(Integer crmAccountID);

    BigDecimal getInventoryItemOrders(Integer itemID);//count of sale order by the inventory item

    HashMap<Integer, BigDecimal> getInventoryItemOrders(String itemIDs);//count of sale order by the inventory items

    List<EdsSaleQuote> getQuotesByClient(ListingFilterParameter fp, List<Integer> statusIds, boolean multiConvertEnabled);

    List<EdsPurchaseOrder> getPurchaseOrderBySupplier(ListingFilterParameter fp, Map<String, Integer> statusId);

    void removeQuoteItems(Integer quoteId);

    List<EdsPurchaseOrder> getPurchaseOrderListForSaasuSync(Integer startIndex, Integer limit);

    EdsPurchaseOrder getPurchaseOrderBySaasuGUID(String saasuGUID);

    List<EdsSaleQuote> getSaleQuotesByIds(String Ids);

    List<EdsPurchaseOrder> getPurchaseOrdersByIds(String Ids);

    EdsSaleQuote getSalesQuoteByCode(String code);

    List<EdsPurchaseOrder> getPurchaseOrderByQuoteId(Integer quoteId);

    EdsQuoteItem getQuoteItemByID(Integer quoteItemID);

    BigDecimal getRemainingQtyByQuoteId(Integer quoteId);

//    HashMap<Integer,BigDecimal> getSaleRamaingQty(String itemIDs);

    HashMap<Integer, BigDecimal> getOnPurchaseOrderCountByItem(String itemIDs);

    Integer getSaleQuoteListCount(ListingFilterParameter fp);

    void calculateCustomerQuoteBalance(Integer customerID);

    boolean findSaleQuotesByCrmAccountID(Integer objectID);

    List<EdsSaleQuote> getSaleQuotesByCrmContactID(Integer objectID);

    Object getQuotedItemCountByPeriod(Date startDate, Date endDate, Integer itemID, Integer objectID);

    Boolean hasConvertedItems(Integer quoteId);

    boolean hasConvertedShippingData(Integer shippingDataId);

    boolean isFullyShipped(Integer saleOrderId);

    SelectItem getBookingQty(Integer productId, Integer warehouseId, Integer entityId);

    List<EdsSaleQuote> getSaleQuotes(ListingFilterParameter fp, List<Integer> statusIds);

    List<SelectItem> getGroupedItems(List<Integer> Ids, HashMap<String, Boolean> fieldsForName, HashMap<String, Boolean> fieldsForDesc);

    List<Object[]> getGroupedItems(List<Integer> Ids, List<QIGroupingField> groupingFields);

    List<Object[]> getGroupedItemsByName(List<Integer> Ids);

    List<EdsSaleQuote> getQuoteByNumber(String quoteNumber);

    EdsPurchaseOrder getPurchaseOrderByNumber(String orderNumber);

    EdsQuote getByObjectKey(String objectKey);

    List<Integer> getQuotesByVat(Integer objectID);

    List<Integer> getExpensesByVat(Integer objectID);

    List<Integer> getBankTransafersByVat(Integer objectID);

    List<EdsSaleQuote> getSaleQuotesByCategoryId(Integer categoryId);

    List<EdsSaleQuote> getSaleOrderByProductCategoryID(Integer categoryId);

    ArrayList<EdsQuote> getSaleQuotesByDetailed(List<Integer> ids);

    List<Integer> getCompanyDeletedQuotesForSolr(SolrReindexRpc solrReindex);

    List<Integer> getCompanyDeletedPurchaseOrdersForSolr(SolrReindexRpc solrReindex);

    EdsQuote getOrderById(Integer quoteId);

    Map<Integer, EdsQuoteItem> getQuoteItemsByIds(Set<Integer> quoteItemIds);
}
