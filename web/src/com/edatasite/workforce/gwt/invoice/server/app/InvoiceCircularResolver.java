package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 09.10.2008
 * Time: 13:46:37
 * To change this template use File | Settings | File Templates.
 */
public interface InvoiceCircularResolver {

    InvoiceList getSaleInvoiceData(ListingFilterParameter filterParametrs);

    InvoiceList getSaleInvoiceData(ListingFilterParameter filterParameters, ListLoadConfig config, EdsUser user, String param);

    InvoiceList getInvoicesForConversionBalance(boolean isSaleInvoice);

    InvoiceList getSaleInvoiceDataForRecurrenceJob(ListingFilterParameter filterParameters, Integer employeeId);

    InvoiceList[] getSaleInvoiceDataForRecurrenceJobForEveryClient(ListingFilterParameter filterParameters, Integer employeeId);

    ListResult<RecurringInvoiceListItem> getRecurringInvoiceData(ListingFilterParameter filterParameters);

    ListResult<RecurringInvoiceListItem> getRecurringBillData(ListingFilterParameter filterParameters);

    String getSaleInvoiceSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate);

    String getSaleQuoteSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate);

    String getSaleOrderSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate);

    String getPurchaseInvoiceCoreSolrQuery(ListingFilterParameter filterParameter, EdsUser user, String selectedDate);

    String getPurchaseOrderSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance);

    InvoiceList getPurchaseInvoiceData(ListingFilterParameter filterParameters);

    InvoiceList getSaleQuoteData(ListingFilterParameter filterParameters);

    InvoiceList getSaleQuoteData(ListingFilterParameter filterParameters, ListLoadConfig config);

    InvoiceList getSaleOrderData(ListingFilterParameter filterParameters, ListLoadConfig config);

    InvoiceList getSaleOrderData(ListingFilterParameter filterParametrs);

    InvoiceList getPurchaseOrderData(ListingFilterParameter filterParameters, ListLoadConfig config);

    InvoiceList getPurchaseOrderData(ListingFilterParameter filterParameters);

//    void setOverdue(EdsBaseInvoice baseInvoice);

    Date getCompanyDate();

//    TypeItem[] getTypeArray();

    String getInvoicePaymentLink(Integer invoiceId, NewInvoice invoiceData, Integer companyID);

    String getOrderPaymentLink(Integer invoiceId, NewInvoice invoiceData, Integer companyID);

    String getStripeInvoicePaymentLink(Integer invoiceId, NewInvoice invoiceData, Integer companyID);

    String getStripeOrderPaymentLink(Integer invoiceId, NewInvoice invoiceData, Integer companyID, boolean isInvoice);

    String getPayMeInvoicePaymentLink(NewInvoice invoice);

    String getClickInvoicePaymentLink(NewInvoice invoice);

    String getRevolutInvoicePaymentLink(NewInvoice invoice);

    String getInvoiceGoogleCheckoutMerchantId(Integer companyID);

    String getInvoiceLogoUrl(EdsCompany company);

    HistoryListItem[] getInvoiceNotes(Integer invoiceId);

    HistoryListItem[] getQuoteNotes(Integer objectID);

    SelectItem[] getRelatedProjects(ListingFilterParameter filterParametrs);

//    Integer indexCompanyPurchaseInvoice(SolrReindexRpc solrReindex, Integer start, Integer limit);

    NewInvoice getQuote(Integer id, Integer externalFormID);

    BankTransferNumberData parseGrnNumberData();

    BankTransferNumberData parseGdnNumberData();

    InvoiceNumberData getQuoteOrderNumberData(String type);

    InvoiceNumberData getQuoteOrderNumberData(String type, DateNonConvertable orderDate);

    InvoiceNumberData getInvoiceNumberData(EdsCompany company, String customPrefix);

    InvoiceNumberData getInvoiceNumberData(EdsCompany company, String customPrefix, DateNonConvertable invoiceDate);

    InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote);

    InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote, boolean isDebitNote);

    InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote, boolean isDebitNote, DateNonConvertable invoiceDate);

    InvoiceNumberData generatePurchaseInvoiceNumber(boolean isPICreditNote);

    FileItem[] getAttachments(Integer id, int folderType);

    CurrencyItem getBaseCurrency();

    CurrencyItem getBaseCurrency(Integer companyID);

    EdsCurrency returnBaseCurrency(EdsCompany company);

    String getMasterCardPaymentURL(Integer invoiceID, Integer companyID, BigDecimal paymentAmount, String paymentType, String userDefinedUrl);

    String validateMasterCardLinkParameters(Integer companyID, Integer invoiceID, BigDecimal paymentAmount, String paymentType);

    boolean isMasterCardParametersValid(Integer invoiceID, Integer companyID);

    boolean isElavonParametersValid(Integer invoiceID, Integer companyID);

    InvoiceList getPurchaseInvoiceListFromSolr(ListingFilterParameter filterParameter);

    SelectItem[] getInvoiceAndQuoteLookUpItems(ListingFilterParameter fp, CustomFieldLookUpTypeEnum typeEnum);

    LineItemDto wrapListItemToDto(EdsBaseInvoiceItem invoiceItem, List<CompanyCustomFieldItem> customFieldsItems, String transactionType);

    InvoicePaymentDto wrapPaymentToDto(EdsInvoicePayment payment);
}
