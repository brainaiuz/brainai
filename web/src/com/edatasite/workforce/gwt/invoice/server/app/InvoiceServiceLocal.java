package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.InterCompanyDataMQ;
import com.edatasite.workforce.gwt.core.server.usps.USPSDeliveryConfirmation;
import com.edatasite.workforce.gwt.core.server.usps.USPSExpressMailLabel;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter.ConvertedQuotesDto;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceFieldsUpdaterDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Normurod Buriev  * Date: 11/29/11
 */
public interface InvoiceServiceLocal {

    EdsInvoiceCustomFields createInvoiceCustomFields(List<CompanyCustomFieldItem> customFieldItems);

    NewInvoice getInvoice(Integer id);

    NewInvoice getInvoiceForCreditNote(Integer id);

    Integer createDailyInvoiceForCustomerByCustomerStaff(Date date, Integer customerID, List<StudentAsInvoiceItem> studentAsInvoiceItemList, Integer invoiceID);

    Integer createDailyInvoiceForCustomerByCustomerStaff(Date date, Integer customerID, List<StudentAsInvoiceItem> studentAsInvoiceItemList, Integer invoiceID, Integer userID, boolean isCash);

    Integer voidInvoice(Integer invoiceID, DateNonConvertable voidDate);

    Integer deleteInvoice(Integer objectID, String type);

    TestRPC deletePayment(Integer paymentID);

    USPSDeliveryConfirmation getUSPSDeliveryConfirmation(Integer invoiceID, String serviceName, String fromZipCode, String toZipCode, String weightInOunces);

    USPSExpressMailLabel getUSPSExpressMailLabel(Integer invoiceID, String fromZipCode, String toZipCode, String weightInOunces);

    CurrencyItem getBaseCurrency();

    InvoiceNumberData generateAndGetSaleInvoiceNumber(EdsCompany company);

    SaveResult saveSaleInvoice(NewInvoice data);

    SaveResult saveSaleInvoice(NewInvoice data, boolean runWebhook);

    HashMap<Integer, Integer> convertInterCompanyTaxes(List<TaxData> taxes);

    void saveInterCompanySales(InterCompanyDataMQ data, HashMap<Integer, Integer> productConversionIDs, HashMap<Integer, Integer> discountConversionIDs, HashMap<Integer, Integer> taxConversionIDs);

    HashMap<Integer, Integer> convertInterCompanyDiscounts(List<DiscountItem> discounts);

    BigDecimal getExchangeRate(String to);

    PdfTemplateItemList getCompanyPdfTemplatesByType(String type);

    EdsInvoiceItemCustomFields createInvoiceItemCustomFields(List<CompanyCustomFieldItem> customFieldItems);

    SelectItem[] getRelatedProjectsWithFilter(ListingFilterParameter filterParameter);

    ListResult<SelectItem> getExpenseRelatedProjects(ListingFilterParameter fp);

    ListResult<NewInvoice> getPurchaseInvoiceDataFromSolr(ListingFilterParameter filterParameter);

    void saveInvoiceApprovers(EdsApprovable edsApprovable, ArrayList<ApproverItemMini> approvers, String statusCode, String approveStatusCode);

    void createInvoiceTransactionsAndCalculateProjectBugdet(EdsInvoice edsInvoice, EdsUser user);

    NewInvoice getInvoiceSummaryData(Integer id);

    NewInvoice getCreditNoteSummaryData(Integer id);

    PdfTemplateItemList getCompanyPdfTemplates(String type);

    ShippingMethod[] getShippinhMethodsForLookUp(ListingFilterParameter filterParameter);

    InvoiceNumberData getSaleInvoiceNumber();

    InvoiceNumberData getSaleInvoiceNumber(DateNonConvertable invoiceDate);

    ArrayList<CompanyCustomFieldItem> getSalesInvoiceCustomFieldsByNumber(String number);

    SaveResult updateSaleInvoice(NewInvoice data);

    SaveResult updateSaleInvoice(NewInvoice data, boolean runWebhook);

    NewInvoice getAllCreditNoteData(Params fp);

    SaveResult saveCreditNote(NewInvoice data);

    SaveResult updateCreditNote(NewInvoice data);

    InvoiceNumberData getCreditNoteNumber();

    InvoiceNumberData getCreditNoteNumber(DateNonConvertable invoiceDate);

    BatchPaymentResult saveReceivePaymentData(ReceivePaymentData paymentData, boolean isClient);

    void saveCreditNoteRefund(PaymentData data);

    void sendOverDueInvoiceReminders(Integer employeeId, Integer companyId, Boolean toClient, Integer recurrenceId);

    ReceivePaymentData getBatchPaymentPdfData(Integer objectID);

    void runPostDatedTransactions(Integer companyID);

    void updateSaleInvoiceByQB(NewInvoice newInvoice, int synchItemId);

    void updatePurchaseInvoiceByQB(NewInvoice newPurchaseInvoice, int synchItemId);

    Integer createInvoiceFromCourseBooking(Integer keyID);

    String updateInvoiceFields(InvoiceFieldsUpdaterDto dto) throws RestException;

    void updateSaleInvoicesAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID);

    InvoiceNumberData getSaleInvoiceNumber(String customPrefix);

    ContactItem[] getContactsEmailAsSelectItem(Integer objectID, String messageType, Integer contactId, boolean isComposeForm);

    void mergeCrmAccounts(Integer newAccountID, List<Integer> oldAccountIDs);

    void updateCompanyPurchaseInvoicesStatuses(Integer companyID);

    void resetCompanyPurchaseInvoicesStatuses(Integer companyID);

    void updateCompanySalesInvoicesStatuses(Integer companyID);

    void resetCompanySalesInvoicesStatuses(Integer companyID);

    void saleInvoiceToSolrIndex(SolrReindexRpc solrReindex);

    MessageItem getRecurringInvoiceMessageItem(Integer invoiceID);

    SelectItem[] getRecurrencePattern();

    Integer getNumberForDueDate();

    void sendMastercardReceiptToClient(Integer invoiceID);

    Integer saveGatewayPaymentData(PaymentData paymentData, String gatewayType);

    void updateGatewaySaleInvoice(NewInvoice data);

    Integer createInvoiceFromRecurringInvoice(Integer recurringInvoiceID);

    Integer createInvoiceFromRecurringBill(Integer recurringBillID);

    void createSalesInvoiceTransactionAndSendMessage(Integer newInvoiceID, Integer recurringInvoiceID);

    void createPurchaseInvoiceTransaction(Integer newInvoiceID, Integer recurringBillID);

    void sendMailToAccountants(InvoiceList data, Integer companyId, Integer recurrenceId);

    void sendMailFromAccountantsToClients(InvoiceList[] data, Integer employeeId);

    TaxList getCompanyTaxList();

    CurrencyItem[] getCurrencies(ListingFilterParameter fp);

    InvoiceList[] getSaleInvoiceDataForRecurrenceJobForEveryClient(Integer employeeId);

    InvoiceList getSaleInvoiceDataForRecurrenceJob(Integer employeeId);

    ReceivePaymentData getBatchPaymentData(Integer objectID);

    InvoiceList getSaleInvoiceData(ListingFilterParameter filterParametrs);

    TestRPC deleteBatchPayment(Integer objectID);

    InvoiceList getInvoiceByNumber(String invNumber, String type);

    List<CompanyCustomFieldItem> getInvoiceCustomFields(Integer entityId, ViewName viewName);

    void createInvoiceCustomFields(Integer entityId, List<CompanyCustomFieldItem> customFieldTO);

    void batchImportInvoicesFromFile(Integer fileID);

    SaveResult savePurchaseInvoice(NewInvoice purchaseInvoice);

    SaveResult updatePurchaseInvoice(NewInvoice data);

    ListResult<InvoiceTermsItem> getInvoiceTermsList(ListingFilterParameter filterParametrs);


    InvoiceNumberData getPurchaseInvoiceNumber(boolean isPICreditNote);

    Integer savePayment(PaymentData data);

    SelectItem[] validateStockAvailability(QuantityItem[] items, Integer entityID, StockOutFlow outFlow, DateNonConvertable tillDate);

    BigDecimal countItemsInStock(QuantityItem item);

    ListResult<AgingSummaryItem> getOverdueInvoiceByCrmAccount(ListingFilterParameter parameter);

    List<MyUpdateItem> getAllHistory(Integer invoiceID, String viewType);

    Integer saveStripeAccount(String authorization_code);

    Integer removeStripeAccount();

    AccountItem getDefaultAccountItem(String type, String accountType);

    AccountItem getChartOfAccountFromProductAndService(String type, String accountType, Integer productId);

    List<NewInvoiceItem> getSOQItems(List<Integer> quoteIds);

    List<NewInvoiceItem> getSOQItems(List<Integer> quoteIds, List<QIGroupingField> groupingFields);

    void updateSalesQuoteProgressInvoicingData(Integer saleInvoiceId, ConvertedQuotesDto convertedQuotes);

    void deleteInvoiceTransaction(Integer invoiceId, String type);

    void changeInvoiceStatus(Integer objectId, String statusCode);

    void changePurchaseInvoiceStatus(Integer objectId, String statusCode);

    void saveDeferredTransactionItemsByInvoice(Integer invoiceId);

    void deleteDeferredTransactionItemsByInvoice(Integer invoiceId);

    public FileItem[] getAttachments(Integer id, int folderType);
}
