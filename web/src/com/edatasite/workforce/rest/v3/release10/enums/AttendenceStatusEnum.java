package com.edatasite.workforce.rest.v3.release10.enums;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

public enum AttendenceStatusEnum {
    CHECK_IN, ON_BREAK, ON_DUTY, OFF_DUTY;

    public static String getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return AttendenceStatusEnum.valueOf(status.toUpperCase()).toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getKPIStatus(String status) {
        if (status == null) {
            return null;
        }
        if (CHECK_IN.name().equals(status)) {
            return Constants.AVAILABLE;
        }
        if (ON_BREAK.name().equals(status)) {
            return Constants.BREAK;
        }
        if (ON_DUTY.name().equals(status)) {
            return Constants.ON_DUTY;
        }
        if (OFF_DUTY.name().equals(status)) {
            return Constants.NOT_AVAILABLE;
        }
        return null;
    }

}
