package com.edatasite.workforce.core.domain.enums;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public enum SalesType {
    B2B(1),
    B2C(2);

    private final Integer id;

    SalesType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static SelectItem[] toSelectItems() {
        SalesType[] values = SalesType.values();
        SelectItem[] items = new SelectItem[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = new SelectItem(values[i].getId(), values[i].name());
        }
        return items;
    }
}
