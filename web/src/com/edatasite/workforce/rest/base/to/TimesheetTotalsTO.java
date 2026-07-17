package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Umidbek.
 */
public class TimesheetTotalsTO implements IsSerializable {
    private ArrayList<Integer> actualDaily = new ArrayList<>();
    private ArrayList<Integer> plannedDaily = new ArrayList<>();

    private Integer actualWeekly;
    private Integer plannedWeekly;
    private Integer actualMonthly;
    private Integer plannedMonthly;

    public TimesheetTotalsTO() {
    }

    public ArrayList<Integer> getActualDaily() {
        return actualDaily;
    }

    public void setActualDaily(ArrayList<Integer> actualDaily) {
        this.actualDaily = actualDaily;
    }

    public ArrayList<Integer> getPlannedDaily() {
        return plannedDaily;
    }

    public void setPlannedDaily(ArrayList<Integer> plannedDaily) {
        this.plannedDaily = plannedDaily;
    }

    public Integer getActualWeekly() {
        return actualWeekly;
    }

    public void setActualWeekly(Integer actualWeekly) {
        this.actualWeekly = actualWeekly;
    }

    public Integer getPlannedWeekly() {
        return plannedWeekly;
    }

    public void setPlannedWeekly(Integer plannedWeekly) {
        this.plannedWeekly = plannedWeekly;
    }

    public Integer getActualMonthly() {
        return actualMonthly;
    }

    public void setActualMonthly(Integer actualMonthly) {
        this.actualMonthly = actualMonthly;
    }

    public Integer getPlannedMonthly() {
        return plannedMonthly;
    }

    public void setPlannedMonthly(Integer plannedMonthly) {
        this.plannedMonthly = plannedMonthly;
    }
}
