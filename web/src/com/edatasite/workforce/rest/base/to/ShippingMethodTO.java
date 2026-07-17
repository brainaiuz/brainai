package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov on 4/14/15 1:45 AM
 */
public class ShippingMethodTO implements IsSerializable {
    Integer id;
    String name;
    String description;
    TaxTO tax;
    SelectItemTO account;
    BigDecimal price;
    CurrencyTO currency;

    public ShippingMethodTO() {

    }

    public ShippingMethodTO(ShippingMethod shippingMethod) {
        this.id = shippingMethod.getId();
        this.name = shippingMethod.getName();
        this.description = shippingMethod.getDescription();
        if (shippingMethod.getTaxItem() != null) {
            this.tax = new TaxTO(shippingMethod.getTaxItem());
        }
        if (shippingMethod.getAccount() != null) {
            this.account = new SelectItemTO(shippingMethod.getAccount().getId(), shippingMethod.getAccount().getName());
        }
        this.price = shippingMethod.getPrice();
        if (shippingMethod.getCurrencyId() != null) {
            this.currency = new CurrencyTO(shippingMethod.getCurrencyId(), "");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaxTO getTax() {
        return tax;
    }

    public void setTax(TaxTO tax) {
        this.tax = tax;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }
}
