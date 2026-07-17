package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum FlowSettingsTypeEnum {

    LEADS("LEADS"),
    OPPORTUNITIES("OPPORTUNITIES"),
    TASKS("TASKS");

    private String type;

    FlowSettingsTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
