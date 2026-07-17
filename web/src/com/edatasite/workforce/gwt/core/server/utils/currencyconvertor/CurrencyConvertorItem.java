package com.edatasite.workforce.gwt.core.server.utils.currencyconvertor;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.11.2008
 * Time: 14:01:34
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyConvertorItem {
    private double rate;
    private Date lastUpdateTime;

    public CurrencyConvertorItem(double rate, Date lastUpdateTime) {
        this.rate = rate;
        this.lastUpdateTime = lastUpdateTime;
    }

    public double getRate() {
        return rate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
}
