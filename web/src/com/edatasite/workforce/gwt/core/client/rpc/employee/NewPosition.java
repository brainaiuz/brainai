package com.edatasite.workforce.gwt.core.client.rpc.employee;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NewPosition implements IsSerializable {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}