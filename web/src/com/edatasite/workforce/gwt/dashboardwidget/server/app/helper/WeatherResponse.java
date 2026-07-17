package com.edatasite.workforce.gwt.dashboardwidget.server.app.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 * User: Abror Abdukadirov
 * Date: 01.06.2018 14:57
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private WeatherCoord coord;
    private String name;
    private Weather[] weather;
    private Main main;
    private String base;
    private Integer visibility;
    private WeatherWind wind;
    private WeatherClouds clouds;
    private Long dt;
    private WeatherSys sys;
    private Integer timezone;
    private Long id;
    private Integer cod;

    public WeatherResponse() {
    }

    public WeatherResponse(WeatherCoord coord, String name, Weather[] weather, Main main, String base, Integer visibility, WeatherWind wind, WeatherClouds clouds, Long dt, WeatherSys sys, Integer timezone, Long id, Integer cod) {
        this.coord = coord;
        this.name = name;
        this.weather = weather;
        this.main = main;
        this.base = base;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.cod = cod;
    }

    public WeatherCoord getId() {
        return coord;
    }

    public void setId(WeatherCoord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public WeatherCoord getCoord() {
        return coord;
    }

    public void setCoord(WeatherCoord coord) {
        this.coord = coord;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public WeatherWind getWind() {
        return wind;
    }

    public void setWind(WeatherWind wind) {
        this.wind = wind;
    }

    public WeatherClouds getClouds() {
        return clouds;
    }

    public void setClouds(WeatherClouds clouds) {
        this.clouds = clouds;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public WeatherSys getSys() {
        return sys;
    }

    public void setSys(WeatherSys sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherResponse)) return false;

        WeatherResponse that = (WeatherResponse) o;

        if (getCoord() != null ? !getCoord().equals(that.getCoord()) : that.getCoord() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getWeather(), that.getWeather())) return false;
        if (getMain() != null ? !getMain().equals(that.getMain()) : that.getMain() != null) return false;
        if (getBase() != null ? !getBase().equals(that.getBase()) : that.getBase() != null) return false;
        if (getVisibility() != null ? !getVisibility().equals(that.getVisibility()) : that.getVisibility() != null)
            return false;
        if (getWind() != null ? !getWind().equals(that.getWind()) : that.getWind() != null) return false;
        if (getClouds() != null ? !getClouds().equals(that.getClouds()) : that.getClouds() != null) return false;
        if (getDt() != null ? !getDt().equals(that.getDt()) : that.getDt() != null) return false;
        if (getSys() != null ? !getSys().equals(that.getSys()) : that.getSys() != null) return false;
        if (getTimezone() != null ? !getTimezone().equals(that.getTimezone()) : that.getTimezone() != null)
            return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getCod() != null ? !getCod().equals(that.getCod()) : that.getCod() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getCoord() != null ? getCoord().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getWeather());
        result = 31 * result + (getMain() != null ? getMain().hashCode() : 0);
        result = 31 * result + (getBase() != null ? getBase().hashCode() : 0);
        result = 31 * result + (getVisibility() != null ? getVisibility().hashCode() : 0);
        result = 31 * result + (getWind() != null ? getWind().hashCode() : 0);
        result = 31 * result + (getClouds() != null ? getClouds().hashCode() : 0);
        result = 31 * result + (getDt() != null ? getDt().hashCode() : 0);
        result = 31 * result + (getSys() != null ? getSys().hashCode() : 0);
        result = 31 * result + (getTimezone() != null ? getTimezone().hashCode() : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getCod() != null ? getCod().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "coord=" + coord +
                ", name='" + name + '\'' +
                ", weather=" + Arrays.toString(weather) +
                ", main=" + main +
                ", base='" + base + '\'' +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", id=" + id +
                ", cod=" + cod +
                '}';
    }
}
