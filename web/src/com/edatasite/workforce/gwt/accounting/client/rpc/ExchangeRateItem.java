package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * User: iabdullo
 * Date: 16.09.14 18:19
 */
public class ExchangeRateItem implements IsSerializable {

    private Integer yearNum;
    private Integer monthNum;
    private LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> currencyExchangeRateData;

    public Integer getYearNum() {
        return yearNum;
    }

    public void setYearNum(Integer yearNum) {
        this.yearNum = yearNum;
    }

    public Integer getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(Integer monthNum) {
        this.monthNum = monthNum;
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> getCurrencyExchangeRateData() {
        return currencyExchangeRateData;
    }

    public void setCurrencyExchangeRateData(LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> currencyExchangeRateData) {
        this.currencyExchangeRateData = currencyExchangeRateData;
    }
}
