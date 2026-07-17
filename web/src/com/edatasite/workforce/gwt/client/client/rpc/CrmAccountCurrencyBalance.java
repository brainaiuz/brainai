package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 3/28/14
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountCurrencyBalance implements IsSerializable{
    private CurrencyItem baseCurrency;
    private CurrencyItem currency;
    private CrmAccountBalanceItem[] items;
    private BigDecimal earlyBalanceInBase;
    private BigDecimal earlyBalance;
    private BigDecimal endingBalanceInBase;
    private BigDecimal endingBalance;

    public CrmAccountCurrencyBalance() {
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public CrmAccountBalanceItem[] getItems() {
        return items;
    }

    public void setItems(CrmAccountBalanceItem[] items) {
        this.items = items;
    }

    public BigDecimal getEarlyBalance() {
        return earlyBalance;
    }

    public void setEarlyBalance(BigDecimal earlyBalance) {
        this.earlyBalance = earlyBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getEarlyBalanceInBase() {
        return earlyBalanceInBase;
    }

    public void setEarlyBalanceInBase(BigDecimal earlyBalanceInBase) {
        this.earlyBalanceInBase = earlyBalanceInBase;
    }

    public BigDecimal getEndingBalanceInBase() {
        return endingBalanceInBase;
    }

    public void setEndingBalanceInBase(BigDecimal endingBalanceInBase) {
        this.endingBalanceInBase = endingBalanceInBase;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
