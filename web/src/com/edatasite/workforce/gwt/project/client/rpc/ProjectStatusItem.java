package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 22, 2011
 * Time: 11:46:09 AM
 * To change this template use File | Settings | File Templates.
 */

public class ProjectStatusItem implements IsSerializable {
   public static String ACTION="action";
   public static String PROJECTNAME="projectName";
   public static String STATUSDATE="statusDate";
   public static String CURRENTSTATUS="currentStatus";
   public static String NEXTSTEP="nextStep";
   public static String STATUSACTION="statusAction";
   public static String CAMPAIGN="campaign";
   public static String DUEDATE="dueDate";

    private Integer objectID;
    private Integer projectID;
    private String projectName;
    private Date statusDate;
    private String currentStatus;
    private String nextSteps;
    private String action;
    private String dueDate;
    private String campaign;
    private Integer locationID;
    private String clientName;
    private ProjectItem[] projects;
    private SelectItem[] locations;
    private SelectItem[] clients;

    public ProjectStatusItem() {

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

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ProjectItem[] getProjects() {
        return projects;
    }

    public void setProjects(ProjectItem[] projects) {
        this.projects = projects;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }
}
