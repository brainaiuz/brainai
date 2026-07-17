package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.05.2009
 * Time: 12:05:45
 * To change this template use File | Settings | File Templates.
 */
public class GroupByProjectEntry implements IsSerializable {

    private SelectItem key;
    private TotalCostHours value;
    private SelectItem client;

    public GroupByProjectEntry() {
    }

    public GroupByProjectEntry(SelectItem key, TotalCostHours value) {
        this.key = key;
        this.value = value;
    }

    public SelectItem getKey() {
        return key;
    }

    public void setKey(SelectItem key) {
        this.key = key;
    }

    public TotalCostHours getValue() {
        return value;
    }

    public void setValue(TotalCostHours value) {
        this.value = value;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }
}