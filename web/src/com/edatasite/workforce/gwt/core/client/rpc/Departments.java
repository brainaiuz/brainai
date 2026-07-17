package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Mar 2, 2009
 * Time: 1:05:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Departments implements IsSerializable {

    String deptName;
    String employees;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }
}
