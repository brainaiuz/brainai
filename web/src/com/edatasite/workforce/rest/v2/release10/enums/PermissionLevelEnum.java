package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public enum PermissionLevelEnum {
    READ("READ"),
    WRITE("WRITE");

    private String level;

    PermissionLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public static String getLevel(String level) {
        if (level == null) {
            return null;
        }
        try {
            return PermissionLevelEnum.valueOf(level.toUpperCase()).toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
