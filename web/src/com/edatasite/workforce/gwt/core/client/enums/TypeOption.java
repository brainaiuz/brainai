package com.edatasite.workforce.gwt.core.client.enums;

/**
 * @author Hurshid on 1/17/2019
 */
public enum TypeOption {

    NOT_ALLOW_EXCEED_ALLOWANCE("Do not allow"),
    ALLOW_AS_PAID("Allow as paid"),
    ALLOW_AS_NON_PAID("Allow as non-paid");

    String name;

    TypeOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
