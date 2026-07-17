package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by Hurshid on 5/10/2018.
 */
public class DashboardPMItem implements IsSerializable {

    private Integer objectID;
    private String number;
    private String name;
    private String manager;
    private Date deadLine;
    private String status;
    private String priority;
    private String projectName;
    private String referenecColor;

    public DashboardPMItem() {
    }

    public DashboardPMItem(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReferenecColor() {
        return referenecColor;
    }

    public void setReferenecColor(String referenecColor) {
        this.referenecColor = referenecColor;
    }
}
