package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum PeopleTypeEnum {
    ME("ME"),
    USUAL("USUAL"),
    N_A("N_A");

    private String type;

    PeopleTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
