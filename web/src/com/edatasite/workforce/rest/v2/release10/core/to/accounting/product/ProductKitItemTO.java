package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

public class ProductKitItemTO extends ResponseData {

    private Integer product_kit_item_id;
    private IdNameTO product;
    private Integer product_type;
    private String description;
    private BigDecimal quantity;
    private BigDecimal sell_price = BigDecimal.ZERO;
    private BigDecimal cost_price = BigDecimal.ZERO;
    private BigDecimal sub_total = BigDecimal.ZERO;

    public Integer getProduct_kit_item_id() {
        return product_kit_item_id;
    }

    public void setProduct_kit_item_id(Integer product_kit_item_id) {
        this.product_kit_item_id = product_kit_item_id;
    }

    public IdNameTO getProduct() {
        return product;
    }

    public void setProduct(IdNameTO product) {
        this.product = product;
    }

    public Integer getProduct_type() {
        return product_type;
    }

    public void setProduct_type(Integer product_type) {
        this.product_type = product_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSell_price() {
        return sell_price;
    }

    public void setSell_price(BigDecimal sell_price) {
        this.sell_price = sell_price;
    }

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public BigDecimal getSub_total() {
        return sub_total;
    }

    public void setSub_total(BigDecimal sub_total) {
        this.sub_total = sub_total;
    }
}
