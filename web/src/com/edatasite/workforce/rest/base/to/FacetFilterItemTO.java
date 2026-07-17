package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek on 01.02.2015.
 */
public class FacetFilterItemTO implements IsSerializable {
    String key;
    String name;
    String description;

    public FacetFilterItemTO() {
    }

    public FacetFilterItemTO(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public FacetFilterItemTO(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
