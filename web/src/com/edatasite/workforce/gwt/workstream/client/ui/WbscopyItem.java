package com.edatasite.workforce.gwt.workstream.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by Djuraev on 7/24/15.
 */
public class WbscopyItem implements IsSerializable {

    private Integer objectID;
    private Integer projectID;
    private Date startDate;
    private boolean copyTask;
    private boolean copyAssignee;
    private boolean resetStatus;
    private Integer taskStatusID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isCopyTask() {
        return copyTask;
    }

    public void setCopyTask(boolean copyTask) {
        this.copyTask = copyTask;
    }

    public boolean isCopyAssignee() {
        return copyAssignee;
    }

    public void setCopyAssignee(boolean copyAssignee) {
        this.copyAssignee = copyAssignee;
    }

    public boolean isResetStatus() {
        return resetStatus;
    }

    public void setResetStatus(boolean resetStatus) {
        this.resetStatus = resetStatus;
    }

    public Integer getTaskStatusID() {
        return taskStatusID;
    }

    public void setTaskStatusID(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }
}
