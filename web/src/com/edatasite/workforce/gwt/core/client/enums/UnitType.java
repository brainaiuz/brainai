package com.edatasite.workforce.gwt.core.client.enums;

/**
 * @author Hurshid on 12/18/2018
 */
public enum UnitType {
    DAILY("Daily"),
    HOURLY("Hourly"),
    DAILY_WORK("Daily/Work");

    String name;

    UnitType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
