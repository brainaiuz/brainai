package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 4:58:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountItem extends SelectItem {
    public static final String CODE_COLUMN = "code";
    public static final String NAME_COLUMN = "name";
    public static final String TYPE_COLUMN = "type";

    private Integer type;
    private Boolean active;
    private BigDecimal percentage;
    private BigDecimal fixedAmount;

    private String currencySymbol;

    private DiscountAppliesItem[] productList;

    private DiscountMultiRangeItem[] multiRangeItems;
    private Integer multiRangeDiscountType;

    private Integer[] appliedProductIDs;
    private SelectItem[] appliedClients;

    public DiscountItem() {
    }

    public DiscountItem(Integer id, String name) {
        super(id, name);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public DiscountMultiRangeItem[] getMultiRangeItems() {
        return multiRangeItems;
    }

    public void setMultiRangeItems(DiscountMultiRangeItem[] multiRangeItems) {
        this.multiRangeItems = multiRangeItems;
    }

    public DiscountAppliesItem[] getProductList() {
        return productList;
    }

    public void setProductList(DiscountAppliesItem[] productList) {
        this.productList = productList;
    }

    public Integer[] getAppliedProductIDs() {
        return appliedProductIDs;
    }

    public void setAppliedProductIDs(Integer[] appliedProductIDs) {
        this.appliedProductIDs = appliedProductIDs;
    }

    public SelectItem[] getAppliedClients() {
        return appliedClients;
    }

    public void setAppliedClients(SelectItem[] appliedClients) {
        this.appliedClients = appliedClients;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getMultiRangeDiscountType() {
        return multiRangeDiscountType;
    }

    public void setMultiRangeDiscountType(Integer multiRangeDiscountType) {
        this.multiRangeDiscountType = multiRangeDiscountType;
    }
}
