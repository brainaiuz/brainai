package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/28/12
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddAccountData implements IsSerializable, ListingCustomFields {
    private CurrencyItem[] currencyItems;
    private CurrencyItem baseCurrency;
    private AddAccountItem accountItem;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldsMap;


    public AddAccountData() {
    }

    public CurrencyItem[] getCurrencyItems() {
        return currencyItems;
    }

    public void setCurrencyItems(CurrencyItem[] currencyItems) {
        this.currencyItems = currencyItems;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public AddAccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(AddAccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

}
