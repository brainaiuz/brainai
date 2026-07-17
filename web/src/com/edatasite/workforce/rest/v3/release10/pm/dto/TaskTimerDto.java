package com.edatasite.workforce.rest.v3.release10.pm.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TaskTimerDto {
    private Integer objectId;
    private Integer busObjectId;
    @NotNull
    private Integer taskId;
    private String taskName;
    private Integer projectId;
    private String projectName;
    private Date startDate;
    private Date endDate;
    private String comment;
    private Boolean reset = Boolean.FALSE;
    private Boolean started = Boolean.FALSE;
    private Boolean haveAStoppedTime = Boolean.FALSE;
    private Integer todaysTime = 0;

    public Integer getTodaysTime() {
        return todaysTime;
    }

    public void setTodaysTime(Integer todaysTime) {
        this.todaysTime = todaysTime;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getBusObjectId() {
        return busObjectId;
    }

    public void setBusObjectId(Integer busObjectId) {
        this.busObjectId = busObjectId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isReset() {
        return reset;
    }

    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    public Boolean isStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Boolean getHaveAStoppedTime() {
        return haveAStoppedTime;
    }

    public void setHaveAStoppedTime(Boolean haveAStoppedTime) {
        this.haveAStoppedTime = haveAStoppedTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getReset() {
        return reset;
    }

    public Boolean getStarted() {
        return started;
    }
}
