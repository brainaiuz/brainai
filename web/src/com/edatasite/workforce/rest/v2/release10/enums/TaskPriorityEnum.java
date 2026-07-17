package com.edatasite.workforce.rest.v2.release10.enums;

import com.edatasite.workforce.core.domain.EdsTask;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum TaskPriorityEnum {
    LOW, HIGH, MEDIUM, NONE;

    public static String get(String priorityCode) {
        if (priorityCode == null || "".equals(priorityCode)) {
            return null;
        }
        if (EdsTask.HIGH.equalsIgnoreCase(priorityCode)) {
            return HIGH.name();
        }
        if (EdsTask.MEDIUM.equalsIgnoreCase(priorityCode)) {
            return MEDIUM.name();
        }
        if (EdsTask.LOW.equalsIgnoreCase(priorityCode)) {
            return LOW.name();
        }
        return null;
    }

    public static String from(String priorityCode) {
        if (priorityCode == null || "".equals(priorityCode)) {
            return null;
        }
        if (TaskPriorityEnum.HIGH.name().equalsIgnoreCase(priorityCode)) {
            return EdsTask.HIGH;
        }
        if (TaskPriorityEnum.MEDIUM.name().equalsIgnoreCase(priorityCode)) {
            return EdsTask.MEDIUM;
        }
        if (TaskPriorityEnum.LOW.name().equalsIgnoreCase(priorityCode)) {
            return EdsTask.LOW;
        }
        return null;
    }
}
