package com.edatasite.workforce.gwt.core.client.rpc.currency;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class CurrencyItem extends SelectItem {

    private String symbol;
    private String fullName;
    private double value;
    private boolean companyCurrency;
    private String frname;

    public CurrencyItem() {

    }

    public CurrencyItem(Integer id, String name, String symbol, String fullName) {
        this(id, name, symbol);
        this.fullName = fullName;
    }

    public CurrencyItem(Integer id, String name, String symbol) {
        super(id, name);
        this.symbol = symbol;
    }

    public CurrencyItem(Integer id, String name) {
        super(id, name);
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isCompanyCurrency() {
        return companyCurrency;
    }

    public void setCompanyCurrency(boolean companyCurrency) {
        this.companyCurrency = companyCurrency;
    }

    public String getFrname() {
        return frname;
    }

    public void setFrname(String frname) {
        this.frname = frname;
    }
}