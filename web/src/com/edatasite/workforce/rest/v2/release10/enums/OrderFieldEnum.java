package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum OrderFieldEnum {
    DATE("DATE"),
    NAME("NAME"),
    ID("ID"),
    COMPANY("COMPANY"),
    KANBAN_ORDER("KANBAN_ORDER");

    private String field;

    OrderFieldEnum(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static OrderFieldEnum getOrderField(String type) {
        if (type == null) {
            return null;
        }
        try {
            return OrderFieldEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
