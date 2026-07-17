package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 13/11/12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeRateItemMQ implements Serializable {

    private Integer month;
    private Integer year;
    private Map<Integer, Map<Integer, BigDecimal>> curencyExchangeRate;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Map<Integer, Map<Integer, BigDecimal>> getCurencyExchangeRate() {
        if (curencyExchangeRate == null) {
            curencyExchangeRate = new HashMap<>();
        }
        return curencyExchangeRate;
    }

    public void setCurencyExchangeRate(Map<Integer, Map<Integer, BigDecimal>> curencyExchangeRate) {
        this.curencyExchangeRate = curencyExchangeRate;
    }
}
