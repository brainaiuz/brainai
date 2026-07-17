package com.edatasite.workforce.gwt.backend.client.rpc.enums;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * User: Murad Satimov
 * Date: 8/26/17 4:50 PM
 */
public enum FileTranserTypeEnum {
    FROM_AMAZON_TO_LOCAL(1, "Amazon -> Local"),
    FROM_AMAZON_TO_AMAZON(2, "Amazon -> Amazon");

    private Integer id;
    private String name;

    FileTranserTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectItem toSelectItem() {
        return new SelectItem(this.getId(), this.getName());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
