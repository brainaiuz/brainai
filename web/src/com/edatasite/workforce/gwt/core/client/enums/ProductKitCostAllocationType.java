package com.edatasite.workforce.gwt.core.client.enums;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Satimov Murad
 * Date: 2/12/18 10:39 PM
 */
public enum ProductKitCostAllocationType implements IsSerializable {
    BY_AMOUNT(2, "By amount");

    private final Integer id;
    private final String name;

    ProductKitCostAllocationType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProductKitCostAllocationType getById(Integer id) {
        if (id == null) {
            return BY_AMOUNT;
        }
        for (ProductKitCostAllocationType costAllocationType : ProductKitCostAllocationType.values()) {
            if (costAllocationType.getId().equals(id)) {
                return costAllocationType;
            }
        }
        return BY_AMOUNT;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(this.getId(), this.getName());
    }
}
