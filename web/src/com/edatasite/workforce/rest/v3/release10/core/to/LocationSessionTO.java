package com.edatasite.workforce.rest.v3.release10.core.to;

public class LocationSessionTO {
    private String sessionId;
    private Integer locationId;

    public LocationSessionTO(String sessionId, Integer locationId) {
        this.sessionId = sessionId;
        this.locationId = locationId;
    }

    public LocationSessionTO() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
