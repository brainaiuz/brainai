package com.edatasite.workforce.rest.v3.release10.core.to.accounting;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created By : Dilsh0d Madrahimov on 9/30/2019 2:52 PM
 */
public class PickListItemDTO extends ResponseData {
    private Integer id;
    private Integer product_id;
    private String product_name;
    private String product_number;
    private String reference;
    private BigDecimal shipping_qty;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getShipping_qty() {
        return shipping_qty;
    }

    public void setShipping_qty(BigDecimal shipping_qty) {
        this.shipping_qty = shipping_qty;
    }
}
