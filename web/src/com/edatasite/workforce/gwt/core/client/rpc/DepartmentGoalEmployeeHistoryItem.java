package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class DepartmentGoalEmployeeHistoryItem implements IsSerializable {

    public static final String DATE = "DATE";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String ACTUAL = "ACTUAL";
    public static final String COMMENT = "COMMENT";
    public static final String EMPLOYEE = "EMPLOYEE";


    private Integer id;
    private Date date;
    private Date creationDate;
    private Double actual;
    private String employee;
    private String comment;
    private Integer employeeId;
    private Integer goalAssigneeId;
    private Integer departmentGoalId;

    public DepartmentGoalEmployeeHistoryItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentGoalId() {
        return departmentGoalId;
    }

    public void setDepartmentGoalId(Integer departmentGoalId) {
        this.departmentGoalId = departmentGoalId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getGoalAssigneeId() {
        return goalAssigneeId;
    }

    public void setGoalAssigneeId(Integer goalAssigneeId) {
        this.goalAssigneeId = goalAssigneeId;
    }
}
