package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/18/11
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalanceItem implements IsSerializable, AccountingConstants {
    private Integer objectID;
    private Integer reversalID;
    private String number;
    private String reference;
    private String paymentInvoiceNumber;
    private String paymentNumber;
    private String paymentType;
    private String transactionType;
    private String narration;
    private Integer invoiceID;
    private Integer paymentID;
    private Integer manualJournalId;
    private Boolean creditNote;
    private Boolean refund;
    private Date date;
    private Date dueDate;
    private DateNonConvertable date_nc;
    private BigDecimal amountInBase;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal debit;
    private BigDecimal credit;
    private Integer itemId;
    private String clientSupplierName;
    private Integer batchPaymentId;

    public String getTransactionLabel() {
        StringBuilder str = new StringBuilder();
        if (Constants.INVOICE_TRANSACTION.equals(transactionType)) {
            str.append(reversalID != null ? "Reversed Invoice: " + number : ((isCreditNote() ? "Credit Note: " : "Invoice: ") + number));
        } else if (Constants.INVOICEPAYMENT_TRANSACTION.equals(transactionType)) {
            if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(paymentType)) {
                str.append(narration != null ? narration : "PrePayment. ");
            } else if (AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(paymentType)) {
                str.append(narration != null ? narration : "Supplier Credit. ");
            } else {
                if (reversalID != null) {
                    str.append("Reversed Payment");
                } else if (AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE.equals(paymentType)) {
                    str.append("Applied Prepayment Share. ");
                } else if (AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE.equals(paymentType)) {
                    str.append("Applied Supplier Credit Share. ");
                } else {
                    str.append(isRefund() ? "Refund. " : "Payment. ");
                    if (paymentNumber != null && !"".equals(paymentNumber.trim())) {
                        str.append(paymentNumber);
                    }
                }
                if (paymentInvoiceNumber != null && !"".equals(paymentInvoiceNumber.trim())) {
                    str.append(" Invoice:" + paymentInvoiceNumber);
                }
            }
        } else if (Constants.CUSTOMER_TRANSACTION.equals(transactionType) || Constants.SUPPLIER_TRANSACTION.equals(transactionType)) {
            str.append("Opening Balance");
        } else if (Constants.CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(transactionType)) {
            str.append(manualJournalId == null ? "Opening Balance Payment" : "Payment for Manual Transaction. Narration: " + getNarration());
        } else if (Constants.MANUAL_TRANSACTION.equals(transactionType)) {
            str.append(getNarration());
        } else if (Constants.BANK_CHECK_TRANSACTION.equals(transactionType)) {
            str.append("Bank Check Transaction");
        } else if ("EdsBankTransferTransaction".equals(transactionType) && narration.startsWith("Cash Receipt")) {
            str.append("Cash Receipt");
        } else if ("EdsBankTransferTransaction".equals(transactionType) && narration.startsWith("Cash Payment")) {
            str.append("Cash Payment ");
        } else if ("EdsBankTransferTransaction".equals(transactionType) && narration.startsWith("Bank Receipt")) {
            str.append("Bank Receipt");
        } else if ("EdsBankTransferTransaction".equals(transactionType) && narration.startsWith("Bank Payment")) {
            str.append("Bank Payment");
        } else if ("EdsBankTransferTransaction".equals(transactionType)) {
            str.append("Bank Transfer Transaction");
        } else if (Constants.EXPENSE_TRANSACTION.equals(transactionType)) {
            str.append((reversalID != null ? "Reversed Expense" : "Expense") + " " + number);
        } else if (Constants.EXPENSEPAYMENT_TRANSACTION.equals(transactionType)) {
            str.append(reversalID != null ? "Reversed Expense Payment" : "Expense Payment");
        }
        return str.toString();
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getReversalID() {
        return reversalID;
    }

    public void setReversalID(Integer reversalID) {
        this.reversalID = reversalID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPaymentInvoiceNumber() {
        return paymentInvoiceNumber;
    }

    public void setPaymentInvoiceNumber(String paymentInvoiceNumber) {
        this.paymentInvoiceNumber = paymentInvoiceNumber;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public Boolean isCreditNote() {
        return creditNote != null ? creditNote : false;
    }

    public void setCreditNote(Boolean creditNote) {
        this.creditNote = creditNote;
    }

    public Boolean isRefund() {
        return refund != null ? refund : false;
    }

    public void setRefund(Boolean refund) {
        this.refund = refund;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public DateNonConvertable getDate_nc() {
        return date_nc;
    }

    public void setDate_nc(DateNonConvertable date_nc) {
        this.date_nc = date_nc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        if (this.balance == null)
            return new BigDecimal(0);
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getManualJournalId() {
        return manualJournalId;
    }

    public void setManualJournalId(Integer manualJournalId) {
        this.manualJournalId = manualJournalId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getClientSupplierName() {
        return clientSupplierName;
    }

    public void setClientSupplierName(String clientSupplierName) {
        this.clientSupplierName = clientSupplierName;
    }

    public Integer getBatchPaymentId() {
        return batchPaymentId;
    }

    public void setBatchPaymentId(Integer batchPaymentId) {
        this.batchPaymentId = batchPaymentId;
    }

    public BigDecimal getAmountInBase() {
        return amountInBase;
    }

    public void setAmountInBase(BigDecimal amountInBase) {
        this.amountInBase = amountInBase;
    }
}
