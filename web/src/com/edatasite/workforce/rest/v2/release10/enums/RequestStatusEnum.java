package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum RequestStatusEnum {
    APPROVED("APPROVED"),
    PARTIALLY_APPROVED("PARTIALLY_APPROVED"),
    DECLINED("DECLINED"),
    PENDING("PENDING"),
    PAID("PAID"),
    DRAFT("DRAFT"),
    CUSTOM("CUSTOM");

    private String status;

    RequestStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static RequestStatusEnum getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return RequestStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
