package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Fathulla
 * Date: 19.12.14
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class ProductCustomSettings implements IsSerializable {

    private HashMap<String, String> customSettings;

    private TreeSelectItem[] productCategory;

    private ArrayList<String> companyCurrencyList;

    public TreeSelectItem[] getUnitMeasurementItems() {
        return unitMeasurementItems;
    }

    public void setUnitMeasurementItems(TreeSelectItem[] unitMeasurementItems) {
        this.unitMeasurementItems = unitMeasurementItems;
    }

    private TreeSelectItem[] unitMeasurementItems;

    public HashMap<String, String> getCustomSettings() {
        return customSettings;
    }

    public void setCustomSettings(HashMap<String, String> customSettings) {
        this.customSettings = customSettings;
    }

    public TreeSelectItem[] getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(TreeSelectItem[] productCategory) {
        this.productCategory = productCategory;
    }

    public ArrayList<String> getCompanyCurrencyList() {
        if(companyCurrencyList == null){
            companyCurrencyList = new ArrayList<>();
        }
        return companyCurrencyList;
    }

    public void setCompanyCurrencyList(ArrayList<String> companyCurrencyList) {
        this.companyCurrencyList = companyCurrencyList;
    }
}
