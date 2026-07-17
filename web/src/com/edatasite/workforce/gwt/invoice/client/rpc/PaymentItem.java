package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 10.03.2009
 * Time: 16:18:07
 * To change this template use File | Settings | File Templates.
 */
public class PaymentItem implements Serializable {
    public static final int CASH_REFUND = 1;
    public static final int PREPAYMENT = 2;
    public static final int SUPPLIER_CREDIT = 3;
    private Integer objectId;
    private String statusText;
    private DateNonConvertable date;
    private String user;
    private BigDecimal amount;
    private String reference;
    private String paidTo;
    private Integer paidToID;
    private String type;
    private Integer appliedPaymentID;
    private ArrayList<PaymentItem> appliedPayments;
    private BigDecimal appliedPaymentAmount;
    private ArrayList<PaymentItem> refundPayments;
    private BigDecimal refundPaymentAmount;
    private BigDecimal closeAmount;

    private SelectItem invoice;
    private SelectItem expense;
    private SelectItem creditNote;
    private SelectItem crmAccount;
    private SelectItem project;
    private SelectItem department;
    private SelectItem receivablePayable;
    private SelectItem bankFee;
    private String bankFeeType;
    private BigDecimal bankFeeValue;
    private SelectItem saleQuoteItem;
    private SelectItem saleInvoiceItem;
    private SelectItem purchaseOrderItem;
    private SelectItem rentalOrderItem;

    private Date invoiceDate;
    private DateNonConvertable invoiceDueDate;
    private BigDecimal invoiceTotal;
    private String invoiceType;
    private String note;
    private String number;
    private boolean isReversed;

    private Integer batchPaymentID;

    private SelectItem currency;
    private BigDecimal exchangeRate;
    private SelectItem[] templates;
    private BigDecimal supplierCustomerBalance;
    private Integer journalID;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private Integer calcScale;

    public PaymentItem() {

    }

    public PaymentItem(String user) {
        this.user = user;
    }

    public PaymentItem(Integer objectId, DateNonConvertable date, BigDecimal amount) {
        this.objectId = objectId;
        this.date = date;
        this.amount = amount;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public Integer getPaidToID() {
        return paidToID;
    }

    public void setPaidToID(Integer paidToID) {
        this.paidToID = paidToID;
    }

    public SelectItem getInvoice() {
        return invoice;
    }

    public void setInvoice(SelectItem invoice) {
        this.invoice = invoice;
    }

    public SelectItem getExpense() {
        return this.expense;
    }

    public void setExpense(final SelectItem expense) {
        this.expense = expense;
    }

    public SelectItem getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(SelectItem creditNote) {
        this.creditNote = creditNote;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(SelectItem receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public SelectItem getBankFee() {
        return bankFee;
    }

    public void setBankFee(SelectItem bankFee) {
        this.bankFee = bankFee;
    }

    public String getBankFeeType() {
        return bankFeeType;
    }

    public void setBankFeeType(String bankFeeType) {
        this.bankFeeType = bankFeeType;
    }

    public BigDecimal getBankFeeValue() {
        return bankFeeValue;
    }

    public void setBankFeeValue(BigDecimal bankFeeValue) {
        this.bankFeeValue = bankFeeValue;
    }

    public SelectItem getSaleQuoteItem() {
        return saleQuoteItem;
    }

    public void setSaleQuoteItem(SelectItem saleQuoteItem) {
        this.saleQuoteItem = saleQuoteItem;
    }

    public SelectItem getSaleInvoiceItem() {
        return saleInvoiceItem;
    }

    public void setSaleInvoiceItem(SelectItem saleInvoiceItem) {
        this.saleInvoiceItem = saleInvoiceItem;
    }

    public SelectItem getPurchaseOrderItem() {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(SelectItem purchaseOrderItem) {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public SelectItem getRentalOrderItem() {
        return rentalOrderItem;
    }

    public void setRentalOrderItem(SelectItem rentalOrderItem) {
        this.rentalOrderItem = rentalOrderItem;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDueDate() {
        return invoiceDueDate != null ? invoiceDueDate.getNonConvertedDate() : null;
    }

    public void setInvoiceDueDate(Date invoiceDueDate) {
        this.invoiceDueDate = new DateNonConvertable(invoiceDueDate);
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(BigDecimal invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public Integer getBatchPaymentID() {
        return batchPaymentID;
    }

    public void setBatchPaymentID(Integer batchPaymentID) {
        this.batchPaymentID = batchPaymentID;
    }

    public boolean isInvoiceCreditNoteAllocation() {
        return invoice != null && creditNote != null;
    }

    public Integer getAppliedPaymentID() {
        return appliedPaymentID;
    }

    public void setAppliedPaymentID(Integer appliedPaymentID) {
        this.appliedPaymentID = appliedPaymentID;
    }

    public ArrayList<PaymentItem> getAppliedPayments() {
        return appliedPayments;
    }

    public void setAppliedPayments(ArrayList<PaymentItem> appliedPayments) {
        this.appliedPayments = appliedPayments;
    }

    public BigDecimal getAppliedPaymentAmount() {
        return appliedPaymentAmount != null ? appliedPaymentAmount : BigDecimal.ZERO;
    }

    public void setAppliedPaymentAmount(BigDecimal appliedPaymentAmount) {
        this.appliedPaymentAmount = appliedPaymentAmount;
    }

    public BigDecimal getRemainingBalance() {
        BigDecimal remainingAmount = getAmount();
        remainingAmount = remainingAmount.subtract(getAppliedPaymentAmount());
        remainingAmount = remainingAmount.subtract(getRefundPaymentAmount());

        return remainingAmount;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getSupplierCustomerBalance() {
        return supplierCustomerBalance;
    }

    public void setSupplierCustomerBalance(BigDecimal supplierCustomerBalance) {
        this.supplierCustomerBalance = supplierCustomerBalance;
    }

    public Integer getJournalID() {
        return this.journalID;
    }

    public void setJournalID(final Integer journalID) {
        this.journalID = journalID;
    }

    public ArrayList<PaymentItem> getRefundPayments() {
        return this.refundPayments;
    }

    public void setRefundPayments(final ArrayList<PaymentItem> refundPayments) {
        this.refundPayments = refundPayments;
    }

    public BigDecimal getRefundPaymentAmount() {
        return this.refundPaymentAmount != null ? this.refundPaymentAmount : BigDecimal.ZERO;
    }

    public void setRefundPaymentAmount(final BigDecimal refundPaymentAmount) {
        this.refundPaymentAmount = refundPaymentAmount;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public BigDecimal getCloseAmount() {
        return this.closeAmount;
    }

    public void setCloseAmount(final BigDecimal closeAmount) {
        this.closeAmount = closeAmount;
    }

    public Integer getCalcScale() {
        return calcScale;
    }

    public void setCalcScale(Integer calcScale) {
        this.calcScale = calcScale;
    }
}
