package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.project.client.rpc.ProjectStatusItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProjectStatusItem {

    private Integer objectID;
    private Integer projectID;
    private String projectName;
    private Date statusDate;
    private String currentStatus;
    private String nextSteps;
    private String action;
    private String dueDate;

    public MProjectStatusItem() {
    }

    public MProjectStatusItem(ProjectStatusItem projectStatusItem) {
        if (projectStatusItem != null) {
            this.objectID = projectStatusItem.getObjectID();
            this.projectID = projectStatusItem.getProjectID();
            this.projectName = projectStatusItem.getProjectName();
            this.statusDate = projectStatusItem.getStatusDate();
            this.currentStatus = projectStatusItem.getCurrentStatus();
            this.nextSteps = projectStatusItem.getNextSteps();
            this.action = projectStatusItem.getAction();
            this.dueDate = projectStatusItem.getDueDate();
        }
    }

    public boolean convert(ProjectStatusItem projectStatusItem, MProjectStatusItem mProjectStatusItem, boolean fromProjectStatusItem) {
        if (projectStatusItem == null || mProjectStatusItem == null)
            return false;

        try {
            if (fromProjectStatusItem) {
                mProjectStatusItem.setObjectID(projectStatusItem.getObjectID());
                mProjectStatusItem.setProjectID(projectStatusItem.getProjectID());
                mProjectStatusItem.setProjectName(projectStatusItem.getProjectName());
                mProjectStatusItem.setStatusDate(projectStatusItem.getStatusDate());
                mProjectStatusItem.setCurrentStatus(projectStatusItem.getCurrentStatus());
                mProjectStatusItem.setNextSteps(projectStatusItem.getNextSteps());
                mProjectStatusItem.setAction(projectStatusItem.getAction());
                mProjectStatusItem.setDueDate(projectStatusItem.getDueDate());
            } else {
                projectStatusItem.setObjectID(mProjectStatusItem.getObjectID());
                projectStatusItem.setProjectID(mProjectStatusItem.getProjectID());
                projectStatusItem.setProjectName(mProjectStatusItem.getProjectName());
                projectStatusItem.setStatusDate(mProjectStatusItem.getStatusDate());
                projectStatusItem.setCurrentStatus(mProjectStatusItem.getCurrentStatus());
                projectStatusItem.setNextSteps(mProjectStatusItem.getNextSteps());
                projectStatusItem.setAction(mProjectStatusItem.getAction());
                projectStatusItem.setDueDate(mProjectStatusItem.getDueDate());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(String nextSteps) {
        this.nextSteps = nextSteps;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
