package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/9/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportProductInitItem implements IsSerializable {

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<String> currencyList;
    private SelectItem[] warehouses;
    private SelectItem[] priceLevels;


    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public ArrayList<String> getCurrencyList() {
        if(currencyList == null){
            currencyList = new ArrayList<>();
        }
        return currencyList;
    }

    public void setCurrencyList(ArrayList<String> currencyList) {
        this.currencyList = currencyList;
    }

    public SelectItem[] getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(SelectItem[] warehouses) {
        this.warehouses = warehouses;
    }

    public SelectItem[] getPriceLevels() {
        return priceLevels;
    }

    public void setPriceLevels(SelectItem[] priceLevels) {
        this.priceLevels = priceLevels;
    }
}
