package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KeyValueStruct implements IsSerializable {

    private String key;
    private Integer id;
    private String value;
    private Integer type;

    public KeyValueStruct() {

    }

    public KeyValueStruct(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueStruct(String key, Integer id, String value) {
        this.key = key;
        this.id = id;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
