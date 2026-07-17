package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 15.05.2009
 * Time: 20:31:09
 * To change this template use File | Settings | File Templates.
 */
public class TimeSpentRateValue implements IsSerializable {

    private Integer timeSpent = 0;

    private Double clientChargeRate;

    private String timesheetDescription;

    private Integer[] entryIds;

    public TimeSpentRateValue() {
    }

    public TimeSpentRateValue(Integer timeSpent, Double clientChargeRate) {
        this.timeSpent = timeSpent;
        this.clientChargeRate = clientChargeRate;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
    }

    public String getTimesheetDescription() {
        return timesheetDescription;
    }

    public void setTimesheetDescription(String timesheetDescription) {
        this.timesheetDescription = timesheetDescription;
    }
}