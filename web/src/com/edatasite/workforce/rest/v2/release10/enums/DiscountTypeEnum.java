package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum DiscountTypeEnum {
    FIXED(0, "FIXED"),
    PERCENT(1, "PERCENT");

    private Integer id;
    private String type;

    DiscountTypeEnum(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Integer getDiscountType(Integer id) {
        if (id == null) {
            return null;
        }
        if (id == 0) {
            return DiscountTypeEnum.FIXED.getId();
        }
        if (id == 1) {
            return DiscountTypeEnum.PERCENT.getId();
        }
        return null;
    }
}
