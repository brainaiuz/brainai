package com.edatasite.workforce.gwt.core.client.rpc.currency;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 08.01.16
 * Time: 20:27:59
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyListItem implements Serializable{
    public static String NAME = "name";
    public static String EXRATE = "exrate";
    public static String UPDATED = "updated";

    private CurrencyItem baseCurrency;
    private CurrencyItem currency;
    private Double exRate;
    private Double xeRate;
    private DateNonConvertable date;
    private DateNonConvertable updateTime;
    private DateNonConvertable xeUpdateTime;
    private boolean fromService;
    private boolean fixedRate;
    private Double exRateInSum;

    public CurrencyListItem() {
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public void setExRate(Double exRate) {
        this.exRate = exRate;
    }

    public void setXeRate(Double xeRate) {
        this.xeRate = xeRate;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateNonConvertable updateTime) {
        this.updateTime = updateTime;
    }

    public DateNonConvertable getXeUpdateTime() {
        return xeUpdateTime;
    }

    public void setXeUpdateTime(DateNonConvertable xeUpdateTime) {
        this.xeUpdateTime = xeUpdateTime;
    }

    public boolean isFromService() {
        return fromService;
    }

    public void setFromService(boolean fromService) {
        this.fromService = fromService;
    }

    public boolean isFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(boolean fixedRate) {
        this.fixedRate = fixedRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        if (isFromService()) {
            setXeRate(exchangeRate);
        } else {
            setExRate(exchangeRate);
        }
    }

    public Double getExchangeRate(boolean fromService) {
        return fromService ? xeRate : exRate;
    }

    public Double getExchangeRate() {
        return isFromService() ? xeRate : exRate;
    }

    public Date getUpdateDate() {
        return isFromService() ? updateTime != null ? updateTime.getNonConvertedDate() : null : date != null ? date.getNonConvertedDate() : null;
    }
    public Double getExRateInSum() {
        return exRateInSum;
    }

    public void setExRateInSum(Double exRateInSum) {
        this.exRateInSum = exRateInSum;
    }
}
