package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Azam
 * Date: 10/18/22
 * Time: 08:44 AM
 */
public class CustomPurchaseOrderImportItem implements IsSerializable{
    private Integer id;
    private Integer poNumber;
    private Integer poDate;
    private Integer poValidDate;
    private Integer supplierNumber;
    private Integer currency;
    private Integer exchangeRate;
    private Integer itemNumber;
    private Integer quantity;
    private Integer price;
    private Integer taxRate;
    private Integer account;

    public CustomPurchaseOrderImportItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(Integer poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getPoDate() {
        return poDate;
    }

    public void setPoDate(Integer poDate) {
        this.poDate = poDate;
    }

    public Integer getPoValidDate() {
        return poValidDate;
    }

    public void setPoValidDate(Integer poValidDate) {
        this.poValidDate = poValidDate;
    }

    public Integer getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(Integer supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }
}
