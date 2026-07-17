package com.edatasite.workforce.gwt.core.client.enums;

public enum UITypeEnum {

    CLASSIC_KPI("CLASSIC_KPI"),
    LIGHT_KPI("LIGHT_KPI");

    String code;

    UITypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
