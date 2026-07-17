package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov.
 */
public class InventoryStockTO implements IsSerializable {
    Integer id;
    SelectItemTO product;
    SelectItemTO warehouse;
    BigDecimal quantityOnHand;
    BigDecimal reorderPoint;

    public InventoryStockTO() {

    }

    public InventoryStockTO(ProductLocationItem item) {
        this.id = item.getObjectID();
        this.product = new SelectItemTO(item.getProductID(), item.getProductName());
        this.warehouse = new SelectItemTO(item.getWarehouseID(), item.getWarehouseName());
        this.quantityOnHand = item.getQty();
        this.reorderPoint = item.getMinReorderPoint();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItemTO getProduct() {
        return product;
    }

    public void setProduct(SelectItemTO product) {
        this.product = product;
    }

    public SelectItemTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItemTO warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(BigDecimal quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        this.reorderPoint = reorderPoint;
    }
}
