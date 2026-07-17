package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 22.05.2009
 * Time: 13:40:30
 * To change this template use File | Settings | File Templates.
 */
public class ProjectsReportResult implements IsSerializable {


    public ProjectsReportResult(String name, Double eac, Double planedCoast, Double costToDate, Double planedRevenue) {
        this.projectName = name;
        this.eac = eac != null ? String.valueOf(eac.doubleValue()) : "0.0";
        this.planedCost = planedCoast != null ? String.valueOf(planedCoast.doubleValue()) : "0.0";
        this.costToDate = costToDate != null ? String.valueOf(costToDate.doubleValue()) : "0.0";
        this.planedRevenue = planedRevenue != null ? String.valueOf(planedRevenue.doubleValue()) : "0.0";
    }

    public ProjectsReportResult(Integer id, String projectName, String manager, Integer members, String taskRemaining,
                                String issueRemaining, Integer completed, Date startDate, Date endDate) {
        this.id = id;
        this.projectName = projectName;
        this.projectManager = manager;
        this.members = members;
        this.tasksRemaining = tasksRemaining;
        this.issueRemaining = issueRemaining;
        this.complete = completed;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public ProjectsReportResult() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getCostToDate() {
        return costToDate;
    }

    public void setCostToDate(String costToDate) {
        this.costToDate = costToDate;
    }

    public String getTasksRemaining() {
        return tasksRemaining;
    }

    public void setTasksRemaining(String tasksRemaining) {
        this.tasksRemaining = tasksRemaining;
    }

    public String getIssueRemaining() {
        return issueRemaining;
    }

    public void setIssueRemaining(String issueRemaining) {
        this.issueRemaining = issueRemaining;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public String getPlanedCost() {
        return planedCost;
    }

    public void setPlanedCost(String planedCost) {
        this.planedCost = planedCost;
    }

    public String getEac() {
        return eac;
    }

    public void setEac(String eac) {
        this.eac = eac;
    }

    public String getPlanedRevenue() {
        return planedRevenue;
    }

    public void setPlanedRevenue(String planedRevenue) {
        this.planedRevenue = planedRevenue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String projectName;
    private String projectManager;
    private String planedCost;
    private String costToDate;
    private String eac;
    private String planedRevenue;
    private String tasksRemaining;
    private String issueRemaining;
    private Integer complete = 0;
    private Float percent;
    private Date startDate;
    private Date dueDate;
    private Integer members;
    private Integer id;

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }
}
