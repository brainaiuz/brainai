package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSys {

    private Integer type;
    private Integer id;
    private String country;
    private Long sunrise;
    private Long sunset;

    public WeatherSys() {
    }

    public WeatherSys(Integer type, Integer id, String country, Long sunrise, Long sunset) {
        this.type = type;
        this.id = id;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherSys)) return false;

        WeatherSys that = (WeatherSys) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
        if (getSunrise() != null ? !getSunrise().equals(that.getSunrise()) : that.getSunrise() != null) return false;
        if (getSunset() != null ? !getSunset().equals(that.getSunset()) : that.getSunset() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getSunrise() != null ? getSunrise().hashCode() : 0);
        result = 31 * result + (getSunset() != null ? getSunset().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WeatherSys{" +
                "type=" + type +
                ", id=" + id +
                ", country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
