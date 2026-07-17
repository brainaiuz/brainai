package com.edatasite.workforce.gwt.accounting.client.rpc.enums;

/**
 * Created by Normurod on 12/20/2016.
 */
public enum PriceLevelOperationTypeEnum {
    FOR_CLIENT(1, "For Client"),
    FOR_SUPPLIER(2, "For Supplier");

    private int id;
    private String title;

    PriceLevelOperationTypeEnum(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
