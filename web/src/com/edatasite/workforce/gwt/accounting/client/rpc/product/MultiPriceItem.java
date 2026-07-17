package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/26/16
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiPriceItem implements IsSerializable, Serializable {

    private SelectItem currency;
    private BigDecimal price;
    private String type;

    public MultiPriceItem() {

    }

    public MultiPriceItem(SelectItem currency, BigDecimal price, String type){
        this.currency = currency;
        this.price = price;
        this.type = type;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
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
