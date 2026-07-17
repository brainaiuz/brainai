package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: 10.11.2009
 * Time: 20:58:20
 * To change this template use File | Settings | File Templates.
 */
public class LocationItem implements IsSerializable {
    private String id = "";
    private String type = "";
    private String name = "";
    private Integer ID;

    public LocationItem() {

    }

    public LocationItem(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
