package com.edatasite.workforce.gwt.core.client.enums;

public enum PeriodTypeEnum {
    DAY("Day"),
    MONTH("Month"),
    YEAR("Year");

    String code;

    PeriodTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
