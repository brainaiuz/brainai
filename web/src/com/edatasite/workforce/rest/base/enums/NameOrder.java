package com.edatasite.workforce.rest.base.enums;

public enum NameOrder {
    FIRST_MIDDLE_LAST("FIRST_MIDDLE_LAST"),
    FIRST_LAST_MIDDLE("FIRST_LAST_MIDDLE"),
    MIDDLE_FIRST_LAST("MIDDLE_FIRST_LAST"),
    MIDDLE_LAST_FIRST("MIDDLE_LAST_FIRST"),
    LAST_FIRST_MIDDLE("LAST_FIRST_MIDDLE"),
    LAST_MIDDLE_FIRST("LAST_MIDDLE_FIRST"),
    FIRST_LAST("FIRST_LAST"),
    LAST_FIRST("LAST_FIRST");

    private final String code;

    NameOrder(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NameOrder fromCode(String code) {
        for (NameOrder order : NameOrder.values()) {
            if (order.getCode().equals(code)) {
                return order;
            }
        }
        return NameOrder.FIRST_LAST_MIDDLE;
    }
}
