package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum LeaveRequestStatusTypeEnum {
    FIXED("FIXED"),
    RANGE("RANGE");

    private String type;

    LeaveRequestStatusTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LeaveRequestStatusTypeEnum getType(String type) {
        if (type == null) {
            return null;
        }
        try {
            return LeaveRequestStatusTypeEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
