package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class FifoItem implements Serializable {
    private Integer transactionId;
    private Integer productId;
    private BigDecimal quantity;
    private Integer warehouserId;
    private Integer transactionItemId; //it could be invoice item id, adjustment item id etc.

    public FifoItem() {
    }

    public FifoItem(Integer transactionId, Integer productId, BigDecimal quantity, Integer warehouserId) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantity = quantity;
        this.warehouserId = warehouserId;
    }

    public FifoItem(Integer transactionId, Integer productId, BigDecimal quantity, Integer warehouserId, Integer transactionItemId) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantity = quantity;
        this.warehouserId = warehouserId;
        this.transactionItemId = transactionItemId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
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

    @Override
    public String toString() {
        return "FifoItem{" +
                "transactionId=" + transactionId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", warehouserId=" + warehouserId +
                ", transactionItemId=" + transactionItemId +
                '}';
    }
}
