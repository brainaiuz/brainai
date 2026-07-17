package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dilsh0d on 11/3/2017.
 */
public class ItemTO extends ResponseData {
    @Schema(required = true)
    private Integer item_id;
    private String parent_item_name;//Zapier need this
    private String item_name;
    private String item_number;
    private String product_type;
    private String sku;
    private String description;
    private BigDecimal quantity;
    private SupplierTO supplier;

    // Rental Items
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date toDate;

    public ItemTO() {
    }

    public ItemTO(Integer item_id, String item_name, String item_number, String product_type) {
        this(item_id, item_name, item_number);
        this.product_type = product_type;
    }

    public ItemTO(Integer item_id, String item_name, String item_number) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_number = item_number;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getParent_item_name() {
        return parent_item_name;
    }

    public void setParent_item_name(String parent_item_name) {
        this.parent_item_name = parent_item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public SupplierTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierTO supplier) {
        this.supplier = supplier;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
