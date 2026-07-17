package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.core.domain.payrolluk.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalanceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.02.2009
 * Time: 15:18:20
 * To change this template use File | Settings | File Templates.
 */
public interface TransactionManager extends Manager<EdsTransaction> {

    ListingResult<Transaction> listingResult(Date from, Date to, String orderBy, Integer journalId, String departmentAndTreeChildIDs, ListingFilterParameter listingFilterParameter);

    Integer getCompanyLastTransactionOrderID();

    Integer getCompanyLastTransactionOrderID(EdsCompany company);

    List<Transaction> listAttendedInAccountInPeriod2(ListingFilterParameter fp, String departmentAndTreeChildIDs, boolean isCacheOnly);

    void deleteBankTransactionByAccountId(Integer accountId);

    EdsInvoiceTransaction getTransactionByInvoice(EdsInvoice invoice);

    EdsInvoiceTransaction getTransactionByInvoice(EdsInvoice invoice, boolean evenDeleted);

    EdsInvoicePaymentTransaction getTransactionByPayment(EdsInvoicePayment invoicePayment);

    void deleteTransactionByRefund(EdsPaymentRefund paymentRefund);

    EdsOverPaymentTransaction getTransactionByOverPayment(EdsOverPayment overPayment);

    EdsPaymentRefundTransaction getTransactionByRefund(EdsPaymentRefund paymentRefund);

    void deleteOverPaymentTransaction(EdsOverPayment overPayment);

    EdsInventoryTransaction getTransactionByInventory(EdsItem inventory);

    EdsStockAdjustmentTransaction getTransactionByStockAdjustment(EdsStockAdjustment stockAdjustment);

    EdsStockTransferTransaction getTransactionByStockTransfer(EdsStockTransfer stockTransfer);

    EdsExpenseTransaction getTransactionByExpense(EdsExpenseReport expenseReport);

    EdsExpensePaymentTransaction getTransactionByExpensePayment(EdsExpensePayment expensePayment);

    void deleteTransactionItems(Integer transactionId);

    void deleteCOGSTransactionItems(Integer transactionId, Integer inventoryId, Integer transactionItemId);

    void deleteStockTransferTransactionItems(Integer transactionId, Integer inventoryId, Integer transactionItemId);

    BigDecimal getCOGSAmountOfStockTransferItem(Integer transactionId, Integer inventoryId, Integer transactionItemId);

    void deleteInvoicePaymentTransaction(EdsInvoicePayment invoicePayment);

    void deleteExpenseReportTransaction(EdsExpenseReport expenseReport);

    void deleteExpensePaymentTransaction(EdsExpensePayment expensePayment);

    List<EdsTransactionItem> getPaymentTransactionsByReconsileStatus(FindMatchFilterData filterData);

    List<EdsTransactionItem> getTransactionItemsByAccountAndDate(Integer accountID, Date from, Date to, boolean debit);

    EdsPayslipTransaction getTransactionByPayslip(Integer payslipId);

    EdsCustomerTransaction getCustomerOpeningBalanceTransaction(Integer clientID);

    EdsSupplierTransaction getSupplierOpeningBalanceTransaction(Integer supplierID);

    void deleteInventoryTransaction(Integer productID);

    void deleteTransaction(Integer transactionID);

    void deleteAdditionalPaymentTransaction(Integer paymentDeductionId);

    EdsFixedAssetTransaction getFixedAssetTransaction(Integer fixedAssetID);

    EdsDisposalTransaction getFixedAssetDisposalTransaction(Integer fixedAssetID);

    List<Object[]> getBalanceDataByDateAndAccountCategory(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID);

    Object[] getBalanceDataByDateAndAccountCategoryByDepartment(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectId);

    List<EdsAccount> getBalanceSubsidiariesDataByDateAndAccountCategory(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID);

    BigDecimal[] getListCashOnlyInAccountInPeriodCount(ListingFilterParameter fp, String departmentAndTreeChildIDs);

    BigDecimal[] getListAttendedInAccountInPeriodCount(ListingFilterParameter fp, String departmentAndTreeChildIDs);

    List<Integer> getCrmAccountUsedCurrencies(ArrayList<Integer> crmAccountIDs, String crmAccountType);

    LinkedHashMap<BigDecimal, BigDecimal> getCrmAccountEarlyBalance(ArrayList<Integer> crmAccountIDs, String from, String crmAccountType, Integer currencyID, boolean isBaseCurrency);

    BigDecimal getCrmAccountPrevPageBalance(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency, ListingFilterParameter fp);

    List<CrmAccountBalanceItem> getCrmAccountBalance(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency, ListingFilterParameter fp, BigDecimal exchangeRate);

    Integer getCrmAccountBalanceCount(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency);

    void setChangedAccountsForRecalculate(Integer transactionID);

    EdsBankCheckTransaction getBankCheckTransaction(EdsBankCheck bankCheck);

    List<EdsGoodsReceivedTransaction> getTransactionByPurchaseOrder(EdsPurchaseOrder purchaseOrder);

    List<EdsGoodsReceivedTransaction> getTransactionsyPurchaseOrderId(Integer purchaseOrderId);

    List<Integer> getTransactionIdsByPurchaseOrderId(Integer purchaseOrderId);

    List<EdsGoodsDeliveredTransaction> getTransactionBySaleOrderId(Integer saleOrderId);

    EdsDepreciationTransaction getDepreciationTransaction(EdsDepreciation depreciation);

    EdsManualTransaction getTransactionByManualJournal(EdsManualJournal manualJournal);

    List<EdsManualTransaction> getTransactionsByManualJournal(EdsManualJournal manualJournal);

    BigDecimal getCompanyIncome(ListingFilterParameter filterParameter);

    EdsCusSuppPaymentTransaction getTransaction(Integer transactionID);

    Number getTransactionCount();

    List<EdsInventoryTransaction> getInventoryTransactions(Integer productID);

    EdsBankTransaction getBankTransactionByMoneyTransfer(Integer bankMoneyTransferID);

    EdsBillOfEntryTransaction getBillOfEntryTransaction(Integer billOfEntryID);

    EdsBankTransaction getBankAccountOpeningBalanceTransaction(Integer bankAccountID);

    void deleteIncomeTaxTransactionsByPeriod(Date startDate, Date endDate);

    BigDecimal getPNLProfitForIncomeTax(Date startDate, Date endDate);

    EdsSinglePayrunTransaction getTransactionByPayrun(Integer payrunID);

    EdsCashAdvanceTransaction getTransactionByCashAdvance(Integer cashAdvanceID);

    EdsPayrunPaymentTransaction getTransactionByPayrunPayment(Integer paymentItemID);

    EdsPayrollPaymentTransaction getTransactionByPayrollPayment(Integer paymentItemID);

    BigDecimal getCashAdvanceTransctionPaidAmount(ListingFilterParameter lfp);

    Long getTransactionItemCount(ListingFilterParameter filterParameter);

    BigDecimal getBankAccountLastExchangeRate(Integer accountID);

    BigDecimal getBankAccountLastExchangeRate(Integer accountID, Date date);

    void deleteCustomerSupplierPaymentTransaction(Integer customerSupplierPaymentID);

    EdsTransaction getTransactionByBankStatementItemID(Integer bankStatementItemID);

    EdsTransaction getTransactionByJournalID(Integer journalId);

    EdsTransactionItem getPaymentTransactionByStatementItem(FindMatchFilterData filterData);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    ArrayList<SelectItem> getTransactionJournals(ListingFilterParameter fp);

    EdsDepreciationTransaction getTransactionByDepreciation(EdsDepreciation depreciation);

    EdsCashAdvancePayTransaction getCashAdvancePayTransactionByPayslipPayment(EdsPayslipPayments payslipPayment);

    List<Integer> getTransactionIdsByShippingData(EdsShippingData shippingData);

    List<Integer> getTransactionIdsByShippings(List<Integer> shippingIds);

    BigDecimal getGrnVATTotal(List<Integer> grnIds);

    List<Integer> getTransactionIdsByAdjustments(List<Integer> adjustmentIds);

    List<EdsGoodsReceivedTransaction> getTransactionsByShippingData(EdsShippingData shippingData);

    List<EdsGoodsReceivedTransaction> getGoodsReceivedTransactionByShippingData(EdsShippingData shippingData);

    List<EdsBankTransferTransaction> getBankTransferTransaction(EdsBankTransfer bankTransfer);

    List<EdsGoodsReceivedTransaction> getTransactionsByShippingData(EdsShippingData shippingData, boolean evenDeleted);

    List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionsByShippingData(EdsShippingData shippingData);

    List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionByShippingData(EdsShippingData shippingData);

    List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionsByShippingData(EdsShippingData shippingData, boolean evenDeleted);

    EdsTransaction getTransactionByVatReturnId(Integer vatReturnId);

    EdsTransaction getTransactionByReversedId(Integer reversedId);

    EdsTransaction getTransactionByVatAdjustmentId(Integer vatAdjustmentId);

    void deleteDeferredTransaction(DeferredTransactionType type, Integer objectId);

    void deleteMJTransactionItemsByIds(String ids);

    List<Object[]> getIncorrectInventoryTransactions();

    void deleteTransactionsByPayslip(Integer payslipId);

    void deleteTransactionsByPayslipTable(Integer payslipId);

    void deleteTransactionsByPayrun(Integer payrunId);

    void deleteTransactionsByPayrunPayment(Integer paymentItemId);

    void deleteTransactionsByPayrollPayment(Integer paymentItemId);
}
