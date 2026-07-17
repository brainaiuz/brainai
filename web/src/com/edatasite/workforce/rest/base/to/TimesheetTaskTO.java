package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Umidbek.
 */
public class TimesheetTaskTO implements IsSerializable {
    private TaskTO task;
    private Integer totalMinutes;
    private Integer weeklyTotal;
    private Integer monthlyTotal;

    private ArrayList<TimesheetTaskEntryTO> entries = new ArrayList<>();

    public TimesheetTaskTO() {
    }

    public TaskTO getTask() {
        return task;
    }

    public void setTask(TaskTO task) {
        this.task = task;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public ArrayList<TimesheetTaskEntryTO> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TimesheetTaskEntryTO> entries) {
        this.entries = entries;
    }

    public Integer getWeeklyTotal() {
        return weeklyTotal;
    }

    public void setWeeklyTotal(Integer weeklyTotal) {
        this.weeklyTotal = weeklyTotal;
    }

    public Integer getMonthlyTotal() {
        return monthlyTotal;
    }

    public void setMonthlyTotal(Integer monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }
}
