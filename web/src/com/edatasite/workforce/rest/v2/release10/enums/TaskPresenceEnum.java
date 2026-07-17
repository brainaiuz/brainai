package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum TaskPresenceEnum {

    AVAILABLE("AVAILABLE"),
    OVERDUE("OVERDUE"),
    NO_TASKS("NO_TASKS");

    private String type;

    TaskPresenceEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
