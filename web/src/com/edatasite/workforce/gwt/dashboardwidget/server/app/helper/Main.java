package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User: Abror Abdukadirov
 * Date: 01.06.2018 15:02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

    private Integer humidity;
    private Float temp;
    private Float temp_max;
    private Float temp_min;
    @JsonIgnore
    private Float feels_like;
    private Integer pressure;

    public Main() {
    }

    public Main(Integer humidity, Float temp, Float temp_max, Float temp_min, Float feels_like, Integer pressure) {
        this.humidity = humidity;
        this.temp = temp;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.feels_like = feels_like;
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(Float temp_max) {
        this.temp_max = temp_max;
    }

    public Float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(Float temp_min) {
        this.temp_min = temp_min;
    }

    public Float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(Float feels_like) {
        this.feels_like = feels_like;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Main)) return false;

        Main main = (Main) o;

        if (getHumidity() != null ? !getHumidity().equals(main.getHumidity()) : main.getHumidity() != null)
            return false;
        if (getTemp() != null ? !getTemp().equals(main.getTemp()) : main.getTemp() != null) return false;
        if (getTemp_max() != null ? !getTemp_max().equals(main.getTemp_max()) : main.getTemp_max() != null)
            return false;
        if (getTemp_min() != null ? !getTemp_min().equals(main.getTemp_min()) : main.getTemp_min() != null)
            return false;
        if (getFeels_like() != null ? !getFeels_like().equals(main.getFeels_like()) : main.getFeels_like() != null)
            return false;
        if (getPressure() != null ? !getPressure().equals(main.getPressure()) : main.getPressure() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getHumidity() != null ? getHumidity().hashCode() : 0;
        result = 31 * result + (getTemp() != null ? getTemp().hashCode() : 0);
        result = 31 * result + (getTemp_max() != null ? getTemp_max().hashCode() : 0);
        result = 31 * result + (getTemp_min() != null ? getTemp_min().hashCode() : 0);
        result = 31 * result + (getFeels_like() != null ? getFeels_like().hashCode() : 0);
        result = 31 * result + (getPressure() != null ? getPressure().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Main{" +
                "humidity=" + humidity +
                ", temp=" + temp +
                ", temp_max=" + temp_max +
                ", temp_min=" + temp_min +
                ", feels_like=" + feels_like +
                ", pressure=" + pressure +
                '}';
    }
}
