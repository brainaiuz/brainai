package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.form.CustomFormAttributeItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CompanyCFAndFormItems implements IsSerializable, Serializable {
    ArrayList<CompanyCustomFieldItem> companyCustomFieldItems;
    ArrayList<CompanyCustomFieldItem> tableCustomFieldItem;
    private FormItems formItems;
    private FormItems formTimerItems;
    private Map<String, ColumnConfigs[]> columnConfigs;
    private ArrayList<CustomFormAttributeItem> attributeItems;
    private HashMap<String, SelectItem> cfItemTableSettings;

    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldItems() {
        return this.companyCustomFieldItems;
    }

    public void setCompanyCustomFieldItems(final ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
        this.companyCustomFieldItems = companyCustomFieldItems;
    }

    public FormItems getFormItems() {
        return this.formItems;
    }

    public void setFormItems(final FormItems formItems) {
        this.formItems = formItems;
    }

    public Map<String, ColumnConfigs[]> getColumnConfigs() {
        return this.columnConfigs;
    }

    public void setColumnConfigs(final Map<String, ColumnConfigs[]> columnConfigs) {
        this.columnConfigs = columnConfigs;
    }

    public ArrayList<CustomFormAttributeItem> getAttributeItems() {
        return this.attributeItems;
    }

    public void setAttributeItems(final ArrayList<CustomFormAttributeItem> attributeItems) {
        this.attributeItems = attributeItems;
    }

    public ArrayList<CompanyCustomFieldItem> getTableCustomFieldItem() {
        return this.tableCustomFieldItem;
    }

    public void setTableCustomFieldItem(final ArrayList<CompanyCustomFieldItem> tableCustomFieldItem) {
        this.tableCustomFieldItem = tableCustomFieldItem;
    }

    public FormItems getFormTimerItems() {
        return this.formTimerItems;
    }

    public void setFormTimerItems(final FormItems formTimerItems) {
        this.formTimerItems = formTimerItems;
    }

    public HashMap<String, SelectItem> getCfItemTableSettings() {
        return cfItemTableSettings;
    }

    public void setCfItemTableSettings(HashMap<String, SelectItem> cfItemTableSettings) {
        this.cfItemTableSettings = cfItemTableSettings;
    }
}

