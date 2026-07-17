package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/4/11
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyData implements IsSerializable {
    private Integer objectID;
    private BankAccountItem fromAccount;
    private BankAccountItem toAccount;
    private BigDecimal amount;
    private String reference;
    private boolean validateReference = true;
    private DateNonConvertable transferMoneyDate;

    private CurrencyItem currency;
    private Integer amountCurrencyID;
    private BigDecimal exchangeRate;
    private BigDecimal fromExchangeRate;
    private BigDecimal toExchangeRate;
    private BigDecimal baseAmount;

    public TransferMoneyData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BankAccountItem getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(BankAccountItem fromAccount) {
        this.fromAccount = fromAccount;
    }

    public BankAccountItem getToAccount() {
        return toAccount;
    }

    public void setToAccount(BankAccountItem toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
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

    public boolean isValidateReference() {
        return validateReference;
    }

    public void setValidateReference(boolean validateReference) {
        this.validateReference = validateReference;
    }

    public DateNonConvertable getTransferMoneyDate() {
        return transferMoneyDate;
    }

    public void setTransferMoneyDate(DateNonConvertable transferMoneyDate) {
        this.transferMoneyDate = transferMoneyDate;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public Integer getAmountCurrencyID() {
        return amountCurrencyID;
    }

    public void setAmountCurrencyID(Integer amountCurrencyID) {
        this.amountCurrencyID = amountCurrencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getFromExchangeRate() {
        return fromExchangeRate;
    }

    public void setFromExchangeRate(BigDecimal fromExchangeRate) {
        this.fromExchangeRate = fromExchangeRate;
    }

    public BigDecimal getToExchangeRate() {
        return toExchangeRate;
    }

    public void setToExchangeRate(BigDecimal toExchangeRate) {
        this.toExchangeRate = toExchangeRate;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }
}
