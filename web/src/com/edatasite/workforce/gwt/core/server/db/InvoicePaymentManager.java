package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface InvoicePaymentManager extends Manager<EdsInvoicePayment> {

    Double getPaymentsSum(Integer invoiceID);

    List<EdsInvoicePayment> getPayments(EdsInvoice invoice);

    List<EdsInvoicePayment> getRefunds(EdsInvoice creditNote);

    boolean isReversed(Integer paymentID);

    ListResult<PrePaymentListItem> getPrePayments(ListingFilterParameter filterParameter);

    Integer getPrePaymentCount(ListingFilterParameter filterParameter);

    List<EdsInvoicePayment> getPaymentsAll(EdsInvoice invoice);

    List<EdsInvoicePayment> getRefundsAll(EdsInvoice creditNote);

    BigDecimal getManualPaymentAmount(Integer manualJournalID, Integer crmAccountID, String type);

    BigDecimal getBankTransferPaymentAmount(Integer bankTransferID, Integer crmAccountID, String type);

    BigDecimal getAppliedCreditAmount(Integer crmAccountID, String type);

    BigDecimal getAppliedPrePaymentAmountInBase(Integer crmAccountID, Integer paymentID, String type);

    List<String> getAppliedPrepaymentsNumberFirst(Integer crmAccountID, Integer paymentID, String type);

    BigDecimal getAppliedPrePaymentAmounts(Integer crmAccountID, Integer paymentID, String paymentType, String refundType);

    BigDecimal getRefundPrePaymentAmount(Integer crmAccountID, Integer paymentID, String refundType);

    BigDecimal getAppliedPrePaymentAmount(Integer crmAccountID, Integer paymentID, String type);

    List<EdsInvoicePayment> getAppliedPrepayments(Integer crmAccountID, Integer paymentID, String type);

    List<EdsInvoicePayment> getRefundItems(Integer paymentID);

    List<EdsInvoicePayment> getExpensePaymentItems(Integer expenseID);

    boolean isAppliedItemExist();

    List<EdsInvoicePayment> getOpenPrePayments(Integer currencyId, Integer accountID, String type, String statuses);

    List<EdsInvoicePayment> getAccountPrePayments(Integer accountID, String type);

    List<EdsInvoicePayment> getAccountOldPrePayments(Integer accountID, String type);

    List<EdsInvoicePayment> getAccountPrePaymentsWithoutReversed(Integer accountID, String type, Integer projectID);

    List<EdsInvoicePayment> getSaleInvoiceRelatedPayments(Integer saleInvoiceId);

    List<EdsInvoicePayment> getPostDatedPrePayments(Date date);

    List<EdsInvoicePayment> getBatchPaymentItems(Integer batchPaymentID);

    List<EdsInvoicePayment> getAppliedPaymentItems(Integer paymentID);

    BigDecimal getBatchPaymentItems(Integer batchPaymentID, Integer exceptObjectID, boolean isInvoicePayment);

    Integer getLastIntNumberByType(String type);

    Integer getInvoicePamyentIdIfPresent(String number, String transferType);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    boolean isDuplicateReference(String refNumber, Integer objectID);

    List<PaymentItem> getInvoicePaymentsByProject(Integer projectId, Date from, Date to);

    EdsInvoicePayment getInvoiceUnderPayment(Integer objectID);

    EdsCustomerPrepaymentNote getPrepaymentNote(Integer paymentId);

    BigDecimal getInvoicePayment(Integer invoiceID);

    BigDecimal getCrmAccountTotalAmount(Integer crmAccountId, Integer invoiceId);

    List<EdsInvoicePayment> getOderPrePaymentAmount(Integer orderId);

    List<EdsInvoicePayment> getInvoicePaymentByBankTransfer(Integer bankTransferId);

    List<EdsInvoicePayment> getInvoicePaymentByManualJournal(Integer manualJournalId);

    Boolean hasCreditDebitNote(Integer invoiceId);
}
