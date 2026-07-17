package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

public class TaskTransfer extends SelectItem {
    private Integer emplTaskId;
    private Integer taskId;
    private Integer projectId;
    private String emplTaskName;
    private String projectName;
    private String clientName;
    private TaskStatus taskStatus;
    private Date startDate;
    private float percentCompleted;
    private Integer estimatedTime;
    private Date endDate;
    private Integer totalMinutes;
    private boolean isIssue = false;

    public boolean isIssue() {
        return isIssue;
    }

    public void setIsIssue(boolean issue) {
        isIssue = issue;
    }


    public TaskTransfer() {
        super();
        this.emplTaskId = getId();
        this.emplTaskName = getName();
        this.taskStatus = new TaskStatus();
    }

    public TaskTransfer(Integer id, String name) {
        super(id, name);
        this.emplTaskId = getId();
        this.emplTaskName = getName();
        this.taskStatus = new TaskStatus();
    }

    public Integer getEmplTaskId() {
        return emplTaskId;
    }

    public void setEmplTaskId(Integer taskId) {
        this.emplTaskId = taskId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEmplTaskName() {
        return emplTaskName;
    }

    public void setEmplTaskName(String taskName) {
        this.emplTaskName = taskName;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public float getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(float percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
