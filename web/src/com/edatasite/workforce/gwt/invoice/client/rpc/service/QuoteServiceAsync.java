package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ListHeap;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSupplierBid;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Anvarbek  * Date: 07.04.2009
 */
public interface QuoteServiceAsync {

    void checkForAccess(Integer quoteId, AsyncCallback<Boolean> callback);

    Request getSaleQuoteData(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<InvoiceList> callback);

    void getBaseCurrency(AsyncCallback<CurrencyItem> callback);

    Request getSaleQuoteData(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceList> callback);

    Request getSaleQuoteByCategoryId(Integer categoryId, AsyncCallback<InvoiceList> callback);

    Request getPurchaseOrderData(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<InvoiceList> callback);

    Request getPurchaseOrderData(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceList> callback);

    Request getPickListData(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<PickList>> callback);

    void getQuoteSummaryData(Integer id, AsyncCallback<NewInvoice> callback);

    void saveSaleQuote(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updateSaleQuote(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updateSaleQuoteCustomFields(NewInvoice data, AsyncCallback<Void> callback);

    void savePurchaseOrder(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updatePurchaseOrder(NewInvoice data, boolean checkForUnallocatedExpenses, AsyncCallback<SaveResult> callback);

    void updatePurchaseOrderCustomFields(NewInvoice data, AsyncCallback<Void> callback);

    void getUserInfo(AsyncCallback<ListHeap> callback);

    void sendToClientOrSupplier(MessageItem messageItem, AsyncCallback<Integer> callback);

    void convertToInvoice(Integer quoteID, AsyncCallback<Integer> callback);

    void convertToSaleOrder(Integer quoteID, AsyncCallback<SelectItem> callback);

    void checkForCreditLimit(Integer quoteID, AsyncCallback<SaveResult> callback);

    void getQuoteNumber(AsyncCallback<InvoiceNumberData> callback);

    void approveQuote(Integer id, AsyncCallback callback);

    void closedOrder(Integer id, AsyncCallback callback);

    void changeQuoteStatus(Integer id, String status, SelectItem rejectionReason, boolean hasApproveForAll, AsyncCallback callback);

    void deleteQuote(Integer objectID, String type, AsyncCallback<TestRPC> callback);

    void deleteSelectedQuotes(ArrayList<Integer> idArray, AsyncCallback<Void> callback);

    void deleteSelectedPurchaseOrders(ArrayList<Integer> idArray, AsyncCallback<Void> callback);

    void getPickList(Integer id, AsyncCallback<PickList> callback);

    void updatePickList(PickList data, AsyncCallback<Boolean> callback);

    void updateReadyToShipPickList(PickList data, AsyncCallback<Boolean> callback);

    Request getSaleOrderData(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<InvoiceList> callback);

    Request getSaleOrderData(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceList> callback);

    Request getSaleOrderDataByCategoryId(Integer categoryId, AsyncCallback<InvoiceList> callback);

    void getAllQuoteData(Params fp, AsyncCallback<NewInvoice> callback);

    void getQuoteConvertToInvoiceCustomType(AsyncCallback<String> async);

    void getPurchaseOrders(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void getGrnItems(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void convertToProject(Integer quoteID, AsyncCallback<Integer> callback);

    void getRFQData(Integer objectID, Params formParameters, AsyncCallback<RFQData> callback);

    void saveRFQData(RFQData rfqData, AsyncCallback<Integer> callback);

    void updateRFQStatus(Integer objectId, String statusCode, AsyncCallback<Void> callback);

    void updateStockTransferStatus(Integer objectId, String statusCode, String rejectionReason, AsyncCallback<Void> callback);

    void saveRFQNotes(HistoryListItem historyListItem, Integer rfqId, AsyncCallback<Integer> callback);

    void deleteRFQNotes(HistoryListItem historyListItem, AsyncCallback<Boolean> callback);

    void saveRFPNotes(HistoryListItem historyListItem, Integer rfqId, AsyncCallback<Integer> callback);

    void deleteRFPNotes(HistoryListItem historyListItem, AsyncCallback<Boolean> callback);

    void getRFQList(ListingFilterParameter filterParameters, AsyncCallback<ListResult<RFQData>> callback);

    void deleteRFQ(Integer rfqID, AsyncCallback<Void> callback);

    void saveRFQSupplierBids(RFQSupplierBid[] bids, AsyncCallback<Void> callback);

    void convertRFQToPurchaseOrder(RFQData rfqData, AsyncCallback<Integer> callback);

    void getProductPreferredSupplier(Integer productID, AsyncCallback<RFQItem> callback);

    void getRFPList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<RFPData>> callback);

    void deleteRFP(Integer objectID, AsyncCallback<Void> callback);

    void getRFPData(RFPData filter, AsyncCallback<RFPData> callback);

    void generateRfpNumber(AsyncCallback<NumberData> async);

    void getRFPItemsForStockAdjustment(ArrayList<Integer> ids, AsyncCallback<ProductItem[]> async);

    void saveRFPData(RFPData rfpData, AsyncCallback<String> callback);

    void sendRFPEmailRequest(MessageItem item, AsyncCallback<Void> callback);

    void changeRFPstatus(Integer id, String status, String rejectionReason, Boolean fromUi, AsyncCallback<Void> callback);

    void closePurchaseOrderRemainingQty(Integer purchaseOrderID, AsyncCallback<Void> callback);

    void validateItemsInStock(QuantityItem[] qItems, Integer quoteId, DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<String[]> callback);

    void updateRFQItem(RFQItem item, AsyncCallback<TestRPC> callback);

    void setSelectedRfpItems(ArrayList<Integer> rfpItems, Integer rfpId, AsyncCallback<Boolean> callback);

    void saveRFQCustomFields(Integer objectID, ArrayList<CompanyCustomFieldItem> customFields, AsyncCallback<ArrayList<CompanyCustomFieldItem>> callback);

    void getShippingDataList(ListingFilterParameter fp, AsyncCallback<ListResult<ShippingData>> ac);

    void getShippingDataForListing(ListingFilterParameter fp, AsyncCallback<ListResult<ShippingData>> ac);

    void getShippingData(Integer id, boolean isGdn, AsyncCallback<ShippingData> ac);

    void getShippingDate(Integer id, AsyncCallback<ShippingData> ac);

    void allocateExpensesToGrn(ShippingData shippingData, AsyncCallback<Integer> async);

    void deleteGoodsReceivedNotes(Integer id, AsyncCallback<TestRPC> ac);

    void deleteGoodsDeliveredNotes(Integer id, AsyncCallback<TestRPC> ac);

    void saveSaleQuoteEditCellValue(NewInvoice rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void savePurchaseOrderCellValue(NewInvoice rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void getRFQHistoryNotes(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void getRFPHistoryNotes(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void getProductQTYInWarehouse(Integer productId, Integer warehouseId, AsyncCallback<BigDecimal> callback);

    void getBookingProductQTYInWarehouse(Integer pickListId, Integer productId, Integer warehouseId, AsyncCallback<BigDecimal> callback);

    void saveRfqCellValue(RFQData rowValue, String columnCodeName, AsyncCallback<Void> async);

    void getSupplier(Integer supplierId, AsyncCallback<SelectItem> async);

    void updatePickListItem(PickList data, AsyncCallback<Boolean> async);

    void getRfqItemSuppliersAsSelectItem(Integer rfqId, AsyncCallback<SelectItem[]> asyncCallback);

    void getQuote(Integer id, Integer externalFormID, AsyncCallback<NewInvoice> async);

    void getStockTransferHistoryNotes(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void saveStockTransferNotes(HistoryListItem historyListItem, Integer stockTransferId, AsyncCallback<Integer> callback);

    void getConvertingItems(ListingFilterParameter fp, AsyncCallback<ArrayList<SaleOrderBaseInvoiceItem>> callback);

    void getGroupedItems(String objectType, ArrayList<Integer> Ids, HashMap<String, Boolean> fieldsForName, HashMap<String, Boolean> fieldsForDesc, AsyncCallback<SelectItem[]> callback);

    void deleteSelectedRFQs(ArrayList<Integer> ids, AsyncCallback<Void> callback);

    void saveStockAdjustmentNotes(HistoryListItem note, Integer stockAdjustmentId, AsyncCallback<Integer> async);

    void getStockAdjustmentHistoryNotes(Integer objectId, AsyncCallback<List<HistoryNote>> async);

    void isGdnNumberExist(String number, AsyncCallback<Boolean> async);

    void getQuoteCustomFieldItems(Integer customerId, boolean isSalesOrder, AsyncCallback<NewInvoice> async);

    void grnOrGdnCorrection(Integer id, AsyncCallback<String> callback);
}
