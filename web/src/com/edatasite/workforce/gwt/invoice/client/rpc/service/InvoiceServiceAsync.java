package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListData;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateCreditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentAddEditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntry;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ListHeap;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductsAccountsTaxes;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectAllocateData;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.SendToFormFillingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethodsList;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.ShippingLabelData;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPackage;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface InvoiceServiceAsync {

    void findIDsBy(ListingFilterParameter fp, AsyncCallback<HashMap<String, Integer>> callback);

    Request getSaleInvoiceData(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceList> async);

    Request getSaleInvoiceDataByCategoryId(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceList> async);

    Request getInvoicesForConversionBalance(boolean isSaleInvoice, AsyncCallback<InvoiceList> callback);

    Request getRecurringInvoiceData(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<RecurringInvoiceListItem>> async);

    Request getRecurringBillData(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<RecurringInvoiceListItem>> async);

    Request getPurchaseInvoiceData(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<NewInvoice>> async);

    Request getPurchaseInvoiceDataFromSolr(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<NewInvoice>> async);

    void saveSaleInvoice(NewInvoice data, AsyncCallback<SaveResult> async);

    void saveSaleInvoice(NewInvoice data, boolean runWebhook, AsyncCallback<SaveResult> async);

    void saveSaleInvoice(NewInvoice invoice, String number, AsyncCallback<SaveResult> async);

    void getShortLink(Integer id, AsyncCallback<String> callback);

    void updateSaleInvoice(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updateSaleInvoice(NewInvoice data, boolean runWebhook, AsyncCallback<SaveResult> callback);

    void updateSaleInvoiceCustomFields(NewInvoice data, AsyncCallback<Void> callback);

    void savePurchaseInvoice(NewInvoice data, AsyncCallback<SaveResult> callback);

    void getCustomerEmailToSend(Integer customerID, AsyncCallback<CrmAccountItem> async);

    void updatePurchaseInvoice(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updatePurchaseInvoiceCustomFields(NewInvoice data, AsyncCallback<Void> callback);

    Request getCompanyTaxList(AsyncCallback<TaxList> async);

    Request getCompanyTaxList(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<TaxList> async);

    Request getAccountingTaxList(ListingFilterParameter filterParametrs, AsyncCallback<TaxListData> async);

    void getTax(Integer objectId, AsyncCallback<TaxData> async);

    void deleteTax(Integer objectId, AsyncCallback<Boolean> async);

    void getClients(String searchKey, AsyncCallback<TypeItem[]> async);

    void getInvoiceSummaryData(Integer id, AsyncCallback<NewInvoice> callback);

    void changeInvoiceStatus(Integer objectId, String statusCode, AsyncCallback<Void> callback);

    void changePurchaseInvoiceStatus(Integer objectId, String statusCode, AsyncCallback<Void> callback);

    void getInvoice(Integer id, AsyncCallback<NewInvoice> async);

    void getSaleInvoiceCustomer(Integer id, AsyncCallback<CrmAccountItem> async);

    void savePayment(PaymentData data, AsyncCallback<Integer> async);

    void reversePayment(Integer paymentID, DateNonConvertable date, AsyncCallback<Void> async);

    void deletePayment(Integer paymentID, AsyncCallback<TestRPC> async);

    void getCustomerCreditData(Integer invoiceID, Integer crmAccountID, boolean isExpense, AsyncCallback<PaymentAndPrePaymentData> async);

    void getInvoiceSettings(AsyncCallback<SettingsData> async);

    void saveCompanyInvoiceSettings(SettingsData data, AsyncCallback<Integer> async);

    void getInvoiceLogoUrl(AsyncCallback<String> callback);

    void getPaymentOrRefund(Integer paymentId, boolean isRefund, AsyncCallback<PaymentItem> async);

    void getItem(Integer itemID, AsyncCallback<NewInvoiceItem> async);

    void sendToClient(MessageItem messageItem, AsyncCallback<Integer> async);

    void saveSendToClientDetails(MessageItem messageItem, AsyncCallback<Void> async);

    void getUserInfo(AsyncCallback<ListHeap> async);

    void getCurrencies(ListingFilterParameter fp, AsyncCallback<CurrencyItem[]> async);

    void getExchangeRate(String to, AsyncCallback<BigDecimal> async);

    void getBaseCurrency(AsyncCallback<CurrencyItem> async);

    void getSuppliers(String searchKey, AsyncCallback<TypeItem[]> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getInvoiceDate(int day, AsyncCallback<Date> async);

    void getPaymentInstructions(String type, AsyncCallback<SelectItem[]> async);

    void getPaymentIntroduction(String type, AsyncCallback<SelectItem[]> async);

    void getInvoicePaymentLink(Integer id, AsyncCallback<String> async);

    void validateStockAvailability(QuantityItem[] items, Integer entityID, StockOutFlow outFlow, DateNonConvertable tillDate, AsyncCallback<SelectItem[]> async);

    void validateStockInconsistencyInDeleteProcess(StockTransactionType transactionType, Integer entityID, AsyncCallback<SelectItem> async);

    void validateStockInconsistencyInAdjustProcess(StockTransactionType transactionType, Integer entityID, QuantityItem[] qItems, AsyncCallback<SelectItem> async);

    void validateStockInconsistencyInUnbuildAssembly(Integer[] assemblyTransactionIds, AsyncCallback<SelectItem> async);

    void validateBatchSerials(HashMap<Integer, ArrayList<ProductSerialItem>> serialItems, AsyncCallback<String[]> async);

    void validateItemsInConsignmentToSell(QuantityItem[] items, Integer invoiceId, AsyncCallback<String[]> async);

    void validateItemsInConsignment(QuantityItem[] items, Integer invoiceId, AsyncCallback<String[]> async);

    void getGoogleCheckoutMerchantId(AsyncCallback<String> callback);

    void deleteInvoice(Integer objectID, String type, AsyncCallback<Integer> callback);

    void voidInvoice(Integer invoiceID, DateNonConvertable voidDate, AsyncCallback<Integer> callback);

    void getProductsAccountsTaxes(String invoiceType, AsyncCallback<ProductsAccountsTaxes> callback);

    void saveCreditNote(NewInvoice data, AsyncCallback<SaveResult> callback);

    void updateCreditNote(NewInvoice data, AsyncCallback<SaveResult> callback);

    void getCreditNoteSummaryData(Integer objectID, AsyncCallback<NewInvoice> callback);

    void getCreditNote(Integer objectID, AsyncCallback<NewInvoice> callback);

    void approveCreditNote(Integer creditNoteID, AsyncCallback<Void> callback);

    void saveCreditNoteRefund(PaymentData data, AsyncCallback<Void> callback);

    void getAllocateCreditData(Integer creditNoteID, AsyncCallback<AllocateCreditData> asyncCallback);

    void allocateCreditsToInvoices(AllocateCreditData data, AsyncCallback<Void> callback);

    void getAllInvoiceData(Params fp, AsyncCallback<NewInvoice> callback);

    void getAllCreditNoteData(Params fp, AsyncCallback<NewInvoice> callback);

    void saveBatchInvoiceData(NewInvoice[] invoices, AsyncCallback<Void> callback);

    void getClientOrSupplier(Integer clientSupplierID, String type, AsyncCallback<TypeItem> callback);

    void deleteAttachment(Integer attachmentId, AsyncCallback<Void> callback);

    void getCompanyPdfTemplates(String type, AsyncCallback<PdfTemplateItemList> callback);

    void getReceivePaymentData(ListingFilterParameter fp, FindMatchFilterData filterData, AsyncCallback<ReceivePaymentData> callback);

    void saveReceivePaymentData(ReceivePaymentData paymentData, boolean isClient, AsyncCallback<BatchPaymentResult> callback);

    void savePaymentRefundData(ReceivePaymentData paymentData, AsyncCallback<BatchPaymentResult> callback);

    void changeRelatedProject(Integer invoiceQuoteID, String type, Integer relatedProjectID, AsyncCallback<Void> callback);

    void changeBankAccount(Integer invoiceQuoteID, String type, Integer bankAccountId, AsyncCallback<Void> callback);

    void getSendToFormFillingData(SendToFormFillingData data, AsyncCallback<SendToFormFillingData> callback);

    void getSendToFormData(SendToFormFillingData data, Integer contactId, boolean isComposeForm, Integer formDataId, AsyncCallback<SendToFormFillingData> callback);

    void getCrmAccountProjectBalance(ProjectAllocateData data, AsyncCallback<ProjectAllocateData> callback);

    void saveCrmAccountProjectBalance(ProjectAllocateData data, AsyncCallback<Void> callback);

    Request getShippingMethodData(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ShippingMethod>> async);

    Request getShippingMethodList(AsyncCallback<ShippingMethodsList> async);

    void saveShippingMethod(ShippingMethod shippingMethod, AsyncCallback<ShippingMethod> async);

    void getShippingMethod(Integer shippingMethodID, Integer clientId, AsyncCallback<ShippingMethod> async);

    void getSaleQuoteByClient(ListingFilterParameter fp, AsyncCallback<ArrayList<NewInvoice>> async);

    void getBillableExpensesByClient(ListingFilterParameter fp, AsyncCallback<ArrayList<BillableExpenseItem>> async);

    void getPurchaseOrderBySupplier(Integer supplierID, AsyncCallback<NewInvoice[]> async);

    void getInvoicesByConvertedQuote(Integer quoteID, AsyncCallback<NewInvoice[]> callback);

    void getProductSerials(ListingFilterParameter filterParametrs, AsyncCallback<ProductSerialItem[]> asyncCallback);

    void getProductTrachBatches(ListingFilterParameter filterParametrs, AsyncCallback<ArrayList<ProductTrackBatchItem>> asyncCallback);

    void saveInvoiceTerms(InvoiceTermsItem invoiceTermsData, AsyncCallback<InvoiceTermsItem> callback);

    void applySupplierCreditData(ReceivePaymentData appliedPrePaymentData, AsyncCallback<Integer> callback);

    void getAllHistory(Integer invoiceID, String viewType, AsyncCallback<ArrayList<MyUpdateItem>> callback);

    void getHistoryByHistoryNote(Integer id, String viewType, AsyncCallback<List<HistoryNote>> callback);

    void getInvoiceTermsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<InvoiceTermsItem>> callback);

    void getInvoiceTerm(Integer termID, AsyncCallback<InvoiceTermsItem> callback);

    void deleteInvoiceTerm(Integer termID, AsyncCallback<Boolean> callback);

    void getOverdueInvoiceByCrmAccount(ListingFilterParameter parameter, AsyncCallback<ListResult<AgingSummaryItem>> async);


    void getUSPSRates(ShippingLabelData shippingLabelData, AsyncCallback<USPSPackage[]> callback);

    void getShippingLabelData(Integer invoiceID, AsyncCallback<ShippingLabelData> callback);

    void createInvoiceFromCourseBooking(Integer courseBookingID, String status, AsyncCallback<Integer> async);

    void getShippinhMethodsForLookUp(ListingFilterParameter filterParameter, AsyncCallback<ShippingMethod[]> async);

    void hasShippingMethod(AsyncCallback<Boolean> async);

    void saveBaseInvoiceCustomFields(String viewType, Integer objectID, ArrayList<CompanyCustomFieldItem> customFields, AsyncCallback<ArrayList<CompanyCustomFieldItem>> callback);

    void getDefaultAccountItem(String formType, String type, AsyncCallback<AccountItem> callback);

    void removeUnbookkeepedInvoice(Integer objectID, AsyncCallback<Void> callback);

    void getBatchPayments(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BatchPaymentListItem>> callback);

    void getBatchPaymentData(Integer objectID, AsyncCallback<ReceivePaymentData> callback);

    void sendEmail(MessageItem messageItem, AsyncCallback<Integer> async);

    void getBatchPaymentAddEditData(Integer objectID, boolean isReceivable, AsyncCallback<BatchPaymentAddEditData> callback);

    void deleteBatchPayment(Integer objectID, AsyncCallback<TestRPC> callback);

    void deleteBatchPayment(ArrayList<Integer> ids, AsyncCallback<Void> callback);

    void voidBatchPayment(Integer objectID, DateNonConvertable date, AsyncCallback<Integer> callback);

    void generateBatchPaymentNumberData(boolean isReceivable, Integer accountID, AsyncCallback<BankTransferNumberData> callback);

    void createInvoiceNoteAndHistory(Integer invoiceID, String viewType, HistoryListItem item, Boolean isInvoice, AsyncCallback<Integer> callback);

    void removeInvoiceNoteAndHistory(Integer invoiceQoteNoteID, AsyncCallback<Boolean> callback);

    void saveInvoiceEditCellValue(NewInvoice rowValue, String columnCode, AsyncCallback callback);

    void getInvoiceBillableExpenses(Integer invoiceId, AsyncCallback<ArrayList<ExpenseListItem>> callback);

    void generateInvoicePdfTemplateHtml(Integer invoiceId, Integer pdfTemplateId, AsyncCallback<String> callback);

    void savePurchaseInvoiceCellValue(NewInvoice rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void saveBatchPaymentCellValue(BatchPaymentListItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void countItemsInStock(QuantityItem item, AsyncCallback<BigDecimal> async);

    void loadInvoiceHistoryNote(Integer id, String viewType, boolean isInvoice, AsyncCallback<List<HistoryNote>> callback);

    void getBillOfEntry(Integer purchaseInvoiceId, Integer billOfEntryId, AsyncCallback<BillOfEntry> abstractAsyncCallback);

    void saveBillOfEntry(Integer purchaseInvoiceId, BillOfEntry billOfEntry, AsyncCallback<BillOfEntry> abstractAsyncCallback);

    void deleteBillOfEntry(Integer billOfEntryId, AsyncCallback<Boolean> abstractAsyncCallback);

    void getInvoiceBillableExpensesList(Integer invoiceId, AsyncCallback<ArrayList<ExpenseListItem>> async);

    void getPILookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void saveItemTableItems(ColumnConfigs item, Integer accountId,Integer discountId, String oldTitleName, ItemTableEnum itemTableEnum, AsyncCallback<Void> async);

    void getItemTableDefaultAccount(CustomFieldSection type, AsyncCallback<SelectItem> async);

    void getPaymentRefundItemData(ListingFilterParameter fp, AsyncCallback<ReceivePaymentData> async);

    void getPaymentRefund(ListingFilterParameter filterParameter, AsyncCallback<ReceivePaymentData> async);

    void deleteRefundPayment(Integer objectID, AsyncCallback<Void> async);

    void deleteSelectedInvoices(ArrayList<Integer> idArray, String type, AsyncCallback<Void> callback);

    void generateNewNumberData(String type, DateNonConvertable invoiceDate, AsyncCallback<InvoiceNumberData> async);

    void sendInvoiceToZatca(Integer invoiceId, String xmlType, AsyncCallback<Void> async);

    void saveItemTableDefaultDiscount(Integer dicountId, CustomFieldSection type, AsyncCallback<Void> async);

    void getItemTableDefaultDiscount(CustomFieldSection type, AsyncCallback<SelectItem> async);

    void getInvoiceCustomFieldItems(Integer customerId, ViewName viewName, AsyncCallback<NewInvoice> async);


    void checkExchangeRate(Integer selectedItemID, DateNonConvertable date, AsyncCallback<CurrencyListItem> callback);

    void reSendToFifo(Integer entityID, AsyncCallback<Boolean> async);

    void loadFixedAssetHistoryNote(Integer id, String viewType, AsyncCallback<List<HistoryNote>> async);
}
