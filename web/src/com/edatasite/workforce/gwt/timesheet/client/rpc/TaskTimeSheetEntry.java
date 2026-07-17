package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 01.05.2009
 * Time: 19:55:22
 * To change this template use File | Settings | File Templates.
 */
public class TaskTimeSheetEntry implements IsSerializable {
    private Integer projectId;
    private Integer taskId;
    private String taskName;
    private String managerComment;
    private String managerApproveComment;
    private boolean isApproved = false;
    private boolean isRejected = false;
    private TimeSheetEntry[] entries;
    private boolean isBillable = false;
    private int totalTimeSpent;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public TimeSheetEntry[] getEntries() {
        return entries;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public void setEntries(TimeSheetEntry[] entries) {
        this.entries = entries;
    }

    public boolean isBillable() {
        return isBillable;
    }

    public void setBillable(boolean billable) {
        isBillable = billable;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public String getManagerApproveComment() {
        return managerApproveComment;
    }

    public void setManagerApproveComment(String managerApproveComment) {
        this.managerApproveComment = managerApproveComment;
    }
}
