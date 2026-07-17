package com.workforcetrack.mobile.rpc.client;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 04.07.11
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public class MListArray {
    private List<MSelectItem> countryList;

    private List<MSelectItem> regionList;

    private List<MSelectItem> currencyList;

    public List<MSelectItem> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<MSelectItem> countryList) {
        this.countryList = countryList;
    }

    public List<MSelectItem> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<MSelectItem> regionList) {
        this.regionList = regionList;
    }

    public List<MSelectItem> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<MSelectItem> currencyList) {
        this.currencyList = currencyList;
    }
}
