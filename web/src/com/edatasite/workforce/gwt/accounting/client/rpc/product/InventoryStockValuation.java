package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 24, 2011
 * Time: 3:01:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryStockValuation implements IsSerializable {

    private String name;
    private String productCode;
    private BigDecimal beginningQty;
    private BigDecimal beginningBalance;
    private InventoryStockValuationItem[] stockValuationItems;

    public InventoryStockValuation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getBeginningQty() {
        return beginningQty;
    }

    public void setBeginningQty(BigDecimal beginningQty) {
        this.beginningQty = beginningQty;
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public InventoryStockValuationItem[] getStockValuationItems() {
        return stockValuationItems;
    }

    public void setStockValuationItems(InventoryStockValuationItem[] stockValuationItems) {
        this.stockValuationItems = stockValuationItems;
    }
}
