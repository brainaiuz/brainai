package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Hurshid on 11/14/2017.
 */
public class BillOfMaterialItem implements IsSerializable {

    private Integer projectID;
    private Integer objectID;
    private String description;
    private Integer itemID;
    private String itemName;
    private String itemNumber;
    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal onHand;
    private BigDecimal requestedQqty;
    private Integer supplierID;
    private String supplierName;
    private SelectItem unitMeasurement;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private String status;

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRequestedQqty() {
        if (requestedQqty == null) {
            requestedQqty = BigDecimal.ZERO;
        }
        return requestedQqty;
    }

    public void setRequestedQqty(BigDecimal requestedQqty) {
        this.requestedQqty = requestedQqty;
    }

    public BigDecimal getQty() {
        if (qty == null) {
            qty = BigDecimal.ZERO;
        }
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
    public BigDecimal getOnHand() {
        return onHand;
    }

    public void setOnHand(BigDecimal onHand) {
        this.onHand = onHand;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public SelectItem getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItem unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
