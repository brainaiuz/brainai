package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/3/12
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyAdjustmentData implements IsSerializable{
    private String memo;
    private DateNonConvertable date;
    private Integer currencyID;
    private BigDecimal exchangeRate;

    private ArrayList<CurrencyAdjustmentItem> items;

    public CurrencyAdjustmentData() {
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public ArrayList<CurrencyAdjustmentItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(ArrayList<CurrencyAdjustmentItem> items) {
        this.items = items;
    }
}
