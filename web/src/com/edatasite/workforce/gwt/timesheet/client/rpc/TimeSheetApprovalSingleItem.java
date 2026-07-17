package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 04.05.2009
 * Time: 17:57:09
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetApprovalSingleItem implements IsSerializable {
    private Integer id;
    private String taskName;
    private String description;
    private String comment;
    private DateNonConvertable date;
    private String timeSpent;
    private String estimatedTime;
    private String approvedHours;
    private int timeSpentInt;
    private boolean isApproved;
    private boolean isRejected;
    private String managerComment;
    private String managerApproveComment;
    private String projectName;
    private String hourType;

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getTimeSpentInt() {
        return timeSpentInt;
    }

    public void setTimeSpentInt(int timeSpentInt) {
        this.timeSpentInt = timeSpentInt;
    }

    public String getApprovedHours() {
        return approvedHours != null ? approvedHours : "";
    }

    public void setApprovedHours(String approvedHours) {
        this.approvedHours = approvedHours;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
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

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getHourType() {
        return hourType;
    }

    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    public String getManagerApproveComment() {
        return managerApproveComment;
    }

    public void setManagerApproveComment(String managerApproveComment) {
        this.managerApproveComment = managerApproveComment;
    }
}
