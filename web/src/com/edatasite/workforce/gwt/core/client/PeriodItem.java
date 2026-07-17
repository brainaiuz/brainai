package com.edatasite.workforce.gwt.core.client;

import java.util.Date;

public class PeriodItem {

    private Integer objectID;
    private Integer periodID;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private Date modifiedDate;
    private Double allowance;
    private Double outOfSystemDays;
    private String employee;
    private String modifiedBy;
    private String text;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Double getOutOfSystemDays() {
        return outOfSystemDays;
    }

    public void setOutOfSystemDays(Double outOfSystemDays) {
        this.outOfSystemDays = outOfSystemDays;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }
}
