package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 24, 2011
 * Time: 3:05:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryStockValuationItem implements IsSerializable {

    private Integer transactionType;
    private DateNonConvertable entryDate;
    private DateNonConvertable transactionDate;
    private String name;
    private String number;
    private String shippingDataNumber;
    private BigDecimal qty;
    private BigDecimal transactionValue;
    private Integer itemId;
    private Integer shippingDataId;
    private String quantityPerPriceList;
    private String priceListWithoutScaling;

    public InventoryStockValuationItem() {
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public DateNonConvertable getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(DateNonConvertable entryDate) {
        this.entryDate = entryDate;
    }

    public DateNonConvertable getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(DateNonConvertable transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getQuantityPerPriceList() {
        return quantityPerPriceList;
    }

    public void setQuantityPerPriceList(String quantityPerPriceList) {
        this.quantityPerPriceList = quantityPerPriceList;
    }

    public Integer getShippingDataId() {
        return shippingDataId;
    }

    public void setShippingDataId(Integer shippingDataId) {
        this.shippingDataId = shippingDataId;
    }

    public String getShippingDataNumber() {
        return shippingDataNumber;
    }

    public void setShippingDataNumber(String shippingDataNumber) {
        this.shippingDataNumber = shippingDataNumber;
    }

    public String getPriceListWithoutScaling() {
        return priceListWithoutScaling;
    }

    public void setPriceListWithoutScaling(String priceListWithoutScaling) {
        this.priceListWithoutScaling = priceListWithoutScaling;
    }
}
