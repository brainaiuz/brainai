package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/11/17.
 */
public class Location extends FacebookObject {
    private Address location;
    private String id;

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
