package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "item_serial_detail")
public class EdsItemSerialDetail extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serial_id")
    private EdsItemSerial serial;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "reversed", columnDefinition = "boolean default false")
    private Boolean reversed;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsItemSerial getSerial() {
        return serial;
    }

    public void setSerial(EdsItemSerial serial) {
        this.serial = serial;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean getReversed() {
        return reversed != null ? reversed : false;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }
}
