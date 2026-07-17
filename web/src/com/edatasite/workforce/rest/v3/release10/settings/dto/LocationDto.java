package com.edatasite.workforce.rest.v3.release10.settings.dto;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import java.util.List;

public class LocationDto {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private List<? extends CustomFieldRequest> customFields;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }
}
