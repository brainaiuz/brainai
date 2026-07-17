package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 11.01.2008
 * Time: 14:34:54
 * To change this template use File | Settings | File Templates.
 */
public class ContractViewItem implements IsSerializable {

    private String client;
    private Integer clientId;
    private int membersInvolved;
    private Integer objectID;
    private String creator;
    private Integer creatorID;
    private String lastUpdaterName;
    private String number;
    private ProjectPosition[] projectPositions;
    private String project;
    private Integer projectId;
    private String projectStatusCode;
    private Integer projectParentId;
    private Boolean accomodation;
    private Boolean food;
    private DateNonConvertable startDate;
    private DateNonConvertable dueDate;
    private DateNonConvertable creationTime;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<SelectItem> relatedCases;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public String getLastUpdaterName() {
        return lastUpdaterName;
    }

    public void setLastUpdaterName(String lastUpdaterName) {
        this.lastUpdaterName = lastUpdaterName;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        objectID = objectID;
    }


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ProjectPosition[] getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(ProjectPosition[] projectPositions) {
        this.projectPositions = projectPositions;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getProjectStatusCode() {
        return projectStatusCode;
    }

    public void setProjectStatusCode(String projectStatusCode) {
        this.projectStatusCode = projectStatusCode;
    }

    public Integer getProjectParentId() {
        return projectParentId;
    }

    public void setProjectParentId(Integer projectParentId) {
        this.projectParentId = projectParentId;
    }

    public void setAccomodation(Boolean accomodation) {
        this.accomodation = accomodation;
    }

    public Boolean getAccomodation() {
        return accomodation;
    }

    public void setFood(Boolean food) {
        this.food = food;
    }

    public Boolean getFood() {
        return food;
    }

    public DateNonConvertable getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateNonConvertable creationTime) {
        this.creationTime = creationTime;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public ArrayList<SelectItem> getRelatedCases() {
        if (relatedCases == null) {
            relatedCases = new ArrayList<>();
        }
        return relatedCases;
    }

    public void setRelatedCases(ArrayList<SelectItem> relatedCases) {
        this.relatedCases = relatedCases;
    }
}
