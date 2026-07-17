package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 7:25:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseList implements IsSerializable {
    private WarehouseItem[] items;

    public WarehouseItem[] getItems() {
        return items;
    }

    public void setItems(WarehouseItem[] items) {
        this.items = items;
    }
}
