package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek on 03.02.2015.
 */
public class SelectItemTO implements IsSerializable {
    Integer id;
    String name;
    String code;
    String description;

    public SelectItemTO() {
    }

    public SelectItemTO(SelectItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.code = item.getCode();
    }

    public SelectItemTO(Integer id) {
        this.id = id;
    }

    public SelectItemTO(String name) {
        this.name = name;
    }

    public SelectItemTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectItemTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public SelectItemTO(Integer id, String name, String code, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public SelectItemTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public SelectItem wrap(SelectItemTO selectItemTO) {
        return new SelectItem(selectItemTO.getId(), selectItemTO.getName(), selectItemTO.getDescription());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}