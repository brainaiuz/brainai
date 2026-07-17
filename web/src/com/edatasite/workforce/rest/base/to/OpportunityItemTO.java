package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountTypeEnum;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d Madrahimov on 31.11.2016.
 */
public class OpportunityItemTO implements IsSerializable {
    private SelectItemTO item;
    private String description;
    private BigDecimal quantity;
    private SelectItemTO unitMeasurement;
    private BigDecimal price;
    private DiscountTO discount;
    private SelectItemTO supplier;
    private Integer opportunityId;

    public OpportunityItemTO() {
    }

    public OpportunityItemTO(OpportunityItem opportunityItem) {
        this.item = opportunityItem.getItemID() != null ? new SelectItemTO(opportunityItem.getItemID(), opportunityItem.getItemName(), opportunityItem.getItemNumber(), "") : null;
        this.description = opportunityItem.getDescription();
        this.quantity = opportunityItem.getQty();
        this.unitMeasurement = WrapUtils.wrapSelectItemTO(opportunityItem.getUnitMeasurement());
        this.price = opportunityItem.getPrice();
        this.discount = new DiscountTO();
        if (opportunityItem.getDiscountItemID() != null) {
            this.discount.setId(opportunityItem.getDiscountItemID());
            this.discount.setName(opportunityItem.getDiscountItemName());
        }
        if (opportunityItem.getDiscountItemFixedType() != null) {
            this.discount.setType(DiscountTypeEnum.buildWithId(opportunityItem.getDiscountItemFixedType()));
            this.discount.setName(DiscountTypeEnum.buildWithId(opportunityItem.getDiscountItemFixedType()).getName());
        }
        this.discount.setPercentage(opportunityItem.getDiscountPercent());
        this.discount.setFixedAmount(opportunityItem.getDiscountAmount());
        this.supplier = opportunityItem.getSupplierID() != null ? new SelectItemTO(opportunityItem.getSupplierID(), opportunityItem.getSupplierName()) : null;
        this.opportunityId = opportunityItem.getOpportunityID();
    }

    public OpportunityItem wrap(OpportunityItemTO itemTO) {
        if (itemTO.getItem() != null) {
            OpportunityItem item = new OpportunityItem();
            item.setItemID(itemTO.getItem().getId());
            item.setItemName(itemTO.getItem().getName());
            item.setItemName(itemTO.getItem().getCode());
            item.setDescription(itemTO.getDescription());
            item.setQty(itemTO.getQuantity());
            if (itemTO.getUnitMeasurement() != null) {
                item.setUnitMeasurement(itemTO.getUnitMeasurement().wrap(itemTO.getUnitMeasurement()));
            }
            item.setPrice(itemTO.getPrice());
            if (itemTO.getDiscount() != null) {
                item.setDiscountItemID(itemTO.getDiscount().getId());
                item.setDiscountItemName(itemTO.getDiscount().getName());
                item.setDiscountItemFixedType(itemTO.getDiscount().getType().getId());
                item.setDiscountPercent(itemTO.getDiscount().getPercentage());
                item.setDiscountAmount(itemTO.getDiscount().getFixedAmount());
            }
            if (itemTO.getSupplier() != null) {
                item.setSupplierID(itemTO.getSupplier().getId());
                item.setSupplierName(itemTO.getSupplier().getName());
            }
            return item;
        }
        return null;
    }

    public SelectItemTO getItem() {
        return item;
    }

    public void setItem(SelectItemTO item) {
        this.item = item;
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

    public SelectItemTO getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItemTO unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DiscountTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountTO discount) {
        this.discount = discount;
    }

    public SelectItemTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItemTO supplier) {
        this.supplier = supplier;
    }

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

}
