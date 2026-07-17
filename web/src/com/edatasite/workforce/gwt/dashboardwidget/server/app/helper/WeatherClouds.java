package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherClouds {

    private Integer all;

    public WeatherClouds() {
    }

    public WeatherClouds(Integer all) {
        this.all = all;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherClouds)) return false;

        WeatherClouds that = (WeatherClouds) o;

        if (getAll() != null ? !getAll().equals(that.getAll()) : that.getAll() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getAll() != null ? getAll().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WeatherClouds{" +
                "all=" + all +
                '}';
    }
}
