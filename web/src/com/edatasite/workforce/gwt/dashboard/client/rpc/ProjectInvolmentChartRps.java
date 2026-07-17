package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 14.10.2009
 * Time: 22:49:56
 * To change this template use File | Settings | File Templates.
 */
public class ProjectInvolmentChartRps implements IsSerializable {

    private String name;
    private ArrayList<String> projectName;
    private ArrayList<Number> timeSheet;
    private ArrayList<Number> timeSlot;
    private ArrayList<Number> estimatedTime;
    private boolean notZero;

    public ProjectInvolmentChartRps() {
        super();
    }

    public ProjectInvolmentChartRps(String name) {
        this.name = name;
    }

    public static ProjectInvolmentChartRps createEmpty() {
        ProjectInvolmentChartRps rps = new ProjectInvolmentChartRps();
        rps.setEstimatedTime(new ArrayList<>());
        rps.setNotZero(false);
        rps.setProjectName(new ArrayList<>());
        rps.setTimeSheet(new ArrayList<>());
        rps.setTimeSlot(new ArrayList<>());
        rps.setName("");
        return rps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Number> getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(ArrayList<Number> timeSheet) {
        this.timeSheet = timeSheet;
    }

    public ArrayList<Number> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(ArrayList<Number> timeSlot) {
        this.timeSlot = timeSlot;
    }

    public ArrayList<Number> getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(ArrayList<Number> estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public ArrayList<String> getProjectName() {
        return projectName;
    }

    public void setProjectName(ArrayList<String> projectName) {
        this.projectName = projectName;
    }

    public boolean isNotZero() {
        return notZero;
    }

    public void setNotZero(boolean notZero) {
        this.notZero = notZero;
    }
}
