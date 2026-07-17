package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class PayrollPayment implements IsSerializable {

    private Integer objectID;
    private Integer additionalPaymentID;

    private Integer paidFromAccountID;
    private SelectItem paidFromAccount;
    private Integer paidToAccountID;
    private SelectItem paidToAccount;
    private String reference;
    private String details;

    private DateNonConvertable paymentDate;
    private DateNonConvertable dueDate;

    private ArrayList<PayrollPaymentItem> items;
    private Integer totalItems;

    private HashMap<Integer, PayrollPaymentItem> changedItems;
    private HashMap<Integer, Boolean> deletedItems;

    private BigDecimal exchangeRate;
    private CurrencyItem currency;

    private BigDecimal amount;
    private BigDecimal amountInBase;

    private Boolean multiCurrencyEnabled;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getAdditionalPaymentID() {
        return additionalPaymentID;
    }

    public void setAdditionalPaymentID(Integer additionalPaymentID) {
        this.additionalPaymentID = additionalPaymentID;
    }

    public Integer getPaidFromAccountID() {
        return paidFromAccountID;
    }

    public void setPaidFromAccountID(Integer paidFromAccountID) {
        this.paidFromAccountID = paidFromAccountID;
    }

    public SelectItem getPaidFromAccount() {
        return paidFromAccount;
    }

    public void setPaidFromAccount(SelectItem paidFromAccount) {
        this.paidFromAccount = paidFromAccount;
    }

    public Integer getPaidToAccountID() {
        return paidToAccountID;
    }

    public void setPaidToAccountID(Integer paidToAccountID) {
        this.paidToAccountID = paidToAccountID;
    }

    public SelectItem getPaidToAccount() {
        return paidToAccount;
    }

    public void setPaidToAccount(SelectItem paidToAccount) {
        this.paidToAccount = paidToAccount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DateNonConvertable getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateNonConvertable paymentDate) {
        this.paymentDate = paymentDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public ArrayList<PayrollPaymentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<PayrollPaymentItem> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public HashMap<Integer, PayrollPaymentItem> getChangedItems() {
        return changedItems;
    }

    public void setChangedItems(HashMap<Integer, PayrollPaymentItem> changedItems) {
        this.changedItems = changedItems;
    }

    public HashMap<Integer, Boolean> getDeletedItems() {
        return deletedItems;
    }

    public void setDeletedItems(HashMap<Integer, Boolean> deletedItems) {
        this.deletedItems = deletedItems;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountInBase() {
        return amountInBase;
    }

    public void setAmountInBase(BigDecimal amountInBase) {
        this.amountInBase = amountInBase;
    }

    public Boolean getMultiCurrencyEnabled() {
        return multiCurrencyEnabled != null && multiCurrencyEnabled;
    }

    public void setMultiCurrencyEnabled(Boolean multiCurrencyEnabled) {
        this.multiCurrencyEnabled = multiCurrencyEnabled;
    }
}
