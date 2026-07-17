package com.edatasite.workforce.gwt.core.client.rpc.currency;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shohruh on 2/15/2016.
 */
public class CurrencyLayerItem implements Serializable{
    private double rate;
    private Date lastUpdateTime;

    public CurrencyLayerItem() {
    }

    public CurrencyLayerItem(double rate, Date lastUpdateTime) {
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
