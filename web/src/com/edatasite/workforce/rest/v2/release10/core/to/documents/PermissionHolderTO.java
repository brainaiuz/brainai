package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;

/**
 * Created by Anvar Akramov on 11/17/2017.
 */
public class PermissionHolderTO extends ResponseData {

    private Integer id;
    private EmployeeTO user;
    private SelectItemTO group;
    private boolean read;
    private boolean delete;
    private boolean write;
    private boolean modify_acl;
    private String role;
    private boolean can_change = true;
    private String relationship;

    public PermissionHolderTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmployeeTO getUser() {
        return user;
    }

    public void setUser(EmployeeTO user) {
        this.user = user;
    }

    public SelectItemTO getGroup() {
        return group;
    }

    public void setGroup(SelectItemTO group) {
        this.group = group;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isModify_acl() {
        return modify_acl;
    }

    public void setModify_acl(boolean modify_acl) {
        this.modify_acl = modify_acl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCan_change() {
        return can_change;
    }

    public void setCan_change(boolean can_change) {
        this.can_change = can_change;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
