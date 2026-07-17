package com.edatasite.workforce.gwt.accounting.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/3/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProductCustomDescription implements Serializable {
    private Integer id;
    private String name;
    private BigDecimal qty;
    private BigDecimal price;

    public NewProductCustomDescription() {
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
