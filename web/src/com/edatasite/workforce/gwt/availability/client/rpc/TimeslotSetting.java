package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeslotSetting implements IsSerializable {

    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String DAYS = "DAYS";
    public static final String ADD = "ADD";
    public static final String TIMESLOT = "TIMESLOT";
    public static final String ACTION = "ACTION";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";

    private String employeeName;
    private Integer employeeID;
    private String departmentName;
    private String annualAllowance;//in days
    private String timeslotName;
    private Integer timeslotID;
    private Integer allowanceYear;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAnnualAllowance() {
        return annualAllowance;
    }

    public void setAnnualAllowance(String annualAllowance) {
        this.annualAllowance = annualAllowance;
    }

    public String getTimeslotName() {
        return timeslotName;
    }

    public void setTimeslotName(String timeslotName) {
        this.timeslotName = timeslotName;
    }

    public Integer getTimeslotID() {
        return timeslotID;
    }

    public void setTimeslotID(Integer timeslotID) {
        this.timeslotID = timeslotID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAllowanceYear() {
        return allowanceYear;
    }

    public void setAllowanceYear(Integer allowanceYear) {
        this.allowanceYear = allowanceYear;
    }

}
