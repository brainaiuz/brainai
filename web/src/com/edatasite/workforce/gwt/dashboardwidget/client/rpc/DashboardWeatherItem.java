package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 01.06.2018 15:11
 */
public class DashboardWeatherItem implements IsSerializable {

    public static final String IMG_01D = "01d";
    public static final String IMG_01N = "01n";
    public static final String IMG_02D = "02d";
    public static final String IMG_02N = "02n";
    public static final String IMG_03D = "03d";
    public static final String IMG_03N = "03n";
    public static final String IMG_04D = "04d";
    public static final String IMG_04N = "04n";
    public static final String IMG_09D = "09d";
    public static final String IMG_09N = "09n";
    public static final String IMG_10D = "10d";
    public static final String IMG_10N = "10n";
    public static final String IMG_11D = "11d";
    public static final String IMG_11N = "11n";
    public static final String IMG_13D = "13d";
    public static final String IMG_13N = "13n";
    public static final String IMG_50D = "50d";
    public static final String IMG_50N = "50n";

    private String location;
    private String description;
    private String date;
    private String tempMax;
    private String icon;
    private String temperature;
    private String humidity;
    private boolean isFahrenheit;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public boolean isFahrenheit() {
        return isFahrenheit;
    }

    public void setFahrenheit(boolean fahrenheit) {
        isFahrenheit = fahrenheit;
    }
}
