package com.edatasite.workforce.gwt.core.client.enums;

/**
 * Created by Normurod on 8/5/15.
 */
public enum EmployeeAssignmentEnum {
    BY_POSITION(1, "By Position"),
    BY_EMPLOYEE(2, "By Department");

    private int id;
    private String title;

    EmployeeAssignmentEnum(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static EmployeeAssignmentEnum buildWithId(int id) {
        switch (id) {
            case 1:
                return BY_POSITION;
            case 2:
                return BY_EMPLOYEE;
            default:
                return BY_EMPLOYEE;
        }
    }
}
