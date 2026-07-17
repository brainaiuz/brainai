package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 23, 2009
 * Time: 3:12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpensePaymentData implements Serializable {

    //VALIDATION STATUS
    public static final Integer REFERENCE_EXIST = -1;

    private Integer objectID;
    private Integer reportId;
    private BigDecimal paymentAmount;
    private BigDecimal oldPaymentAmount;
    private DateNonConvertable date;
    private SelectItem paymentAccount;
    private String paymentType;
    private String referenceNumber;
    private boolean validateReference = true;
    private Integer batchPaymentID;
    private String title;
    private Date expenseDate;
    private String numberData;
    private BigDecimal totalExpenseAmount;
    private BigDecimal totalExpenseAmountinBase;
    private BigDecimal totalPaymentAmountForEdit;
    private BigDecimal exchangeRate;
    private SelectItem supplier;
    private CurrencyItem currency;
    private CurrencyItem baseCurrency;
    private CurrencyItem expenseCurrency;
    private String status;
    private BigDecimal paymentAmountInExpenseCurrency;
    private String layoutHTML;
    private FileResource[] attachments;
    private Integer journalId;
    private boolean applyCredit;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getOldPaymentAmount() {
        return this.oldPaymentAmount;
    }

    public void setOldPaymentAmount(final BigDecimal oldPaymentAmount) {
        this.oldPaymentAmount = oldPaymentAmount;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(SelectItem paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean isValidateReference() {
        return validateReference;
    }

    public void setValidateReference(boolean validateReference) {
        this.validateReference = validateReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public BigDecimal getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public BigDecimal getTotalPaymentAmountForEdit() {
        return this.totalPaymentAmountForEdit;
    }

    public void setTotalPaymentAmountForEdit(final BigDecimal totalPaymentAmountForEdit) {
        this.totalPaymentAmountForEdit = totalPaymentAmountForEdit;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public SelectItem getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItem supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBatchPaymentID() {
        return batchPaymentID;
    }

    public void setBatchPaymentID(Integer batchPaymentID) {
        this.batchPaymentID = batchPaymentID;
    }

    public BigDecimal getPaymentAmountInExpenseCurrency() {
        return paymentAmountInExpenseCurrency;
    }

    public void setPaymentAmountInExpenseCurrency(BigDecimal paymentAmountInExpenseCurrency) {
        this.paymentAmountInExpenseCurrency = paymentAmountInExpenseCurrency;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public FileResource[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileResource[] attachments) {
        this.attachments = attachments;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public CurrencyItem getCurrency() {
        return this.currency;
    }

    public void setCurrency(final CurrencyItem currency) {
        this.currency = currency;
    }

    public CurrencyItem getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(final CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getTotalExpenseAmountinBase() {
        return this.totalExpenseAmountinBase;
    }

    public void setTotalExpenseAmountinBase(final BigDecimal totalExpenseAmountinBase) {
        this.totalExpenseAmountinBase = totalExpenseAmountinBase;
    }

    public SelectItem getExpenseCurrency() {
        return this.expenseCurrency;
    }

    public void setExpenseCurrency(final CurrencyItem expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public boolean isApplyCredit() {
        return this.applyCredit;
    }

    public void setApplyCredit(final boolean applyCredit) {
        this.applyCredit = applyCredit;
    }
}
