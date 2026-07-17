package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class InventoryStockItemTO extends ResponseData {
    private WarehouseTO warehouse;
    private BigDecimal quantity_on_hand;
    private BigDecimal reorder_point;

    public InventoryStockItemTO() {
    }

    public WarehouseTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTO warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getQuantity_on_hand() {
        return quantity_on_hand;
    }

    public void setQuantity_on_hand(BigDecimal quantity_on_hand) {
        this.quantity_on_hand = quantity_on_hand;
    }

    public BigDecimal getReorder_point() {
        return reorder_point;
    }

    public void setReorder_point(BigDecimal reorder_point) {
        this.reorder_point = reorder_point;
    }
}
