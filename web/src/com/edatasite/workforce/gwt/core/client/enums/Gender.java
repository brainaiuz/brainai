package com.edatasite.workforce.gwt.core.client.enums;

/**
 * @author Hurshid on 12/18/2018
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
