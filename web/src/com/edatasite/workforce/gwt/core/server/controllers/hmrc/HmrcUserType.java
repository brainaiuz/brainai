package com.edatasite.workforce.gwt.core.server.controllers.hmrc;

public enum HmrcUserType {
    personal("personal"),
    business("business"),
    agemt("agent");

    String value;

    HmrcUserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HmrcUserType fromString(String value) {
        if (value != null) {
            value = value.trim();
            for (HmrcUserType item : HmrcUserType.values()) {
                if (value.equalsIgnoreCase(item.getValue()) || value.equals(item.name())) {
                    return item;
                }
            }
        }
        return null;
    }
}
