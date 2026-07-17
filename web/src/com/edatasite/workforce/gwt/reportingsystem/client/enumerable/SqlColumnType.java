package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

/**
 * User: ${Dilsh0d}
 * Date: 18-Mar-2010
 * Time: 16:58:19
 */
public enum SqlColumnType {
    TIME("time"),
    DATE("date"),
    NUMBER("number"),
    MONEY("money"),
    STRING("string"),
    USER_ID("userid"),
    COMPANY_ID("compamyid");

    SqlColumnType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
