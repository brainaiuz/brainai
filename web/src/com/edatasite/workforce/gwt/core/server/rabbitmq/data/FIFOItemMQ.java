package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class FIFOItemMQ implements Serializable {

    private Integer objectId;
    private Integer productId;
    private BigDecimal quantity;
    private Integer warehouserId;
    private Integer transactionItemId;
    private boolean hasError;

    public FIFOItemMQ() {
    }

    public FIFOItemMQ(Integer productId, BigDecimal quantity, Integer warehouserId) {
        this.productId = productId;
        this.quantity = quantity;
        this.warehouserId = warehouserId;
    }

    public FIFOItemMQ(Integer productId, BigDecimal quantity, Integer warehouserId, Integer transactionItemId) {
        this.productId = productId;
        this.quantity = quantity;
        this.warehouserId = warehouserId;
        this.transactionItemId = transactionItemId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getWarehouserId() {
        return warehouserId;
    }

    public void setWarehouserId(Integer warehouserId) {
        this.warehouserId = warehouserId;
    }

    public Integer getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(Integer transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public FifoItem convertToFifoItem() {
        FifoItem item = new FifoItem();
        item.setProductId(getProductId());
        item.setQuantity(getQuantity());
        item.setWarehouserId(getWarehouserId());
        item.setTransactionItemId(getTransactionItemId());
        return item;
    }
}
