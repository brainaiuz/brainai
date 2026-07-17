package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created Abdurakhmonov Farrukh on 16.01.2018.
 */
public enum OtherRequestEnum {
    ADDITIONAL_PAYMENT("ADDITIONAL_PAYMENT"),
    GOAL("GOAL"),
    INCIDENT("INCIDENT");

    private String status;

    OtherRequestEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static OtherRequestEnum getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return OtherRequestEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
