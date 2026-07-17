package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class RequestReportResult implements IsSerializable {

    private String employeeName;
    private String approverName;
    private String departmentName;
    private String description;
    private String startDate;
    private String endDate;
    private Date startDDate;
    private Date endDDate;
    private String reason;
    private String status;
    private String duration;
    private Double durationDay;
    private Double durationHour;
    private String type;
    private String remaining;
    private Integer groupId;
    private int n;

    public RequestReportResult() {

    }

    public RequestReportResult(String employeeName, String approverName,
                               String departmentName, String description, String startDate, String endDate,
                               String reason, String status, String duration, String type, String remaining, Integer groupId) {
        this.approverName = approverName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.departmentName = departmentName;
        this.employeeName = employeeName;
        this.reason = reason;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.type = type;
        this.remaining = remaining;
        this.groupId = groupId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Date getEndDDate() {
        return endDDate;
    }

    public void setEndDDate(Date endDDate) {
        this.endDDate = endDDate;
    }

    public Date getStartDDate() {
        return startDDate;
    }

    public void setStartDDate(Date startDDate) {
        this.startDDate = startDDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(Double durationDay) {
        this.durationDay = durationDay;
    }

    public Double getDurationHour() {
        return durationHour;
    }

    public void setDurationHour(Double durationHour) {
        this.durationHour = durationHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}