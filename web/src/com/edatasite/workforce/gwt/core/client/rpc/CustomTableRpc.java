package com.edatasite.workforce.gwt.core.client.rpc;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CustomTableRpc implements IsSerializable, Key {

    private Integer id;
    private Integer sorder;
    private String uuid;
    private Integer itemID;
    private String itemName;
    private String itemNumber;
    private String description;

    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public LinkedHashMap<String, CompanyCustomFieldItem> getCustomFieldValuesAsMap() {
        if (getItemCustomFields() != null) {
            LinkedHashMap<String, CompanyCustomFieldItem> map = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : getItemCustomFields()) {
                map.put(item.getColumnCode(), item);
            }
            return map;
        }
        return null;
    }

    @Override
    public String getKey() {
        return getId() + "_" + getUuid();
    }
}
