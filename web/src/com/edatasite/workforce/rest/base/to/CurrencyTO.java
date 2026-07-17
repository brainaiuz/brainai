package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov on 03.02.2015.
 */
public class CurrencyTO extends SelectItemTO {

    BigDecimal exchangeRate;
    Long date;
    CurrencyTO currency;
    CurrencyTO baseCurrency;

    public CurrencyTO() {
        super();
    }

    public CurrencyTO(CurrencyItem item) {
        super();
        this.id = item.getId();
        this.name = item.getName();
        this.code = item.getSymbol();
        this.description = item.getDescription();
    }

    public CurrencyTO(SelectItem item) {
        super();
        this.id = item.getId();
        this.name = item.getName();
        this.code = item.getCode();
        this.description = item.getDescription();
    }

    public CurrencyTO(Integer id, String name) {
        super(id, name);
    }

    public CurrencyTO(Integer id, String name, String code) {
        super(id, name, code, "");
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public CurrencyTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}