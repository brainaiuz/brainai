package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

/**
 * Created by umakarimov on 10/6/15.
 */
public class Office365GeoCoordinates extends Office365BaseItem {

    private Double altitude;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private Double altitudeAccuracy;

    public Office365GeoCoordinates() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#GeoCoordinates
     */
    public Office365GeoCoordinates(JSONObject data) {
        this.altitude = this.getDouble(data, "Altitude");
        this.latitude = this.getDouble(data, "Latitude");
        this.longitude = this.getDouble(data, "Longitude");
        this.accuracy = this.getDouble(data, "Accuracy");
        this.altitudeAccuracy = this.getDouble(data, "AltitudeAccuracy");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("Altitude", this.altitude);
        json.put("Latitude", this.latitude);
        json.put("Longitude", this.longitude);
        json.put("Accuracy", this.accuracy);
        json.put("AltitudeAccuracy", this.altitudeAccuracy);

        return json;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
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

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(Double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }
}
