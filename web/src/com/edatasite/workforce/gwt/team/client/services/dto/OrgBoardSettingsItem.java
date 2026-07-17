package com.edatasite.workforce.gwt.team.client.services.dto;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class OrgBoardSettingsItem {

    private Integer objectId;
    private Boolean showEmployees;
    private Boolean showShortDescription;
    private Boolean showDescription;
    private Boolean showGoals;
    private Integer employeeId;
    private Integer locationId;
    private SelectItem location;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Boolean getShowEmployees() {
        return showEmployees;
    }

    public void setShowEmployees(Boolean showEmployees) {
        this.showEmployees = showEmployees;
    }

    public Boolean getShowShortDescription() {
        return showShortDescription;
    }

    public void setShowShortDescription(Boolean showShortDescription) {
        this.showShortDescription = showShortDescription;
    }

    public Boolean getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(Boolean showDescription) {
        this.showDescription = showDescription;
    }

    public Boolean getShowGoals() {
        return showGoals;
    }

    public void setShowGoals(Boolean showGoals) {
        this.showGoals = showGoals;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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
}
