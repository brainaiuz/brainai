package com.edatasite.workforce.core.domain.locking;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingChanges;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "transaction_locking_reason")
public class EdsTransactionLockingReason extends EdsObject implements ObjectHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String reason;
    @Type(type = "jsonb")
    @Column(name = "lock_changes", columnDefinition = "jsonb")
    private List<TransactionLockingChanges> changes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date lastUpdateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<TransactionLockingChanges> getChanges() {
        return changes;
    }

    public void setChanges(List<TransactionLockingChanges> changes) {
        this.changes = changes;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }
}
