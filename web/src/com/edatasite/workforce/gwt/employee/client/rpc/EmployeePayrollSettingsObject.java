package com.edatasite.workforce.gwt.employee.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 22.02.2009
 * Time: 19:51:42
 * To change this template use File | Settings | File Templates.
 */
public class EmployeePayrollSettingsObject implements IsSerializable {

    private Integer objectID;

    private Integer employeeID;

    private String key;

    private String value;

    public EmployeePayrollSettingsObject() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}