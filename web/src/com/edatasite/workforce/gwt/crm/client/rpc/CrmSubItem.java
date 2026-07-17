package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CrmSubItem implements IsSerializable {
    private Integer entityID;
    private String entityType;
    private Integer itemID;
    private String itemName;
    private String itemNumber;
    private String description;
    private BigDecimal price;
    private BigDecimal qty;
    private SelectItem unitMeasurement;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public SelectItem getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItem unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public CompanyCustomFieldItem getCustomFieldByCode(String columnCode) {

        if (itemCustomFields != null && !itemCustomFields.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : itemCustomFields) {

                if (columnCode.equals(fieldItem.getColumnCode())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }
}
