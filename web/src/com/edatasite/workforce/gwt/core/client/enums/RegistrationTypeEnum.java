package com.edatasite.workforce.gwt.core.client.enums;

/**
 * Created by Dilsh0d on 10/6/2017.
 */
public enum RegistrationTypeEnum {
    EMAIL("EMAIL"),
    GOOGLE("GOOGLE"),
    FACEBOOK("FACEBOOK"),
    LINKEDIN("LINKEDIN"),
    MICROSOFT("MICROSOFT"),
    PHONE("PHONE");

    private final String type;

    RegistrationTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static RegistrationTypeEnum getRegistrationType(String type) {
        if (type == null) {
            return null;
        }
        try {
            return RegistrationTypeEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

}
