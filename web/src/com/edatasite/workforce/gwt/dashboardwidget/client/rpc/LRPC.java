package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class LRPC implements IsSerializable, Cloneable {
    private Integer objectID;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private Integer employeeId;
    private String employeeName;
    private String employeePhotoUrl;
    private String reason;
    private String reasonColor;
    private String duration;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhotoUrl() {
        return employeePhotoUrl;
    }

    public void setEmployeePhotoUrl(String employeePhotoUrl) {
        this.employeePhotoUrl = employeePhotoUrl;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonColor() {
        return reasonColor;
    }

    public void setReasonColor(String reasonColor) {
        this.reasonColor = reasonColor;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
