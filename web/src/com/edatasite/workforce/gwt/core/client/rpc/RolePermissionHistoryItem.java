package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

public class RolePermissionHistoryItem implements IsSerializable, Serializable {

    public static final String PERMISSION_NAME = "PERMISSION_NAME";
    public static final String MODULE_NAME = "MODULE_NAME";
    public static final String ROLE_NAME = "ROLE_NAME";
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String MODIFIED_BY = "MODIFIED_BY";
    public static final String MODIFIED_DATE = "MODIFIED_DATE";

    private Integer userID;
    private String userName;
    private String permissionName;
    private String moduleName;
    private String oldValue;
    private String newValue;
    private String roleName;
    private Date updatedDate;

    public Integer getUserID() {
        return this.userID;
    }

    public void setUserID(final Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public void setPermissionName(final String permissionName) {
        this.permissionName = permissionName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
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

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
}