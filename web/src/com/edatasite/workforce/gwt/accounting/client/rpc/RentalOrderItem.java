package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RentalOrderItem implements Serializable {

    private Integer objectID;
    private Integer orderID;
    private ProductSelectItem rentalItem;
    private ProductSelectItem productItem;
    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal taxAmount;
    private BigDecimal netAmount;
    private BigDecimal subTotal;
    private String description;
    private TaxItem taxItem;
    private String rentalDuration;
    private Date fromDate;
    private Date toDate;
    private String statusCode;
    private SelectItem productCategory;
    private SelectItem productBrand;


    public RentalOrderItem() {
    }

    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getOrderID() {
        return this.orderID;
    }

    public void setOrderID(final Integer orderID) {
        this.orderID = orderID;
    }

    public ProductSelectItem getRentalItem() {
        return this.rentalItem;
    }

    public void setRentalItem(final ProductSelectItem item) {
        this.rentalItem = item;
    }

    public ProductSelectItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductSelectItem productItem) {
        this.productItem = productItem;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(final BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getNetAmount() {
        return this.netAmount;
    }

    public void setNetAmount(final BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public TaxItem getTaxItem() {
        return this.taxItem;
    }

    public void setTaxItem(final TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(final BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public SelectItem getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(SelectItem productCategory) {
        this.productCategory = productCategory;
    }

    public SelectItem getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(SelectItem productBrand) {
        this.productBrand = productBrand;
    }

    public String getRentalDuration() {
        return rentalDuration;
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

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}