package com.edatasite.workforce.rest.v2.release10.core.to.base;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anvar Akramov on 11/22/2019.
 */
public class CustomFormValueSaveTO extends ResponseData {
    private Integer objectID;
    private String formID;
    private HashMap<String, ArrayList<CustomTableRpc>> tableItems;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private String statusCode;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public HashMap<String, ArrayList<CustomTableRpc>> getTableItems() {
        return tableItems;
    }

    public void setTableItems(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
        this.tableItems = tableItems;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
