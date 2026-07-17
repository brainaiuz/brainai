package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

/**
 * Created by Dilsh0d Madrahimov on 7/17/2017.
 */
public enum DiscountTypeEnum {
    PERCENTAGE(0, "Percentage", "PERCENTAGE"),
    FIXED_AMOUNT(1, "Fixed Amount", "FIXED_AMOUNT");

    private int id;
    private String name;
    private String code;

    DiscountTypeEnum(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static DiscountTypeEnum buildWithId(int id) {
        switch (id) {
            case 0:
                return PERCENTAGE;
            case 1:
                return FIXED_AMOUNT;
            default:
                return PERCENTAGE;
        }
    }

}
