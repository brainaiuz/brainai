package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 1/4/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariationListItem implements IsSerializable {

    private Integer objectID;
    private String combinationID;
    private String name;
    private BigDecimal price;
    private BigDecimal qty;

    private boolean selected;
    private boolean existing = false;

    public VariationListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCombinationID() {
        return combinationID;
    }

    public void setCombinationID(String combinationID) {
        this.combinationID = combinationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }
}
