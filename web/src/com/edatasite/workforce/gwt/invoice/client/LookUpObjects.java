package com.edatasite.workforce.gwt.invoice.client;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 05-Oct-2010
 * Time: 21:26:39
 */
public class LookUpObjects {
    private String code;
    private Integer currencyID;
    private Integer paymentTypeID;
    private String paymentType;

    public LookUpObjects(String code, Integer currencyID, Integer payTypeID, String paymentType) {
        this.code = code;
        this.currencyID = currencyID;
        this.paymentTypeID = payTypeID;
        this.paymentType = paymentType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(Integer paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
