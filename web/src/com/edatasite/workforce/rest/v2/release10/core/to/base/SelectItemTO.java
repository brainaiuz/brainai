package com.edatasite.workforce.rest.v2.release10.core.to.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class SelectItemTO extends IdNameTO {

    private String code;

    public SelectItemTO() {
    }

    public SelectItemTO(Integer id, String name, String code) {
        super(id, name);
        this.code = code;
    }

    public SelectItemTO(SelectItem item) {
        setId(item.getId());
        setName(item.getName());
        setCode(item.getDescription());
    }

    public SelectItem toSelectItem() {
        return new SelectItem(getId(), getName(), getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
