package com.edatasite.workforce.gwt.core.client.rpc.website;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;

public class AttendanceTerminal implements Serializable {
    public static final String DEVICE_ID = "deviceId";
    public static final String BRANCH_NAME = "branchName";
    public static final String DYNAMIC_STATUS = "dynamicStatus";

    private Integer id;
    private Integer locationId;
    private SelectItem location;
    private String companyUniqueID;
    private String companyBranchName;
    private Boolean dynamicStatus;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private Long externalId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public String getCompanyUniqueID() {
        return companyUniqueID;
    }

    public void setCompanyUniqueID(String companyUniqueID) {
        this.companyUniqueID = companyUniqueID;
    }

    public String getCompanyBranchName() {
        return companyBranchName;
    }

    public void setCompanyBranchName(String companyBranchName) {
        this.companyBranchName = companyBranchName;
    }

    public Boolean getDynamicStatus() {
        return dynamicStatus;
    }

    public void setDynamicStatus(Boolean dynamicStatus) {
        this.dynamicStatus = dynamicStatus;
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

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getRadius() {
        return radius;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
}
