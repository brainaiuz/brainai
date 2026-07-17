package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserFingerPrintAdjustment implements IsSerializable {
    private Integer id;
    private Integer employeeId;
    private DateNonConvertable startDate;
    private String deviceUUID;
    private String deviceName;
    private String statusString;
    private String fingerprintId;
    private Integer timeSlotId;
    private String description;
    private Long startDateMillis;
    private String source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getFingerprintId() {
        return fingerprintId;
    }

    public void setFingerprintId(String fingerprintId) {
        this.fingerprintId = fingerprintId;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartDateMillis() {
        return startDateMillis;
    }

    public void setStartDateMillis(Long startDateMillis) {
        this.startDateMillis = startDateMillis;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
