package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ConvertItem implements IsSerializable {

    private String code;
    private String name;
    private Integer entityId;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getEntityId() {
        return this.entityId;
    }

    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
}