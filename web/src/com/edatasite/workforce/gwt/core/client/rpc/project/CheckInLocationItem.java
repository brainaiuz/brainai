package com.edatasite.workforce.gwt.core.client.rpc.project;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CheckInLocationItem  implements IsSerializable {
    private Integer id;
    private String latitude;
    private String longitude;
    private String radius;

    public CheckInLocationItem() {
    }

    public CheckInLocationItem(Integer id, String latitude, String longitude, String radius) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
