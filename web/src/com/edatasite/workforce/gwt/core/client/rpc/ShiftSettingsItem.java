package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

public class ShiftSettingsItem implements Serializable {
    private Integer id;
    private String name;
    private String shortName;

    private ReferenceLocale referenceLocale;

    private String hexColor;
    private String description;
    private String interval;
    private int[] times;
    private int[] lunchTimes;
    private int[] coffeeTimes;
    private String excludedDays;

    public ReferenceLocale getReferenceLocale() {
        return referenceLocale;
    }

    public void setReferenceLocale(ReferenceLocale referenceLocale) {
        this.referenceLocale = referenceLocale;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] times) {
        this.times = times;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int[] getLunchTimes() {
        return lunchTimes;
    }

    public void setLunchTimes(int[] lunchTimes) {
        this.lunchTimes = lunchTimes;
    }

    public int[] getCoffeeTimes() {
        return coffeeTimes;
    }

    public void setCoffeeTimes(int[] coffeeTimes) {
        this.coffeeTimes = coffeeTimes;
    }

    public String getExcludedDays() {
        return excludedDays;
    }

    public void setExcludedDays(String excludedDays) {
        this.excludedDays = excludedDays;
    }
}
