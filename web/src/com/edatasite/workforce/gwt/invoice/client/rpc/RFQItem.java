package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class RFQItem implements IsSerializable{

    private Integer objectID;
    private ProductSelectItem product;
    private String name;
    private String description;
    private BigDecimal qty;
    private SelectItem measurement;
    private BigDecimal unitCost;
    private SelectItem supplier;
    private SelectItem[] suppliers;
    private RFQSupplierBid[] supplierBids;
    private FileResource[] attachments;
    private Boolean converted;
    private BigDecimal commission;
    private String reMarks;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    public RFQItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public ProductSelectItem getProduct() {
        return product;
    }

    public void setProduct(ProductSelectItem product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public SelectItem getMeasurement() {
        return measurement;
    }

    public void setMeasurement(SelectItem measurement) {
        this.measurement = measurement;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public SelectItem getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItem supplier) {
        this.supplier = supplier;
    }

    public SelectItem[] getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(SelectItem[] suppliers) {
        this.suppliers = suppliers;
    }

    public RFQSupplierBid[] getSupplierBids() {
        return supplierBids;
    }

    public void setSupplierBids(RFQSupplierBid[] supplierBids) {
        this.supplierBids = supplierBids;
    }

    public FileResource[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileResource[] attachments) {
        this.attachments = attachments;
    }

    public Boolean isConverted() {
        return converted != null ? converted : false;
    }

    public void setConverted(Boolean converted) {
        this.converted = converted;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getReMarks() {
        return reMarks;
    }

    public void setReMarks(String reMarks) {
        this.reMarks = reMarks;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
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
}
