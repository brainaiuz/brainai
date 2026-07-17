package com.edatasite.workforce.core.domain.locking;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingChanges;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingModule;
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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "transaction_locking")
public class EdsTransactionLocking extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String description;
    private Date lockDate;
    private String status;
    @Type(type = "jsonb")
    @Column(name = "lock_modules", columnDefinition = "jsonb")
    private Map<String, TransactionLockingModule> modules = new HashMap<>();

    @Transient
    private List<TransactionLockingChanges> changes = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        if (!ServerUtils.equalsDate(this.lockDate, lockDate)) {
            addChange("Lock Date", this.lockDate, lockDate);
        }
        this.lockDate = lockDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!ServerUtils.equalsString(this.status, status)) {
            addChange("Status", this.status, status);
        }
        this.status = status;
    }

    public Map<String, TransactionLockingModule> getModules() {
        return modules;
    }

    public void setModules(Map<String, TransactionLockingModule> newModules) {
        if (getObjectID() != null && this.modules != null) {
            // Check for removed modules, this is for fother changes
            for (String module : this.modules.keySet()) {
                if (!newModules.containsKey(module)) {
                    TransactionLockingModule oldModule = this.modules.get(module);
                    addChange(module, oldModule.getStatus(), "removed");
                }
            }

            // Check for added or updated modules
            for (Map.Entry<String, TransactionLockingModule> entry : newModules.entrySet()) {
                String module = entry.getKey();
                TransactionLockingModule newModule = entry.getValue();
                TransactionLockingModule oldModule = this.modules.get(module);
                if (oldModule == null) {
                    String newModuleStatus = newModule.getStatus();
                    addChange(module, "added", newModuleStatus != null ? newModuleStatus : "unlocked");
                } else if (!ServerUtils.equalsString(oldModule.getStatus(), newModule.getStatus())) {
                    addChange(module, oldModule.getStatus(), newModule.getStatus());
                }
            }
        }
        this.modules = newModules;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public List<TransactionLockingChanges> getChanges() {
        return changes;
    }

    private void addChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            TransactionLockingChanges change = new TransactionLockingChanges();
            change.setField(field);
            change.setOldValue(oldValue != null ? String.valueOf(oldValue) : "not present");
            change.setNewValue(newValue != null ? String.valueOf(newValue) : "not present");
            changes.add(change);
        }
    }
}
