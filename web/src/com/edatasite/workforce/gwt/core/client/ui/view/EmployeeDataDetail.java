package com.edatasite.workforce.gwt.core.client.ui.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class EmployeeDataDetail implements IsSerializable, Serializable {

    private String postion;
    private String department;
    private String employmentMode;
    private Double basicSalary;
    private String allowance;
    private String location;

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getEmploymentMode() {
        return employmentMode;
    }

    public void setEmploymentMode(String employmentMode) {
        this.employmentMode = employmentMode;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
