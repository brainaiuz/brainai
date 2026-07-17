package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/8/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryStockData implements IsSerializable{
    private SelectItem warehouse;
    private InventoryStockValuation[] stockValuations;
    private int totalCount;
    private BigDecimal beginningBalance;
    private BigDecimal endingBalance;
    private SelectItem currency;

    public InventoryStockData() {
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public InventoryStockValuation[] getStockValuations() {
        return stockValuations;
    }

    public void setStockValuations(InventoryStockValuation[] stockValuations) {
        this.stockValuations = stockValuations;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount(){
        return this.totalCount;
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }
}
