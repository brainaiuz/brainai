package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov.
 */
public class MultiPriceItemTO implements IsSerializable {
    SelectItemTO currency;
    BigDecimal price;
    String type;

    public MultiPriceItemTO() {

    }

    public MultiPriceItemTO(MultiPriceItem item) {
        this.currency = new SelectItemTO(item.getCurrency());
        this.price = item.getPrice();
        this.type = item.getType();
    }

    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
