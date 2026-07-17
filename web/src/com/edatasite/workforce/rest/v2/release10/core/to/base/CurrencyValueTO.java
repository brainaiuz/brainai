package com.edatasite.workforce.rest.v2.release10.core.to.base;


import java.math.BigDecimal;

/**
 * Created by Abdurakhmonov Farrukh on 11/28/2017.
 */

public class CurrencyValueTO extends ResponseData {

    private BigDecimal value;
    private String currency;

    public CurrencyValueTO() {
    }

    public CurrencyValueTO(BigDecimal value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
