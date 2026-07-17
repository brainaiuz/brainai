package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 7:44:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseLocationsList implements IsSerializable {
    private WarehouseLocationItem[] items;

    public WarehouseLocationItem[] getItems() {
        return items;
    }

    public void setItems(WarehouseLocationItem[] items) {
        this.items = items;
    }
}