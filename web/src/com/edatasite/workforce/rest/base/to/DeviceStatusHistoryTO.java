package com.edatasite.workforce.rest.base.to;

/**
 * Created by Dilsh0d on 10/26/2017.
 */

public class DeviceStatusHistoryTO {

    private String deviceUniqueKey;
    private String deviceStatus;
    private String statusUpdateTime;
    private String description;

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceUniqueKey() {
        return deviceUniqueKey;
    }

    public void setDeviceUniqueKey(String deviceUniqueKey) {
        this.deviceUniqueKey = deviceUniqueKey;
    }

    public String getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(String statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
