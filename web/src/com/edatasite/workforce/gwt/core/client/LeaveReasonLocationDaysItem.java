package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class LeaveReasonLocationDaysItem implements Serializable, IsSerializable {

    private Integer locationId;
    private String locationName;
    private Double leaveDays;
    private boolean applyToAll;
    private DateNonConvertable effectiveFrom;

    public LeaveReasonLocationDaysItem() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public boolean isApplyToAll() {
        return applyToAll;
    }

    public void setApplyToAll(boolean applyToAll) {
        this.applyToAll = applyToAll;
    }

    public DateNonConvertable getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(DateNonConvertable effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
