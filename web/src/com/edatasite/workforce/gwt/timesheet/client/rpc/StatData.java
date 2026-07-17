package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 10/4/11
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatData implements IsSerializable{
    private String project;
    private String client;
    private Integer weekly;
    private Integer monthly;
    private String projectFullName;
    private String clientFullName;

    public StatData() {
    }

    public StatData(String project, String client, Integer weekly, Integer monthly) {
        this.project = project;
        this.client = client;
        this.weekly = weekly;
        this.monthly = monthly;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getWeekly() {
        return weekly;
    }

    public void setWeekly(Integer weekly) {
        this.weekly = weekly;
    }

    public Integer getMonthly() {
        return monthly;
    }

    public void setMonthly(Integer monthly) {
        this.monthly = monthly;
    }

    public String getProjectFullName() {
        return projectFullName;
    }

    public void setProjectFullName(String projectFullName) {
        this.projectFullName = projectFullName;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }
}
