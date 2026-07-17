package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BillableExpenseItem implements Serializable {

    public static final String EXPENSE = "EXPENSE";
    public static final String PURCHASE_AS_EXPENSE = "PURCHASE_AS_EXPENSE";
    public static final String BANK_TRANSFER_AS_EXPENSE = "BANK_TRANSFER_AS_EXPENSE";
    public static final String MANUAL_TRANSACTION_AS_EXPENSE = "MANUAL_TRANSACTION_AS_EXPENSE";
    public static final String CHECK_AS_EXPENSE = "CHECK_AS_EXPENSE";

    private Integer objectID;
    private String number;
    private String type;
    private Integer bankTransferType;
    private String description;
    private SelectItem client;
    private SelectItem account;

    private BigDecimal amountInBase;
    private BigDecimal amountInCurrency;
    private Integer currencyID;

    private BigDecimal markupAmount;
    private BigDecimal markupAmountInBase;

    private BigDecimal markupTaxAmount;
    private BigDecimal markupTaxAmountInBase;

    private SelectItem markupAccount;
    private TaxItem markupTax;
    private Date date;

    private boolean selected;
    private String currencyName;
    private Integer invoiceId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public BigDecimal getAmountInBase() {
        return amountInBase;
    }

    public void setAmountInBase(BigDecimal amountInBase) {
        this.amountInBase = amountInBase;
    }

    public BigDecimal getAmountInCurrency() {
        return amountInCurrency;
    }

    public void setAmountInCurrency(BigDecimal amountInCurrency) {
        this.amountInCurrency = amountInCurrency;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount != null ? markupAmount : BigDecimal.ZERO;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public BigDecimal getMarkupAmountInBase() {
        return markupAmountInBase != null ? markupAmountInBase : BigDecimal.ZERO;
    }

    public void setMarkupAmountInBase(BigDecimal markupAmountInBase) {
        this.markupAmountInBase = markupAmountInBase;
    }

    public SelectItem getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(SelectItem markupAccount) {
        this.markupAccount = markupAccount;
    }

    public BigDecimal getMarkupTaxAmount() {
        return markupTaxAmount != null ? markupTaxAmount : BigDecimal.ZERO;
    }

    public void setMarkupTaxAmount(BigDecimal markupTaxAmount) {
        this.markupTaxAmount = markupTaxAmount;
    }

    public TaxItem getMarkupTax() {
        return markupTax;
    }

    public void setMarkupTax(TaxItem markupTax) {
        this.markupTax = markupTax;
    }

    public BigDecimal getMarkupTaxAmountInBase() {
        return markupTaxAmountInBase;
    }

    public void setMarkupTaxAmountInBase(BigDecimal markupTaxAmountInBase) {
        this.markupTaxAmountInBase = markupTaxAmountInBase;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateSort() {
        return getDate() != null ? getDate() : new Date();
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getBankTransferType() {
        return bankTransferType;
    }

    public void setBankTransferType(Integer bankTransferType) {
        this.bankTransferType = bankTransferType;
    }
}
