package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RFPItem implements Serializable {

    private Integer objectID;
    private ProductSelectItem productItem;
    private String description;
    private BigDecimal qty;
    private SelectItem wareHouse;
    private BigDecimal qtyOnhand;
    private boolean selected;
    private SelectItem measurement;
    private SelectItem departmentItem;
    private Integer entityID;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private boolean hasProductList;
    public RFPItem() {
    }

    public ProductSelectItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductSelectItem productItem) {
        this.productItem = productItem;
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
        if (qty == null) {
            qty = BigDecimal.ZERO;
        }
        this.qty = qty;
    }

    public SelectItem getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(SelectItem wareHouse) {
        this.wareHouse = wareHouse;
    }

    public BigDecimal getQtyOnhand() {
        return qtyOnhand;
    }

    public void setQtyOnhand(BigDecimal qtyOnhand) {
        this.qtyOnhand = qtyOnhand;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getMeasurement() {
        return measurement;
    }

    public void setMeasurement(SelectItem measurement) {
        this.measurement = measurement;
    }

    public SelectItem getDepartmentItem() {
        return this.departmentItem;
    }

    public void setDepartmentItem(final SelectItem departmentItem) {
        this.departmentItem = departmentItem;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public boolean isHasProductList() {
        return this.hasProductList;
    }

    public void setHasProductList(final boolean hasProductList) {
        this.hasProductList = hasProductList;
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
