package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum TaxTypeEnum {
    NO_TAX(0, "NO_TAX", "No Tax"),
    TAX_INCLUSIVE(1, "TAX_INCLUSIVE", "Tax Inclusive"),
    TAX_EXCLUSIVE(2, "TAX_EXCLUSIVE", "Tax Exclusive");

    private Integer id;
    private String code;
    private String name;

    TaxTypeEnum(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static TaxTypeEnum getTaxTypeById(Integer id) {
        if (id == null) {
            return null;
        }
        if (id == 0) {
            return NO_TAX;
        }
        if (id == 1) {
            return TAX_INCLUSIVE;
        }
        if (id == 2) {
            return TAX_EXCLUSIVE;
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
