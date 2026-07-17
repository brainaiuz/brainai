package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FIFODataMQ implements Serializable {

    private Integer objectId;
    private Integer companyId;
    private Integer entityId;
    private Integer transactionId;
    private List<FIFOItemMQ> fifoItems = new ArrayList<>();
    private Date createdAt;
    private FailTarget target;
    private EntityType entityType;
    private boolean removing;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public boolean isRemoving() {
        return removing;
    }

    public void setRemoving(boolean removing) {
        this.removing = removing;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public List<FIFOItemMQ> getFifoItems() {
        return fifoItems;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setFifoItems(List<FIFOItemMQ> fifoItems) {
        if (!fifoItems.isEmpty()) {
            this.fifoItems.addAll(fifoItems);
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public FailTarget getTarget() {
        return target;
    }

    public void setTarget(FailTarget target) {
        this.target = target;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return "FifoDataMQ{" + "entityId=" + entityId + ", transactionId=" + transactionId + ", fifoItems=" + fifoItems + '}';
    }
}
