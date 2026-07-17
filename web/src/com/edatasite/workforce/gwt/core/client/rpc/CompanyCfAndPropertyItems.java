package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CompanyCfAndPropertyItems implements IsSerializable, Serializable {
    ArrayList<CompanyCustomFieldItem> companyCustomFieldItems;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldItems() {
        return this.companyCustomFieldItems;
    }

    public void setCompanyCustomFieldItems(final ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
        this.companyCustomFieldItems = companyCustomFieldItems;
    }

    public LinkedHashMap<String, FormProperty> getFormPropertyMap() {
        return this.formPropertyMap;
    }

    public void setFormPropertyMap(final LinkedHashMap<String, FormProperty> formPropertyMap) {
        this.formPropertyMap = formPropertyMap;
    }
}
