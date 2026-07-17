package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 3/19/13
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingRolePermissionItem extends SelectItem {
    private String role = "";
    private String value;
    private String code;
    private String parent;

    public ReportingRolePermissionItem(Integer id, String name, String code, String value) {
        super(id, name);
        this.code = code;
        this.value = value;
    }

    public ReportingRolePermissionItem(Integer id, String name, String code, String value, String parent) {
        super(id, name);
        this.code = code;
        this.value = value;
        this.parent = parent;
    }

    public ReportingRolePermissionItem() {
    }

    public String getRole() {
        return role = role == null ? "" : role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent = parent == null ? "" : parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
