package com.edatasite.workforce.gwt.core.client.enums;

public enum PaymentTypeEnum {
    PAYPAL("PAYPAL"),
    STRIPE("STRIPE"),
    WIRE("WIRE"),
    REVOLUT("REVOLUT");

    String code;

    PaymentTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
