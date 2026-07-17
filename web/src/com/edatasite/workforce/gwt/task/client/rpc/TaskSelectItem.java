package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

public class TaskSelectItem extends SelectItem {

    private Integer projectId;
    private String taskNumber;
    private Date taskStartDate;
    private Date taskDueDate;
    private Boolean allDay = true;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Date taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }
}