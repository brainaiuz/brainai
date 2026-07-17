package com.edatasite.workforce.gwt.availability.server.enums;

import java.util.HashMap;
import java.util.Map;

public enum GoalCategoryEnum {
    DG("DEPARTMENT_GOAL"),
    PG("PERSONAL_GOAL"),
    Q("PROJECT_GOAL"),
    BG("BUSINESS_GOAL");

    private static final Map<String, GoalCategoryEnum> BY_VALUE = new HashMap<>();

    static {
        for (GoalCategoryEnum e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    public final String value;

    GoalCategoryEnum(String value) {
        this.value = value;
    }

    public static String getByValue(String value) {
        return String.valueOf(BY_VALUE.get(value));
    }
}
