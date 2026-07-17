package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 1, 2011
 * Time: 9:00:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductImportFillingData implements IsSerializable {

    private SelectItem[] categories;
    private SelectItem[] unitMeasurements;
    private SelectItem[] vendors;
    private SelectItem[] brands;
    private SelectItem[] warehouses;
    private SelectItem[] priceLevels;
    private SelectItem[] locations;
    private SelectItem[] products;
    private TaxItem[] taxes;

    private Integer defaultWarehouseId;
    private Integer defaultLocationId;
    private SelectItem defaultAccount;
    private SelectItem defaultCogsAccount;
    private SelectItem defaultAssetAccount;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;

    public ProductImportFillingData() {
    }

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }

    public SelectItem[] getUnitMeasurements() {
        return unitMeasurements;
    }

    public void setUnitMeasurements(SelectItem[] unitMeasurements) {
        this.unitMeasurements = unitMeasurements;
    }

    public SelectItem[] getVendors() {
        return vendors;
    }

    public void setVendors(SelectItem[] vendors) {
        this.vendors = vendors;
    }

    public SelectItem[] getBrands() {
        return brands;
    }

    public void setBrands(SelectItem[] brands) {
        this.brands = brands;
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

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public SelectItem[] getProducts() {
        return products;
    }

    public void setProducts(SelectItem[] products) {
        this.products = products;
    }

    public TaxItem[] getTaxes() {
        return taxes;
    }

    public void setTaxes(TaxItem[] taxes) {
        this.taxes = taxes;
    }

    public Integer getDefaultWarehouseId() {
        return defaultWarehouseId;
    }

    public void setDefaultWarehouseId(Integer defaultWarehouseId) {
        this.defaultWarehouseId = defaultWarehouseId;
    }

    public Integer getDefaultLocationId() {
        return defaultLocationId;
    }

    public void setDefaultLocationId(Integer defaultLocationId) {
        this.defaultLocationId = defaultLocationId;
    }

    public SelectItem getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(SelectItem defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public SelectItem getDefaultCogsAccount() {
        return defaultCogsAccount;
    }

    public void setDefaultCogsAccount(SelectItem defaultCogsAccount) {
        this.defaultCogsAccount = defaultCogsAccount;
    }

    public SelectItem getDefaultAssetAccount() {
        return defaultAssetAccount;
    }

    public void setDefaultAssetAccount(SelectItem defaultAssetAccount) {
        this.defaultAssetAccount = defaultAssetAccount;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }
}
