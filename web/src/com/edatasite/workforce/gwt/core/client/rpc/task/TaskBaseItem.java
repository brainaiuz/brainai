package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Jamshid Asatillayev
 * Date: 8/22/11
 * Time: 3:47 PM
 */
public class TaskBaseItem extends Relational implements IsSerializable {

    protected Integer objectID;
    protected String name;
    protected String description;
    protected String statusName;
    protected Integer statusID;
    protected String priorityName;
    protected String priorityCode;
    protected Integer priorityID;
    protected Integer typeID;
    protected Integer projectID;
    protected String projectName;
    protected Date startDate;
    protected Date dueDate;
    protected boolean billable;
    protected SelectItem[] status;
    protected SelectItem[] priority;
    private ArrayList<SelectItem> backupManagers;
    private ArrayList<HistoryListItem> notes;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public SelectItem[] getStatus() {
        return status;
    }

    public void setStatus(SelectItem[] status) {
        this.status = status;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public boolean getBillable() {
        return billable;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public SelectItem[] getPriority() {
        return priority;
    }

    public void setPriority(SelectItem[] priority) {
        this.priority = priority;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_TASK;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public ArrayList<SelectItem> getBackupManagers() {
        return backupManagers;
    }

    public void setBackupManagers(ArrayList<SelectItem> backupManagers) {
        this.backupManagers = backupManagers;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }
}
