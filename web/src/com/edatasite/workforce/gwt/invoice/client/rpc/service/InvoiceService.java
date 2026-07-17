package com.edatasite.workforce.gwt.invoice.client.rpc.service;


import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListData;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface InvoiceService extends RemoteService {

    HashMap<String, Integer> findIDsBy(ListingFilterParameter fp);

    InvoiceList getSaleInvoiceData(ListingFilterParameter filterParametrs);

    InvoiceList getSaleInvoiceDataByCategoryId(ListingFilterParameter filterParametrs);

    InvoiceList getInvoicesForConversionBalance(boolean isSaleInvoice);

    ListResult<RecurringInvoiceListItem> getRecurringInvoiceData(ListingFilterParameter filterParametrs);

    ListResult<RecurringInvoiceListItem> getRecurringBillData(ListingFilterParameter filterParametrs);

    ListResult<NewInvoice> getPurchaseInvoiceData(ListingFilterParameter filterParametrs);

    ListResult<NewInvoice> getPurchaseInvoiceDataFromSolr(ListingFilterParameter filterParametrs);

    /**
     * @gwt.typeArgs <java.lang.String>
     */
    SaveResult saveSaleInvoice(NewInvoice data);

    SaveResult saveSaleInvoice(NewInvoice data, boolean runWebhook);

    SaveResult saveSaleInvoice(NewInvoice invoice, String number);

    SaveResult updateSaleInvoice(NewInvoice data);

    boolean reSendToFifo(Integer entityID);

    SaveResult updateSaleInvoice(NewInvoice data, boolean runWebhook);

    String getShortLink(Integer id);

    void updateSaleInvoiceCustomFields(NewInvoice data);

    SaveResult savePurchaseInvoice(NewInvoice data);

    SaveResult updatePurchaseInvoice(NewInvoice data);

    void updatePurchaseInvoiceCustomFields(NewInvoice data);

    TaxList getCompanyTaxList();

    TaxList getCompanyTaxList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    TaxListData getAccountingTaxList(ListingFilterParameter filterParametrs);

    TaxData getTax(Integer objectId);

    boolean deleteTax(Integer objectId);

    TypeItem[] getClients(String searchKey);

    NewInvoice getInvoiceSummaryData(Integer id);

    void changeInvoiceStatus(Integer objectId, String statusCode);

    void changePurchaseInvoiceStatus(Integer objectId, String statusCode);

    NewInvoice getInvoice(Integer id);

    CrmAccountItem getSaleInvoiceCustomer(Integer id);

    /**
     * @gwt.typeArgs <java.lang.String>
     */
    Integer savePayment(PaymentData data);

    void reversePayment(Integer paymentID, DateNonConvertable date);

    TestRPC deletePayment(Integer paymentID);

    PaymentAndPrePaymentData getCustomerCreditData(Integer invoiceID, Integer crmAccountID, boolean isExpense);


    SettingsData getInvoiceSettings();

    Integer saveCompanyInvoiceSettings(SettingsData data);

    String getInvoiceLogoUrl();

    PaymentItem getPaymentOrRefund(Integer paymentId, boolean isRefund);

    NewInvoiceItem getItem(Integer itemID);

    Integer sendToClient(MessageItem messageItem);

    void saveSendToClientDetails(MessageItem messageItem);

    ListHeap getUserInfo();

    CurrencyItem[] getCurrencies(ListingFilterParameter fp);

    BigDecimal getExchangeRate(String to);

    CurrencyItem getBaseCurrency();

    TypeItem[] getSuppliers(String searchKey);

    SelectItem[] getCountries();

    Date getInvoiceDate(int day);

    SelectItem[] getPaymentInstructions(String type);

    SelectItem[] getPaymentIntroduction(String type);

    SelectItem[] validateStockAvailability(QuantityItem[] items, Integer entityID, StockOutFlow outFlow, DateNonConvertable tillDate);

    SelectItem validateStockInconsistencyInDeleteProcess(StockTransactionType transactionType, Integer entityID);

    SelectItem validateStockInconsistencyInAdjustProcess(StockTransactionType transactionType, Integer entityID, QuantityItem[] qItems);

    SelectItem validateStockInconsistencyInUnbuildAssembly(Integer[] assemblyTransactionIds);

    String[] validateBatchSerials(HashMap<Integer, ArrayList<ProductSerialItem>> serialItems);

    BigDecimal countItemsInStock(QuantityItem item);

    String[] validateItemsInConsignmentToSell(QuantityItem[] items, Integer invoiceId);

    String[] validateItemsInConsignment(QuantityItem[] items, Integer invoiceId);

    String getInvoicePaymentLink(Integer id);

    String getGoogleCheckoutMerchantId();

    Integer deleteInvoice(Integer objectID, String type);

    Integer voidInvoice(Integer invoiceID, DateNonConvertable voidDate);

    ProductsAccountsTaxes getProductsAccountsTaxes(String invoiceType);

    SaveResult saveCreditNote(NewInvoice data);

    SaveResult updateCreditNote(NewInvoice data);

    NewInvoice getCreditNoteSummaryData(Integer objectID);

    NewInvoice getCreditNote(Integer objectID);

    void approveCreditNote(Integer creditNoteID);

    void saveCreditNoteRefund(PaymentData data);

    AllocateCreditData getAllocateCreditData(Integer creditNoteID);

    void allocateCreditsToInvoices(AllocateCreditData data);

    NewInvoice getAllInvoiceData(Params fp);

    NewInvoice getAllCreditNoteData(Params fp);


    void saveBatchInvoiceData(NewInvoice[] invoices);

    TypeItem getClientOrSupplier(Integer clientSupplierID, String type);

    void deleteAttachment(Integer attachmentId);

    PdfTemplateItemList getCompanyPdfTemplates(String type);

    ReceivePaymentData getReceivePaymentData(ListingFilterParameter fp, FindMatchFilterData filterData);

    ReceivePaymentData getPaymentRefundItemData(ListingFilterParameter fp);

    BatchPaymentResult saveReceivePaymentData(ReceivePaymentData paymentData, boolean isClient);

    void changeRelatedProject(Integer invoiceQuoteID, String type, Integer relatedProjectID);

    SendToFormFillingData getSendToFormFillingData(SendToFormFillingData data);

    SendToFormFillingData getSendToFormData(SendToFormFillingData data, Integer contactId, boolean isComposeForm, Integer formDataId);

    ProjectAllocateData getCrmAccountProjectBalance(ProjectAllocateData data);

    void saveCrmAccountProjectBalance(ProjectAllocateData data);

    ListResult<ShippingMethod> getShippingMethodData(ListingFilterParameter filterParametrs);

    ShippingMethodsList getShippingMethodList();

    ShippingMethod saveShippingMethod(ShippingMethod shippingMethod);

    ShippingMethod getShippingMethod(Integer shippingMethodID, Integer clientId);

    ArrayList<NewInvoice> getSaleQuoteByClient(ListingFilterParameter fp);

    ArrayList<BillableExpenseItem> getBillableExpensesByClient(ListingFilterParameter fp);

    NewInvoice[] getPurchaseOrderBySupplier(Integer supplierID);

    NewInvoice[] getInvoicesByConvertedQuote(Integer quoteID);

    ProductSerialItem[] getProductSerials(ListingFilterParameter filterParametrs);

    ArrayList<ProductTrackBatchItem> getProductTrachBatches(ListingFilterParameter filterParametrs);

    InvoiceTermsItem saveInvoiceTerms(InvoiceTermsItem invoiceTermsData);

    Integer createInvoiceFromCourseBooking(Integer courseBookingID, String status);

    Integer applySupplierCreditData(ReceivePaymentData appliedPrePaymentData);

    ArrayList<MyUpdateItem> getAllHistory(Integer invoiceID, String viewType);

    List<HistoryNote> getHistoryByHistoryNote(Integer invoiceID, String viewType);

    ListResult<InvoiceTermsItem> getInvoiceTermsList(ListingFilterParameter filterParametrs);

    InvoiceTermsItem getInvoiceTerm(Integer termID);

    Boolean deleteInvoiceTerm(Integer termID);

    ListResult<AgingSummaryItem> getOverdueInvoiceByCrmAccount(ListingFilterParameter parameter);


    USPSPackage[] getUSPSRates(ShippingLabelData shippingLabelData);

    ShippingLabelData getShippingLabelData(Integer invoiceID);

    ShippingMethod[] getShippinhMethodsForLookUp(ListingFilterParameter filterParameter);

    Boolean hasShippingMethod();

    ArrayList<CompanyCustomFieldItem> saveBaseInvoiceCustomFields(String viewType, Integer objectID, ArrayList<CompanyCustomFieldItem> customFields);

    AccountItem getDefaultAccountItem(String formType, String type);

    void removeUnbookkeepedInvoice(Integer objectID);

    ListResult<BatchPaymentListItem> getBatchPayments(ListingFilterParameter filterParametrs);

    ReceivePaymentData getBatchPaymentData(Integer objectID);

    Integer sendEmail(MessageItem messageItem);

    BatchPaymentAddEditData getBatchPaymentAddEditData(Integer objectID, boolean isReceivable);

    TestRPC deleteBatchPayment(Integer objectID);

    void deleteBatchPayment(ArrayList<Integer> ids);

    Integer voidBatchPayment(Integer objectID, DateNonConvertable date);

    BankTransferNumberData generateBatchPaymentNumberData(boolean isReceivable, Integer accountID);

    Integer createInvoiceNoteAndHistory(Integer invoiceID, String viewType, HistoryListItem historyItem, Boolean isInvoice);

    Boolean removeInvoiceNoteAndHistory(Integer invoiceQuoteNoteID);

    CrmAccountItem getCustomerEmailToSend(Integer customerID);

    void saveInvoiceEditCellValue(NewInvoice rowValue, String columnCodeName);

    ArrayList<ExpenseListItem> getInvoiceBillableExpenses(Integer invoiceId);

    ArrayList<ExpenseListItem> getInvoiceBillableExpensesList(Integer invoiceId);

    String generateInvoicePdfTemplateHtml(Integer invoiceId, Integer pdfTemplateId);

    boolean savePurchaseInvoiceCellValue(NewInvoice rowValue, String columnCodeName);

    boolean saveBatchPaymentCellValue(BatchPaymentListItem rowValue, String columnCodeName);

    List<HistoryNote> loadInvoiceHistoryNote(Integer id, String viewType, boolean isInvoice);

    List<HistoryNote> loadFixedAssetHistoryNote(Integer id, String viewType);

    BillOfEntry getBillOfEntry(Integer purchaseInvoiceId, Integer billOfEntryId);

    BillOfEntry saveBillOfEntry(Integer purchaseInvoiceId, BillOfEntry billOfEntry);

    Boolean deleteBillOfEntry(Integer billOfEntryId);

    SelectItem[] getPILookUp(ListingFilterParameter filterParameter);

    void saveItemTableItems(ColumnConfigs columnConfigs, Integer accountId,Integer discountId, String oldTitleName, ItemTableEnum itemTableEnum);

    SelectItem getItemTableDefaultAccount(CustomFieldSection type);

    void changeBankAccount(Integer invoiceQuoteID, String type, Integer bankAccountId);

    ReceivePaymentData getPaymentRefund(ListingFilterParameter filterParameter);

    BatchPaymentResult savePaymentRefundData(ReceivePaymentData paymentData);

    void deleteRefundPayment(Integer objectID);

    void deleteSelectedInvoices(ArrayList<Integer> idArray, String type);

    InvoiceNumberData generateNewNumberData(String type, DateNonConvertable invoiceDate);

    void sendInvoiceToZatca(Integer invoiceId, String xmlType) throws Exception;


    void saveItemTableDefaultDiscount(Integer discountId, CustomFieldSection type);


    SelectItem getItemTableDefaultDiscount(CustomFieldSection type);


    NewInvoice getInvoiceCustomFieldItems(Integer customerId, ViewName viewName);

    CurrencyListItem checkExchangeRate(Integer accountId, DateNonConvertable date);



    class App {
        public static InvoiceServiceAsync get() {
            ServiceDefTarget target = GWT.create(InvoiceService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/invoice");
            return (InvoiceServiceAsync) target;
        }
    }
}
