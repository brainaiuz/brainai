package com.edatasite.workforce.rest.v2.release10.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomFieldRelationTypeEnum {
    Candidate("Candidate"),
    Vacancy("Vacancy");

    private String text;

    CustomFieldRelationTypeEnum(String text) {
        this.text = text;
    }

    @JsonCreator
    public static CustomFieldRelationTypeEnum forValue(String text) {
        if (text != null) {
            for (CustomFieldRelationTypeEnum b : CustomFieldRelationTypeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return text;
    }
}
