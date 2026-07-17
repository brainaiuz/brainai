package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum UserTypeEnum {

    USER("USER"),
    MANAGER("MANAGER");

    private String type;

    UserTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
