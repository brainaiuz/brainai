package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCoord {

    private double lon;
    private double lat;

    public WeatherCoord() {
    }

    public WeatherCoord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherCoord)) return false;

        WeatherCoord that = (WeatherCoord) o;

        if (Double.compare(that.getLon(), getLon()) != 0) return false;
        if (Double.compare(that.getLat(), getLat()) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLon());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WeatherCoord{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
