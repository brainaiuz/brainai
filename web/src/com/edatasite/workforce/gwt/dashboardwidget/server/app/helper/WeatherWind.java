package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherWind {

    private Float speed;
    private Integer deg;

    public WeatherWind() {
    }

    public WeatherWind(Float speed, Integer deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherWind)) return false;

        WeatherWind that = (WeatherWind) o;

        if (getSpeed() != null ? !getSpeed().equals(that.getSpeed()) : that.getSpeed() != null) return false;
        if (getDeg() != null ? !getDeg().equals(that.getDeg()) : that.getDeg() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getSpeed() != null ? getSpeed().hashCode() : 0;
        result = 31 * result + (getDeg() != null ? getDeg().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WeatherWind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
