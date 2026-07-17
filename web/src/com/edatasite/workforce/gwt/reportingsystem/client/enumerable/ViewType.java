package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public enum ViewType {

    Collapsible(0),
    Expandable(1);

    private Integer id;

    ViewType(int id) {
    }

    public static SelectItem[] getAsSelectItems() {
        SelectItem[] items = new SelectItem[values().length];
        int i = 0;
        for (ViewType type : values()) {
            items[i++] = new SelectItem(type.id, type.name());
        }
        return items;
    }

    public int getId() {
        return id;
    }

}
