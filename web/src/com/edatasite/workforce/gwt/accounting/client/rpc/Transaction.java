package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.02.2009
 * Time: 14:33:06
 * To change this template use File | Settings | File Templates.
 */
public class Transaction implements IsSerializable {

    private Integer journalId;
    private String journalName;
    private String reference;
    private DateNonConvertable journalDate;
    private DateNonConvertable postedDate;
    private String manualJournalNumber;
    private String invoicePaymentNumber;
    private String fixedAssetNumber;
    private String fixedAssetName;
    private String expenseNumber;
    private String expenseTitle;
    private String stockAdjustmentNumber;
    private String productNumber;
    private String clientName;
    private String supplierName;
    private String narration;
    private String postedBy;
    private TransactionItem[] transactionItems;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    private String invoiceOrPaymentType;
    private String number;
    private String checkNumber;
    private String description; //for expenses
    private Integer keyId;
    private Integer purchaseOrderId; // TODO: 1/20/18 don't delete, don't use it.This is temporary usage, will be deleted
    private Integer saleOrderId;
    private String transactionType;
    private String paymentType;
    private Integer transactionId;
    private boolean creditNote;
    private Integer reversedJournalId;
    private int moreFound;
    private Integer spendReceiveMoneyType;
    private String spendReceiveMoneyNumber;
    private String spendReceiveMoneyNarration;
    private String status;
    private String reconcileStatus;
    private ArrayList<TransactionItem> transactionItemList;
    private String layoutHtml;
    private Integer batchPaymentId;

    //Retained Earnings
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private String transactionLink;
    private String cashAdvanceNumber;
    private String cashAdvanceStatus;
    private boolean isBlank;

    public Integer getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(Integer salesOrderId) {
        this.saleOrderId = salesOrderId;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DateNonConvertable getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(DateNonConvertable journalDate) {
        this.journalDate = journalDate;
    }

    public TransactionItem[] getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(TransactionItem[] transactionItems) {
        this.transactionItems = transactionItems;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public DateNonConvertable getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(DateNonConvertable postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getInvoiceOrPaymentType() {
        return invoiceOrPaymentType;
    }

    public void setInvoiceOrPaymentType(String invoiceOrPaymentType) {
        this.invoiceOrPaymentType = invoiceOrPaymentType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getReversedJournalId() {
        return reversedJournalId;
    }

    public void setReversedJournalId(Integer reversedJournalId) {
        this.reversedJournalId = reversedJournalId;
    }

    public int getMoreFound() {
        return moreFound;
    }

    public void setMoreFound(int moreFound) {
        this.moreFound = moreFound;
    }

    public String getReconcileStatus() {
        return reconcileStatus;
    }

    public void setReconcileStatus(String reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getFixedAssetNumber() {
        return fixedAssetNumber;
    }

    public void setFixedAssetNumber(String fixedAssetNumber) {
        this.fixedAssetNumber = fixedAssetNumber;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public boolean isCreditNote() {
        return creditNote;
    }

    public void setCreditNote(boolean creditNote) {
        this.creditNote = creditNote;
    }

    public Integer getSpendReceiveMoneyType() {
        return spendReceiveMoneyType;
    }

    public void setSpendReceiveMoneyType(Integer spendReceiveMoneyType) {
        this.spendReceiveMoneyType = spendReceiveMoneyType;
    }

    public String getSpendReceiveMoneyNumber() {
        return spendReceiveMoneyNumber;
    }

    public void setSpendReceiveMoneyNumber(String spendReceiveMoneyNumber) {
        this.spendReceiveMoneyNumber = spendReceiveMoneyNumber;
    }

    public String getSpendReceiveMoneyNarration() {
        return spendReceiveMoneyNarration;
    }

    public void setSpendReceiveMoneyNarration(String spendReceiveMoneyNarration) {
        this.spendReceiveMoneyNarration = spendReceiveMoneyNarration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<TransactionItem> getTransactionItemList() {
        return transactionItemList;
    }

    public void setTransactionItemList(ArrayList<TransactionItem> transactionItemList) {
        this.transactionItemList = transactionItemList;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public String getManualJournalNumber() {
        return manualJournalNumber;
    }

    public void setManualJournalNumber(String manualJournalNumber) {
        this.manualJournalNumber = manualJournalNumber;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getInvoicePaymentNumber() {
        return invoicePaymentNumber;
    }

    public void setInvoicePaymentNumber(String invoicePaymentNumber) {
        this.invoicePaymentNumber = invoicePaymentNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getStockAdjustmentNumber() {
        return stockAdjustmentNumber;
    }

    public void setStockAdjustmentNumber(String stockAdjustmentNumber) {
        this.stockAdjustmentNumber = stockAdjustmentNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getTransactionLink() {
        return transactionLink;
    }

    public void setTransactionLink(String transactionLink) {
        this.transactionLink = transactionLink;
    }

    public String getCashAdvanceNumber() {
        return cashAdvanceNumber;
    }

    public void setCashAdvanceNumber(String cashAdvanceNumber) {
        this.cashAdvanceNumber = cashAdvanceNumber;
    }

    public String getCashAdvanceStatus() {
        return cashAdvanceStatus;
    }

    public void setCashAdvanceStatus(String cashAdvanceStatus) {
        this.cashAdvanceStatus = cashAdvanceStatus;
    }

    public boolean isBlank() {
        return isBlank;
    }

    public void setBlank(boolean isBlank) {
        this.isBlank = isBlank;
    }

    public Integer getBatchPaymentId() {
        return batchPaymentId;
    }

    public void setBatchPaymentId(Integer batchPaymentId) {
        this.batchPaymentId = batchPaymentId;
    }
}
