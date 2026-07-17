package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:42:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingMethod extends SelectItem {

    public static String PRICE = "price";
    public static String DESCRIPTION = "description";
    public static String NAME = "name";
    public static String ACTION = "action";
    public static String TAXRATE = "taxrate";
    private BigDecimal price;
    private TaxItem taxItem;
    private SelectItem account;
    private SelectItem[] appliedClients;
    private Integer currencyId;
    private BigDecimal exchangeRate;
    private boolean priceChanged;

    public ShippingMethod() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public SelectItem[] getAppliedClients() {
        return appliedClients;
    }

    public void setAppliedClients(SelectItem[] appliedClients) {
        this.appliedClients = appliedClients;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isPriceChanged() {
        return priceChanged;
    }

    public void setPriceChanged(boolean priceChanged) {
        this.priceChanged = priceChanged;
    }
}
