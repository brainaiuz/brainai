package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 3, 2011
 * Time: 6:55:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockItem implements IsSerializable {
    private Integer id;
    private Integer itemID;
    private Integer transactionID;
    private Integer warehouseID;
    private Date date;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal transactionValue;
    private Integer order;
    private String quantityPerPriceList;
    private String priceListWithoutScaling;
    private boolean exclude;

    private String transactionCode;
    private String warehouseName;


    public StockItem() {
    }

    public StockItem(BigDecimal quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public StockItem(Integer item_stock_id,Integer warehouseID, Date date, BigDecimal quantity, BigDecimal price, Integer order) {
        this.id = item_stock_id;
        this.warehouseID = warehouseID;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }

    public StockItem(Integer transactionID, BigDecimal quantity, BigDecimal transactionValue) {
        this.transactionID = transactionID;
        this.quantity = quantity;
        this.transactionValue = transactionValue;
    }

    public StockItem(Integer transactionID, BigDecimal quantity, BigDecimal transactionValue, String transactionCode) {
        this.transactionID = transactionID;
        this.quantity = quantity;
        this.transactionValue = transactionValue;
        this.transactionCode = transactionCode;
    }

    public StockItem(Integer transactionID, Integer itemID, BigDecimal quantity, BigDecimal transactionValue, String transactionCode) {
        this.transactionID = transactionID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.transactionValue = transactionValue;
        this.transactionCode = transactionCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getQuantityPerPriceList() {
        return quantityPerPriceList;
    }

    public void setQuantityPerPriceList(String quantityPerPriceList) {
        this.quantityPerPriceList = quantityPerPriceList;
    }

    public String getPriceListWithoutScaling() {
        return priceListWithoutScaling;
    }

    public void setPriceListWithoutScaling(String priceListWithoutScaling) {
        this.priceListWithoutScaling = priceListWithoutScaling;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
