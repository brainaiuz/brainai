package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

public class SickRequestSolr {

    private Integer id;
    private String numberData;
    private String description;
    private SelectItem overallStatus;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private SelectItem type;
    private SelectItem currentApprover;
    private SelectItem registeredBy;
    private EmployeeSolr employee;
    private SelectItem leaveReason;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOverallStatus(SelectItem overallStatus) {
        this.overallStatus = overallStatus;
    }

    public SelectItem getOverallStatus() {
        return overallStatus;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public SelectItem getType() {
        return type;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setRegisteredBy(SelectItem registeredBy) {
        this.registeredBy = registeredBy;
    }

    public SelectItem getRegisteredBy() {
        return registeredBy;
    }

    public void setEmployee(EmployeeSolr employee) {
        this.employee = employee;
    }

    public EmployeeSolr getEmployee() {
        return employee;
    }

    public SelectItem getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(SelectItem leaveReason) {
        this.leaveReason = leaveReason;
    }
}
