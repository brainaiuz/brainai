package com.edatasite.workforce.gwt.core.client.rpc.facet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jun-2010
 * Time: 20:57:09
 */
public class SaveFilterSelectItems implements IsSerializable {
    private Integer defaultFilterID;
    private HashMap<Integer, Boolean> publicFilds;
    private SelectItem[] items;

    public SaveFilterSelectItems(){}

    public SaveFilterSelectItems(Integer defaultFilterID, HashMap<Integer, Boolean> publicFilds, SelectItem[] items) {
        this.defaultFilterID = defaultFilterID;
        this.publicFilds = publicFilds;
        this.items = items;
    }

    public Integer getDefaultFilterID() {
        return defaultFilterID;
    }

    public void setDefaultFilterID(Integer defaultFilterID) {
        this.defaultFilterID = defaultFilterID;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public HashMap<Integer, Boolean> getPublicFilds() {
        return publicFilds;
    }

    public void setPublicFilds(HashMap<Integer, Boolean> publicFilds) {
        this.publicFilds = publicFilds;
    }
}
