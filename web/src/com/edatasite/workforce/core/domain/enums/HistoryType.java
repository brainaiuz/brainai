package com.edatasite.workforce.core.domain.enums;

/**
 * Created by Hurshid on 8/16/2017.
 */
public enum HistoryType {

    EMPLOYEE("Employee"),
    COMPANY_SETTINGS("Company Settings"),
    LEAVE_REASON("Leave Reason"),
    TIMESLOT("Timeslot"),
    TIMESLOT_ITEM("Timeslot items"),
    HOLIDAY("Holiday"),
    LABOR_PERIOD("Labor Period");

    String name;

    HistoryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
