package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Umidbek.
 */
public class TimesheetProjectTO implements IsSerializable {

    private ProjectTO project;
    private Integer weeklyTotal;
    private Integer monthlyTotal;

    private ArrayList<TimesheetTaskTO> tasks = new ArrayList<>();

    public TimesheetProjectTO() {
    }

    public ProjectTO getProject() {
        return project;
    }

    public void setProject(ProjectTO project) {
        this.project = project;
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

    public ArrayList<TimesheetTaskTO> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TimesheetTaskTO> tasks) {
        this.tasks = tasks;
    }
}
