package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Farrukh Abdurakhmonov on 26.10.2017.
 * Date: 26/10/17
 * Time: 3:19 PM
 */
public class FingerPrintDeviceStatusHistoryListItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String STATUS = "status";
    public static final String UPDATED_TIME = "updatedTime";
    public static final String DESCRIPTION = "description";
    public static final String DEVICE_NAME = "deviceName";

    private Integer objectID;
    private String deviceName;
    private String deviceUniqueKey;
    private String deviceStatus;
    private DateNonConvertable statusUpdateTime;
    private String description;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDeviceUniqueKey() {
        return deviceUniqueKey;
    }

    public void setDeviceUniqueKey(String deviceUniqueKey) {
        this.deviceUniqueKey = deviceUniqueKey;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public DateNonConvertable getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(DateNonConvertable statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}