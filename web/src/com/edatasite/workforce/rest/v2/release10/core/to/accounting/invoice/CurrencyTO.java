package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class CurrencyTO extends ResponseData {
    private Integer currency_id;
    private String currency_name;

    public CurrencyTO() {
    }

    public CurrencyTO(Integer currency_id, String currency_name) {
        this.currency_id = currency_id;
        this.currency_name = currency_name;
    }

    public Integer getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }
}
