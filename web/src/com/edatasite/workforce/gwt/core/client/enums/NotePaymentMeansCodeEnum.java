package com.edatasite.workforce.gwt.core.client.enums;

public enum NotePaymentMeansCodeEnum {
    IN_CASH("In cash", 10),
    CREDIT("Credit", 30),
    PAYMENT_TO_BANK_ACCOUNT("Payment to bank account", 42),
    BANK_CARD("Bank card", 48),
    INSTRUMENT_NOT_DEFINED("Instrument not defined", 1);

    private String name;
    private Integer paymentCode;

    NotePaymentMeansCodeEnum(String name, Integer paymentCode) {
        this.name = name;
        this.paymentCode = paymentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(Integer paymentCode) {
        this.paymentCode = paymentCode;
    }
}
