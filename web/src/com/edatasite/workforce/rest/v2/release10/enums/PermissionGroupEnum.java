package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public enum PermissionGroupEnum {
    FLOW_SETTINGS("FLOW_SETTINGS"),
    SALES("SALES");

    private String group;

    PermissionGroupEnum(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public static String getGroup(String group) {
        if (group == null) {
            return null;
        }
        try {
            return PermissionGroupEnum.valueOf(group.toUpperCase()).toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
