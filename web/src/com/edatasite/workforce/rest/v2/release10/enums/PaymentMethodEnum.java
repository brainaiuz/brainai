package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum PaymentMethodEnum {
    CASH(0, "CASH"),
    CHECK(1, "CHECK"),
    WIRE_TRANSFER(2, "WIRE_TRANSFER");

    private Integer id;
    private String type;

    PaymentMethodEnum(Integer id, String type) {
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

    public static String getPaymentMethod(Integer id) {
        if (id == null) {
            return null;
        }
        if (id == 0) {
            return PaymentMethodEnum.CASH.getType();
        }
        if (id == 1) {
            return PaymentMethodEnum.CHECK.getType();
        }
        if (id == 2) {
            return PaymentMethodEnum.WIRE_TRANSFER.getType();
        }
        return null;
    }
}
