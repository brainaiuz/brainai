package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * User: Anvarbek  * Date: 07.04.2009
 */
public interface QuoteService extends RemoteService {

    Boolean checkForAccess(Integer quoteId);

    InvoiceList getSaleQuoteData(ListingFilterParameter filterParametrs/*, ListLoadConfig config*/);

    InvoiceList getSaleQuoteByCategoryId(Integer categoryId);

    InvoiceList getSaleQuoteData(ListingFilterParameter filterParametrs, ListLoadConfig config);

    CurrencyItem getBaseCurrency();

    InvoiceList getPurchaseOrderData(ListingFilterParameter filterParametrs, ListLoadConfig config);

    InvoiceList getPurchaseOrderData(ListingFilterParameter filterParametrs);

    ListResult<PickList> getPickListData(ListingFilterParameter filterParametrs);

    NewInvoice getQuoteSummaryData(Integer id);

    NewInvoice getQuote(Integer id, Integer externalFormID);

    SaveResult saveSaleQuote(NewInvoice data);

    SaveResult updateSaleQuote(NewInvoice data);

    void updateSaleQuoteCustomFields(NewInvoice data);

    SaveResult savePurchaseOrder(NewInvoice data);

    SaveResult updatePurchaseOrder(NewInvoice data, boolean checkForUnallocatedExpenses);

    void updatePurchaseOrderCustomFields(NewInvoice data);

    ListHeap getUserInfo();

    Integer sendToClientOrSupplier(MessageItem messageItem);

    Integer convertToInvoice(Integer quoteID);

    SelectItem convertToSaleOrder(Integer quoteID);

    SaveResult checkForCreditLimit(Integer quoteID);

    InvoiceNumberData getQuoteNumber();

    void approveQuote(Integer id);

    void closedOrder(Integer id);

    void changeQuoteStatus(Integer id, String status, SelectItem rejectionReason, boolean hasApproveForAll);

    TestRPC deleteQuote(Integer objectID, String type);

    void deleteSelectedQuotes(ArrayList<Integer> idArray);

    void deleteSelectedPurchaseOrders(ArrayList<Integer> idArray);

    PickList getPickList(Integer id);

    Boolean updatePickList(PickList data);

    Boolean updateReadyToShipPickList(PickList data);

    InvoiceList getSaleOrderData(ListingFilterParameter filterParametrs, ListLoadConfig config);

    InvoiceList getSaleOrderData(ListingFilterParameter filterParametrs);

    InvoiceList getSaleOrderDataByCategoryId(Integer categoryId);

    NewInvoice getAllQuoteData(Params fp);

    String getQuoteConvertToInvoiceCustomType();

    SelectItem[] getPurchaseOrders(ListingFilterParameter filterParameter);

    SelectItem[] getGrnItems(ListingFilterParameter filterParameter);

    Integer convertToProject(Integer quoteID);

    RFQData getRFQData(Integer objectID, Params formParameters);

    Integer saveRFQData(RFQData rfqData);

    void updateRFQStatus(Integer objectId, String statusCode);

    void updateStockTransferStatus(Integer objectId, String statusCode, String rejectionReason);

    Integer saveRFQNotes(HistoryListItem note, Integer rfqId);

    Boolean deleteRFQNotes(HistoryListItem historyListItem);

    Integer saveRFPNotes(HistoryListItem note, Integer rfqId);

    Boolean deleteRFPNotes(HistoryListItem historyListItem);

    ListResult<RFQData> getRFQList(ListingFilterParameter filterParameters);

    void deleteRFQ(Integer rfqID);

    void saveRFQSupplierBids(RFQSupplierBid[] bids);

    Integer convertRFQToPurchaseOrder(RFQData rfqData);

    RFQItem getProductPreferredSupplier(Integer productID);

    ListResult<RFPData> getRFPList(ListingFilterParameter filterParameter);

    void deleteRFP(Integer objectID);

    RFPData getRFPData(RFPData filter);

    ProductItem[] getRFPItemsForStockAdjustment(ArrayList<Integer> ids);

    String saveRFPData(RFPData rfpData) throws NumberExistingException;

    void sendRFPEmailRequest(MessageItem item);

    void changeRFPstatus(Integer id, String status, String rejectionReason, Boolean fromUi);

    void closePurchaseOrderRemainingQty(Integer purchaseOrderID);

    String[] validateItemsInStock(QuantityItem[] qItems, Integer quoteId, DateNonConvertable startDate, DateNonConvertable endDate);

    TestRPC updateRFQItem(RFQItem item);

    Boolean setSelectedRfpItems(ArrayList<Integer> rfpItems, Integer rfpId);

    ArrayList<CompanyCustomFieldItem> saveRFQCustomFields(Integer objectID, ArrayList<CompanyCustomFieldItem> customFields);

    ListResult<ShippingData> getShippingDataList(ListingFilterParameter fp);

    ListResult<ShippingData> getShippingDataForListing(ListingFilterParameter fp);

    ShippingData getShippingData(Integer id, boolean isGdn);

    Integer allocateExpensesToGrn(ShippingData shippingData);

    TestRPC deleteGoodsReceivedNotes(Integer id);

    TestRPC deleteGoodsDeliveredNotes(Integer id);

    boolean saveSaleQuoteEditCellValue(NewInvoice rowValue, String columnCodeName);

    boolean savePurchaseOrderCellValue(NewInvoice rowValue, String columnCodeName);

    List<HistoryNote> getRFQHistoryNotes(Integer objectId);

    List<HistoryNote> getRFPHistoryNotes(Integer objectId);

    BigDecimal getProductQTYInWarehouse(Integer productId, Integer warehouseId);

    void saveRfqCellValue(RFQData rowValue, String columnCodeName);

    SelectItem getSupplier(Integer supplierId);

    NumberData generateRfpNumber();

    Boolean updatePickListItem(PickList data);

    SelectItem[] getRfqItemSuppliersAsSelectItem(Integer rfqId);

    List<HistoryNote> getStockTransferHistoryNotes(Integer objectId);

    Integer saveStockTransferNotes(HistoryListItem note, Integer stockTransferId);

    List<HistoryNote> getStockAdjustmentHistoryNotes(Integer objectId);

    Integer saveStockAdjustmentNotes(HistoryListItem note, Integer stockAdjustmentId);

    BigDecimal getBookingProductQTYInWarehouse(Integer pickListId, Integer productId, Integer warehouseId);

    ArrayList<SaleOrderBaseInvoiceItem> getConvertingItems(ListingFilterParameter fp);

    SelectItem[] getGroupedItems(String objectType, ArrayList<Integer> Ids, HashMap<String, Boolean> fieldsForName, HashMap<String, Boolean> fieldsForDesc);

    void deleteSelectedRFQs(ArrayList<Integer> ids);

    Boolean isGdnNumberExist(String gdnNumber);

    NewInvoice getQuoteCustomFieldItems(Integer customerId, boolean isSalesOrder);

    String grnOrGdnCorrection(Integer objectId);

    ShippingData getShippingDate(Integer id);

    class App {
        public static QuoteServiceAsync get() {
            ServiceDefTarget target = GWT.create(QuoteService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/quote");
            return (QuoteServiceAsync) target;
        }
    }

}
