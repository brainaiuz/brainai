package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 17.06.11
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class MCurrencyItem extends MSelectItem {

    private String symbol;
    private String fullName;
    private double value;
    private boolean companyCurrency;

    public MCurrencyItem() {
    }

    public MCurrencyItem(CurrencyItem currencyItem) {
        if (currencyItem != null) {
            this.symbol = currencyItem.getSymbol();
            this.fullName = currencyItem.getFullName();
            this.value = currencyItem.getValue();
            this.companyCurrency = currencyItem.isCompanyCurrency();
            this.setObjectID(currencyItem.getId());
            this.setName(currencyItem.getName());
            this.setDescription(currencyItem.getDescription());
        }
    }

    public CurrencyItem convertToCurrencyItem() {
        CurrencyItem currencyItem = new CurrencyItem();
        currencyItem.setSymbol(this.symbol);
        currencyItem.setFullName(this.fullName);
        currencyItem.setValue(this.value);
        currencyItem.setCompanyCurrency(this.companyCurrency);
        currencyItem.setId(this.getObjectID());
        currencyItem.setName(this.getName());
        currencyItem.setDescription(this.getDescription());

        return currencyItem;
    }

    public static Boolean convert(MCurrencyItem mCurrencyItem, CurrencyItem currencyItem, boolean toCurrencyItem) {
        if (mCurrencyItem == null || currencyItem == null) {
            return null;
        }

        try {
            if (toCurrencyItem) {
                currencyItem.setSymbol(mCurrencyItem.getSymbol());
                currencyItem.setFullName(mCurrencyItem.getFullName());
                currencyItem.setValue(mCurrencyItem.getValue());
                currencyItem.setCompanyCurrency(mCurrencyItem.isCompanyCurrency());
            } else {
                mCurrencyItem.setSymbol(currencyItem.getSymbol());
                mCurrencyItem.setFullName(currencyItem.getFullName());
                mCurrencyItem.setValue(currencyItem.getValue());
                mCurrencyItem.setCompanyCurrency(currencyItem.isCompanyCurrency());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
