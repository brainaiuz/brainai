package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: unni
 * Date: Jan 7, 2010
 * Time: 4:09:06 PM
 */
public class TimeSlot implements IsSerializable {
    private String startHour;
    private String startMin;
    private String endHour;
    private String endMin;

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }
}