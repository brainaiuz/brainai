package com.edatasite.workforce.rest.base.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/20/15 9:27 PM
 */
public enum DeviceStatusEnum implements IsSerializable {
    ON("ON"),
    OFF("OFF");

    private String status;

    DeviceStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static DeviceStatusEnum getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return DeviceStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
