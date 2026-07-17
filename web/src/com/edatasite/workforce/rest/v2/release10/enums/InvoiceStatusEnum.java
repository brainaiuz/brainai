package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public enum InvoiceStatusEnum {
    DRAFT("DRAFT"),
    OPEN("OPEN"),
    APPROVE("APPROVE"),
    SALE_ORDER("SALE_ORDER"),
    PAID("PAID"),
    OVER_DUE("OVER_DUE"),
    REVERSED("REVERSED");

    private String status;

    InvoiceStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return InvoiceStatusEnum.valueOf(status.toUpperCase()).toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
