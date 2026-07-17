package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public enum PermissionSubGroupEnum {
    FLOW_SETTINGS("FLOW_SETTINGS"),
    LEADS("LEADS"),
    OPPORTUNITIES("OPPORTUNITIES"),
    TASKS("TASKS"),
    SOME_OTHER("SOME_OTHER");

    private String group;

    PermissionSubGroupEnum(String group) {
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
            return PermissionSubGroupEnum.valueOf(group.toUpperCase()).toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
