package com.edatasite.workforce.rest.v2.release10.enums;


/**
 * Created by Abdurakhmonov Farrukh on 12/19/2017 1:50 PM
 */

public enum RelevanceIndicator {
    RED(1, "RED"),
    YELLOW(2, "YELLOW"),
    GREEN(3, "GREEN");

    private int order;
    private String name;

    RelevanceIndicator(int order, String name) {
        this.order = order;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

}
