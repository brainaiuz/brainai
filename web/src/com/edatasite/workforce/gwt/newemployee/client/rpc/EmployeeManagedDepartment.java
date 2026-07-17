package com.edatasite.workforce.gwt.newemployee.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EmployeeManagedDepartment implements IsSerializable {

    private String name;
    private String description;
    private Integer employees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }
}
