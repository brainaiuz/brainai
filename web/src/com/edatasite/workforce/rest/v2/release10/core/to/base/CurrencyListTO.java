package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.math.BigDecimal;

public class CurrencyListTO extends ResponseData {
    private Integer id;
    private String international_code;
    private BigDecimal exchange_rate;

    public CurrencyListTO() {
    }

    public CurrencyListTO(Integer id, String international_code, BigDecimal exchange_rate) {
        this.id = id;
        this.international_code = international_code;
        this.exchange_rate = exchange_rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInternational_code() {
        return international_code;
    }

    public void setInternational_code(String international_code) {
        this.international_code = international_code;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }
}
