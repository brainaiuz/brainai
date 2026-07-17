package com.edatasite.workforce.core.domain.fifo;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "fifo_items")
public class EdsFifoItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "transaction_item_id")
    private Integer transactionItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "failure_id")
    private EdsFifoFailure failure;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public Integer getObjectID() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(Integer transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public EdsFifoFailure getFailure() {
        return failure;
    }

    public void setFailure(EdsFifoFailure failure) {
        this.failure = failure;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public FIFOItemMQ toRPC() {
        FIFOItemMQ item = new FIFOItemMQ();
        item.setObjectId(getObjectID());
        item.setProductId(getItemId());
        item.setQuantity(getQuantity());
        item.setTransactionItemId(getTransactionItemId());
        item.setWarehouserId(getWarehouseId());

        return item;
    }
}
