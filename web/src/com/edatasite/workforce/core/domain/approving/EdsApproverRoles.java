package com.edatasite.workforce.core.domain.approving;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by dilsh0d on 17.05.16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "approver_roles")
public class EdsApproverRoles  extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", insertable = false, updatable = false)
    private EdsApprover approver;

    @Column(name = "approver_id")
    private Integer approverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",insertable = false,updatable = false)
    private EdsRole role;

    @Column(name = "role_id")
    private Integer roleId;

    @Basic
    private Boolean approveForAll = Boolean.FALSE;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Boolean getApproveForAll() {
        return approveForAll != null ? approveForAll : Boolean.FALSE;
    }

    public void setApproveForAll(Boolean approveForAll) {
        this.approveForAll = approveForAll;
    }


    public EdsApprover getApprover() {
        return approver;
    }

    public EdsRole getRole() {
        return role;
    }


    public SelectItem getAsSelectItem() {
        SelectItem item = new SelectItem(getRoleId(), getRole().getName(), getRole().getCode());
        item.setSelected(getApproveForAll());
        return item;
    }
}
