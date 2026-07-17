package com.edatasite.workforce.gwt.core.client.rpc.leaveRequest;

import com.edatasite.workforce.gwt.core.client.rpc.BaseListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

public class LaborPeriodRequest extends BaseListItem implements ListingCustomFields, IsSerializable {

    public static final String LABOR_PERIOD = "period";
    public static final String ALLOWANCE = "currentAllowance";
    public static final String TAKEN = "taken";
    public static final String ADJUSTED = "adjusted";
    public static final String LEFT_DAYS = "leftDays";

    private Integer employeeID;
    private Integer periodID;
    private Double takenDays;
    private Date startDate;
    private Date endDate;
    private Date leavePeriodCreatedDate;
    private String laborPeriod;
    private String leavePeriod;
    private Double allowance;
    private Double approvedTakenDays;
    private Double outOfSystemDays;
    private Double overAllSubmittedLeaveDays;
    private String createdDate;
    private String leaveRequestNumber;
    private String leaveRequestStatus;
    private String leaveRequestStatusCode;
    private Double currentLeaveDays;
    private Double minLeaveDays;
    private String fromDateToDate;
    private Integer leaveRequestID;
    private ArrayList<MultiLeaveDTO> multiLeaveList;
    private Double experienceDays;

    public Double getTakenDays() {
        return takenDays;
    }

    public void setTakenDays(Double takenDays) {
        this.takenDays = takenDays;
    }

    public ArrayList<MultiLeaveDTO> getMultiLeaveList() {
        return multiLeaveList;
    }

    public void setMultiLeaveList(ArrayList<MultiLeaveDTO> multiLeaveList) {
        this.multiLeaveList = multiLeaveList;
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
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

    public String getLaborPeriod() {
        return laborPeriod;
    }

    public void setLaborPeriod(String laborPeriod) {
        this.laborPeriod = laborPeriod;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Double getApprovedTakenDays() {
        return approvedTakenDays;
    }

    public void setApprovedTakenDays(Double approvedTakenDays) {
        this.approvedTakenDays = approvedTakenDays;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLeaveRequestNumber() {
        return leaveRequestNumber;
    }

    public void setLeaveRequestNumber(String leaveRequestNumber) {
        this.leaveRequestNumber = leaveRequestNumber;
    }

    public Double getMinLeaveDays() {
        return minLeaveDays;
    }

    public void setMinLeaveDays(Double minLeaveDays) {
        this.minLeaveDays = minLeaveDays;
    }

    public String getLeaveRequestStatus() {
        return leaveRequestStatus;
    }

    public void setLeaveRequestStatus(String leaveRequestStatus) {
        this.leaveRequestStatus = leaveRequestStatus;
    }

    public String getLeaveRequestStatusCode() {
        return leaveRequestStatusCode;
    }

    public void setLeaveRequestStatusCode(String leaveRequestStatusCode) {
        this.leaveRequestStatusCode = leaveRequestStatusCode;
    }

    public Double getCurrentLeaveDays() {
        return currentLeaveDays;
    }

    public void setCurrentLeaveDays(Double currentLeaveDays) {
        this.currentLeaveDays = currentLeaveDays;
    }

    public Double getOverAllSubmittedLeaveDays() {
        return overAllSubmittedLeaveDays;
    }

    public void setOverAllSubmittedLeaveDays(Double overAllSubmittedLeaveDays) {
        this.overAllSubmittedLeaveDays = overAllSubmittedLeaveDays;
    }

    public String getFromDateToDate() {
        return fromDateToDate;
    }

    public void setFromDateToDate(String fromDateToDate) {
        this.fromDateToDate = fromDateToDate;
    }

    public String getLeavePeriod() {
        return leavePeriod;
    }

    public void setLeavePeriod(String leavePeriod) {
        this.leavePeriod = leavePeriod;
    }

    public Integer getLeaveRequestID() {
        return leaveRequestID;
    }

    public void setLeaveRequestID(Integer leaveRequestID) {
        this.leaveRequestID = leaveRequestID;
    }

    public Date getLeavePeriodCreatedDate() {
        return leavePeriodCreatedDate;
    }

    public void setLeavePeriodCreatedDate(Date leavePeriodCreatedDate) {
        this.leavePeriodCreatedDate = leavePeriodCreatedDate;
    }

    public Double getOutOfSystemDays() {
        return outOfSystemDays;
    }

    public void setOutOfSystemDays(Double outOfSystemDays) {
        this.outOfSystemDays = outOfSystemDays;
    }

    @Override
    public Integer getRelationID() {
        return null;
    }

    @Override
    public String getRelationType() {
        return null;
    }

    @Override
    public String getRelationName() {
        return null;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {

    }

    public void setExperienceDays(Double experienceDays) {
        this.experienceDays = experienceDays;
    }

    public Double getExperienceDays() {
        return experienceDays != null ? experienceDays : 0;
    }
}
