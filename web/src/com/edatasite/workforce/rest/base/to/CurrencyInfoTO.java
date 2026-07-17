package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Abdurakhmonov Farrukh on 03/16/2018.
 */
public class CurrencyInfoTO extends ResponseData {
    private Integer id;
    private String code;
    private BigDecimal exchange_rate;

    public CurrencyInfoTO() {
    }

    public CurrencyInfoTO(Integer id, String code, BigDecimal exchange_rate) {
        this.id = id;
        this.code =code;
        this.exchange_rate = exchange_rate;
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

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }
}
