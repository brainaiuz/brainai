package com.edatasite.workforce.rest.base.to;

import java.io.Serializable;

public class InOutItemTO implements Serializable {

    private String[] inHours;
    private String[] outHours;

    private Integer actualInHour;
    private Integer timeslotStartHour;
    private Integer timeslotEndHour;
    private Integer overtimeHour;


    public InOutItemTO() {
    }

    public void setInHours(String[] inHours) {
        this.inHours = inHours;
    }

    public void setOutHours(String[] outHours) {
        this.outHours = outHours;
    }

    public void setTimeslotStartHour(Integer timeslotStartHour) {
        this.timeslotStartHour = timeslotStartHour;
    }

    public void setTimeslotEndHour(Integer timeslotEndHour) {
        this.timeslotEndHour = timeslotEndHour;
    }

    public void setOvertimeHour(Integer overtimeHour) {
        this.overtimeHour = overtimeHour;
    }

    public void setActualInHour(Integer actualInHour) {
        this.actualInHour = actualInHour;
    }

    public String[] getInHours() {
        return inHours;
    }

    public String[] getOutHours() {
        return outHours;
    }

    public Integer getActualInHour() {
        return actualInHour;
    }

    public Integer getTimeslotStartHour() {
        return timeslotStartHour;
    }

    public Integer getTimeslotEndHour() {
        return timeslotEndHour;
    }

    public Integer getOvertimeHour() {
        return overtimeHour;
    }
}
