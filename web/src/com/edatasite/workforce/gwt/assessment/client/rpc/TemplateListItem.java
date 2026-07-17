package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TemplateListItem implements IsSerializable {
    public static final String NAME = "name";
    public static final String ACTION = "action";

    private String name;
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
