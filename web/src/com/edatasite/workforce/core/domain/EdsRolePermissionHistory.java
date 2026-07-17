package com.edatasite.workforce.core.domain;

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
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rolepermission_history")
public class EdsRolePermissionHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "permissioncode")
    private String permissioncode;

    private String permissionName;
    private String roleName;
    private String context;
    private String oldValue;
    private String newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public String getPermissioncode() {
        return this.permissioncode;
    }

    public void setPermissioncode(final String permissioncode) {
        this.permissioncode = permissioncode;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public void setPermissionName(final String permissionName) {
        this.permissionName = permissionName;
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(final String context) {
        this.context = context;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(final String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public void setNewValue(final String newValue) {
        this.newValue = newValue;
    }

    public EdsUser getUpdater() {
        return this.updater;
    }

    public void setUpdater(final EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(final Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
}
