package com.edatasite.workforce.core.domain.fifo;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "fifo_failures")
public class EdsFifoFailure extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Column(name = "entity_id", nullable = false)
    private Integer entityId;

    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    @OneToMany(mappedBy = "failure", cascade = CascadeType.ALL)
    private List<EdsFifoItem> items;

    @Column(name = "removing")
    private boolean removing = false;

    @Column(name = "deleted")
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    private FailTarget target;

    @Enumerated(EnumType.STRING)
    private EntityType type;

    @Column(name = "retries")
    private Integer retries = 0;

    @Column(name = "last_attempt_at")
    private Date lastAttemptAt;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "onQue")
    private Boolean onQue;

    @Column(name = "fail_reason")
    @Type(type = "text")
    private String failReason;

    @Override
    public Integer getObjectID() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer invoiceId) {
        this.entityId = invoiceId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public List<EdsFifoItem> getItems() {
        return items;
    }

    public void setItems(List<EdsFifoItem> items) {
        this.items = items;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Date getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(Date lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isRemoving() {
        return removing;
    }

    public void setRemoving(boolean removing) {
        this.removing = removing;
    }

    public FailTarget getTarget() {
        return target;
    }

    public void setTarget(FailTarget target) {
        this.target = target;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getOnQue() {
        return onQue;
    }

    public void setOnQue(Boolean onQue) {
        this.onQue = onQue;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public FIFODataMQ toRPC() {
        FIFODataMQ dataMQ = new FIFODataMQ();
        dataMQ.setObjectId(getObjectID());
        dataMQ.setCompanyId(getCompanyId());
        dataMQ.setEntityId(getEntityId());
        dataMQ.setTransactionId(getTransactionId());
        List<FIFOItemMQ> itemList = getItems().stream().map(i -> i.toRPC()).collect(Collectors.toList());
        dataMQ.setFifoItems(itemList);
        dataMQ.setTarget(getTarget());
        dataMQ.setEntityType(getType());
        dataMQ.setRemoving(isRemoving());
        dataMQ.setCreatedAt(getCreatedAt());

        return dataMQ;
    }
}
