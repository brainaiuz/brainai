package com.edatasite.workforce.rest.v2.release10.enums;

public enum TermsEnum {
    FIXED(0, "FIXED"),
    PERCENTAGE(1, "PERCENTAGE");

    private Integer id;
    private String code;

    TermsEnum(Integer id, String code) {
        this.id = id;
        this.code = code;
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

    public void setCode(String code) {
        this.code = code;
    }

    public static TermsEnum getCode(String code) {
        if (code == null) {
            return null;
        }
        try {
            return TermsEnum.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Integer getId(Integer id) {
        if (id == null) {
            return null;
        }
        if (id == 0) {
            return FIXED.getId();
        }
        if (id == 1) {
            return PERCENTAGE.getId();
        }
        return null;
    }

}
