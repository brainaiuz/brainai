package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.CrmAccountInvoiceTO;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringBill;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.dashboard.client.rpc.ClientsByAmmount;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RevisionHistoryItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InvoiceManager extends Manager<EdsInvoice> {

    List<EdsBaseSaleInvoice> getSaleInvoiceList(ListingFilterParameter fp);   // for recurrence Invoice

    List<EdsBaseSaleInvoice> getRecurringInvoiceList(ListingFilterParameter fp, ListLoadConfig config);

    List<EdsRecurringBill> getRecurringBillList(ListingFilterParameter fp, ListLoadConfig config, EdsCompany company);

    List<EdsSaleInvoice> getSaleInvoiceListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsPurchaseInvoice> getPurchaseInvoiceList(ListingFilterParameter fp, boolean isConversionBalance);

    List<EdsPurchaseInvoice> getPurchaseInvoiceList(ListingFilterParameter fp, boolean isConversionBalance, boolean isRecurringBill);

    List<EdsPurchaseInvoice> getPurchaseInvoiceListForSolr(SolrReindexRpc solrReindix, Integer startat, Integer limit);

    Integer getSaleInvoiceFourDigitNumber(DateNonConvertable dateNonConvertable);

    Integer getCreditNoteFourDigitNumber(DateNonConvertable invoiceDate);

    Integer getPurchaseInvoiceFourDigitNumber(boolean isCreditNote, DateNonConvertable invoiceDate);

    String getLastBillingInformation();

    List<EdsInvoice> findCompanyInvoices();

    List<ClientsByAmmount> findClientsByAmmount();

    List<EdsBaseSaleInvoice> getSaleInvoiceByNumber(String number, Date creationDate);

    List<EdsBaseSaleInvoice> getSaleInvoiceByNumberGlobal(String number);

    List<EdsPurchaseInvoice> getPurchaseInvoiceByNumber(String number, Integer supplier, Date creationDate);

    List<EdsPurchaseInvoice> getPurchaseInvoiceByNumberGlobal(String number);

    void removeRelationFromQuote(Integer invoiceID);

    void removeRelationFromQuote(Integer invoiceID, List<Integer> quoteIds);

    void deleteInvoiceOldTaxTotals(EdsInvoice invoice);

    List<Integer> deleteInvoiceItems(Integer invoiceID);

    List<EdsInvoice> getInvoicesByDate(Date from, Date to, boolean accrual);

    List<EdsInvoice> getInvoicesByPaymentsDate(Date from, Date to, boolean accrual);

    List<EdsExpenseReport> getExpenseReportsByDate(Date from, Date to, boolean accrual);

    List<EdsExpenseReport> getExpenseReportsByPaymentsDate(Date from, Date to, boolean accrual);

    List<EdsBankTransfer> getBankTransferByDate(Date from, Date to);

    List<EdsInvoice> getInvoicesForAllocatingCredits(EdsInvoice creditNote, boolean isReceivable);

    EdsSaleInvoice getSaleInvoice(Integer id);

    EdsInvoice getInvoice(Integer id);

    EdsSaleInvoice getSaleInvoiceByZapierOrderNumber(Long order_number);

    List<EdsSaleInvoice> getSaleInvoiceListByIDs(String IDs, String ascOrDesc);

    EdsPurchaseInvoice getPurchaseInvoice(Integer id);

    EdsRecurringBill getRecurringBill(Integer id);

    List<EdsSaleInvoice> getCompanySaleInvoiceByTimeZoneAndCorsor(Date currentCompanyDate, Integer lastId, int limit);

    List<EdsSaleInvoice> getCompanyOverdueSaleInvoiceByTimeZone(Date companyDate, int limit);

    List<EdsPurchaseInvoice> getCompanyPurchaseInvoiceByTimeZone(Date companyCurrentDate, Integer lastId, int limit);

    List<EdsPurchaseInvoice> getCompanyOverduePurchaseInvoiceByTimeZone(Date companyDate,int limit);

    boolean isClientSupplierInvoiceExists(Integer clientSupplierID, boolean isClient);

    List<Integer> getInvoiceIdsByIDs(String ids);

    List<Integer> getPurchaseInvoiceIdsByIDs(String ids);

    List<Integer> getCompanyInoviceIdsWithLimit(Integer companyID, int startat, int limit);

    List<Integer> getPurchaseInvoiceIdsWithLimit(Integer startat, Integer limit);

    Long getCountPurchaseInvoiceList();

    List<EdsInvoice> getInvoicesByClientSupplierAndStatuses(List<Integer> crmAccountIds, Integer currencyID, boolean isCustomer, List<String> statuses, FindMatchFilterData filterData, boolean isMultiCurrencyEnabled);

    List<EdsSaleInvoice> getSaleInvoicesByCrmAccountID(Integer crmAccountID);

    List<EdsSaleInvoice> getUnDeleteSaleInvoicesByCrmAccountID(Integer crmAccountID);

    List<EdsRecurringInvoice> getRecurringInvoicesByCrmAccountID(Integer crmAccountID);

    List<EdsPurchaseInvoice> getPurchaseInvoicesByCrmAccountID(Integer crmAccountID);

    List<EdsPurchaseInvoice> getUndeletedPurchaseInvoicesByCrmAccountID(Integer crmAccountID);

    List<EdsSaleInvoice> getSalesInvoicesByConvertedItem(Integer quoteID);

    Boolean hasConvertedItems(Integer quoteID);

    List<EdsPurchaseInvoice> getPurchaseInvoicesByConvertedItem(Integer quoteID);

    BigDecimal getConvertedInvoiceAmount(Integer quoteID, Integer currentInvoiceId);

    BigDecimal getConvertedBaseAmount(Integer quoteID, Integer currentInvoiceId);

    HashMap<Integer, BigDecimal> getConvertedInvoiceAmountsForListing(String quoteIDs);

    BigDecimal getConvertedInvoicesPercent(Integer quoteID, Integer invoiceID, boolean fromDelete);

    BigDecimal getConvertedInvoicesAmount(Integer quoteID, Integer invoiceID, boolean fromDelete);

    Map<Integer, BigDecimal> getEstAmounts(Integer quoteID);

    Map<Integer, BigDecimal> getPriorAmounts(Integer quoteID, Integer invoiceID);

    Long getTotalRecurringSaleInvoiceList(ListingFilterParameter filterParameters, ListLoadConfig listLoadConfig, EdsCompany company, boolean isRecurringInvoice);

    Long getTotalRecurringBillList(ListingFilterParameter filterParameters, ListLoadConfig listLoadConfig, EdsCompany company);

    void removeInvoiceItems(Integer invoiceID);

    EdsInvoicePayment getInvoicePaymentByCreditNote(EdsInvoice creditNote);

    RevisionHistoryItem[] getRevisionHistory(Integer invoiceID, String type);

    List<EdsSaleInvoice> getSaleInvoicesForSaasuSync(Integer startIndex, Integer limit);

    List<TCScheduleItem> getTCInvoicesForSchedule(ListingFilterParameter filterParameter);

    EdsSaleInvoice getSaleInvoiceBySaasuGUID(String saasuGUID);

    List<EdsInvoice> getCurrentInvoicesByCrmAccount(Integer crmAccountID, String type);

    BigDecimal getCustomerPrePaymentBalance(Integer crmaccountID);

    List<EdsPurchaseInvoice> getPurchaseInvoiceByIds(String Ids);

    EdsPurchaseInvoice getPurchaseInvoiceByExternalGUID(String code);

    List<EdsSaleInvoice> getSaleInvoiceByIds(String Ids);

    EdsSaleInvoice getSaleInvoiceByExternalGUID(String code);

    BigDecimal getConvertedQtyByQuoteItem(Integer quoteItemId);

    BigDecimal getConvertedAmountByQuoteItem(Integer quoteItemId);

    boolean checkInvoiceForExisting(String ref);

    List<Integer> getInvoiceEnabledCurrencies(Integer crmAccountID, boolean customer);

    Integer getSaleInvoiceListCount(ListingFilterParameter fp, boolean b);

    List<EdsInvoice> getProductInvoice(Integer productID, String type);

    List<EdsInvoiceItem> getProjectInvoiceItems(Integer projectID);

    HashMap<String, BigDecimal> getInvoiceTransactionsForChart(Date fromDate, Date toDate, boolean isPayable);

    BigDecimal getInvoiceTotalByCreditNoteId(Integer invCreditNoteId);

    EdsSaleQuote getSaleQuote(Integer quoteID);

    boolean findSaleInvoicesByCrmAccountID(Integer objectID);

    BigDecimal getInvoiceQuoteUnrecTotal(Integer invoiceID, String unrecRevCode);

    List<EdsInvoiceItem> getItemListAsExpenseByInvoice(Integer invoiceId);

    BigDecimal getPreviousConvertedSaleInvoiceItemsQuantity(Integer itemId, Integer quoteId, Date creationDate);

    BigDecimal getConvertedQuoteItemQuantity(Integer itemId, Integer quoteId, Date creationDate, Integer quoteItemId);

    List<NewInvoice> getInvoiceDueAmountsByProjectId(Integer projectId, Integer invoiceId, Date from, Date to);

    EdsInvoice getGDNInvoiceNumber(EdsShippingDataItem edsShippingDataItem);

    Map<Integer, EdsInvoice> getInvoiceListByIds(List<Integer> invoiceIds);

    ArrayList<Integer> quoteCreatorInvoices(Integer creator);

    List<SelectItem> getPurchaseInvoicesForLookUp(ListingFilterParameter fp);

    List<Object[]> getMultiQuoteAndGdnConvertedInvoiceData(Integer invoiceId);

    boolean checkIsBatchConvert(Integer invoiceId);

    void updateConvertedQuotes(Integer saleInvoiceId, List<Integer> quoteIds, String methodKey);

    String getConvertedQuoteIds(Integer invoiceId);

    Integer getDefaultTaxCalcType();

    List<EdsInvoice> getCreditOrDebitNotes(ListingFilterParameter fp);

    EdsInvoice getByObjectKey(String objectKey);

    String getOrderNumber(Integer itemId);

    List<Integer> getInvoicesByVat(Integer objectID);

    List<EdsSaleInvoice> getInvoicesByCategoryId(Integer categoryId);

    void insertRevolutUrl(Integer salesInvoiceId, String url, String publicId, Integer companyId);

    EdsInvoice
    getByIntegrationId(String integrationId);

    String getPreviouseInoiceHash();

    List<Integer> getCompanyDeletedInvoicesForSolr(SolrReindexRpc solrReindex);

    List<Integer> getCompanyDeletedPurchaseInvoicesForSolr(SolrReindexRpc solrReindex);

    List<CrmAccountInvoiceTO> getPrioritizedInvoices(List<Integer> crmAccountIds);

    EdsInvoice getInvoiceForPdfGeneration(Integer invoiceId);

    NewInvoice buildInvoiceData(EdsInvoice invoice);

    Map<Integer, NewInvoiceItem> getInvoiceItems(Integer invoiceId, boolean isQuote);

    NewInvoice getInvoiceProductSubItemsByTypes(Integer invoiceId);

    Map<Integer, String> getOrderNumbers(Set<Integer> quoteItemIds);

    Map<Integer, BigDecimal> getConvertedQuoteItemQuantities(Set<Integer> itemIds, Integer convertedItemID, Date creationDate, Set<Integer> quoteItemIds);

    Map<Integer, BigDecimal> getPreviousConvertedSaleInvoiceItemsQuantities(Set<Integer> itemIds, Integer convertedItemID, Date creationDate);
}
