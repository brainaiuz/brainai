package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09/10/12
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class ConsolidationCompanyItem implements IsSerializable {

    private SelectItem[] countryItem;
    private SelectItem[] currencyItem;
    private SelectItem baseCurrency;
    private HashMap<Integer, ArrayList<SelectItem>> statesMap;

    public SelectItem[] getCountryItem() {
        return countryItem;
    }

    public void setCountryItem(SelectItem[] countryItem) {
        this.countryItem = countryItem;
    }

    public SelectItem[] getCurrencyItem() {
        return currencyItem;
    }

    public void setCurrencyItem(SelectItem[] currencyItem) {
        this.currencyItem = currencyItem;
    }

    public SelectItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(SelectItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public HashMap<Integer, ArrayList<SelectItem>> getStatesMap() {
        if (statesMap == null) {
            statesMap = new HashMap<>();
        }
        return statesMap;
    }

    public void setStatesMap(HashMap<Integer, ArrayList<SelectItem>> statesMap) {
        this.statesMap = statesMap;
    }
}
