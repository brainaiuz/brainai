package com.edatasite.workforce.gwt.issue.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Abdulaziz
 * Date: 14.05.2009
 * Time: 13:33:55
 */
public class IssueItem extends Relational implements IsSerializable, ListingCustomFields {

    private FileItem[] attachments;
    private Integer objectID;
    private Integer taskID;
    private Integer defaultProjectID;
    private String defaultProjectName;
    private String createdBy;
    private Integer createdID;
    private Date createdFrom;
    private String description;
    private Date endDate;
    private boolean isBillable;
    private Boolean isPublic;
    private NumberData numberData;
    private String name;
    private ArrayList<HistoryListItem> notes;
    private String lastUpdatedBy;
    private Date lastUpdatedDate;
    private Integer reportedByID;
    private String reportedByName;
    private Integer resolverID;
    private String resolverName;
    private Integer priorityID;
    private String priorityName;
    private String priorityCode;
    private Integer projectID;
    private String projectName;
    private Date startDate;
    private Integer statusID;
    private String statusName;
    private String statusCode;
    private boolean timeSheetEnabled;
    private PositionsSelectItem[] issueEmployees;
    private ArrayList<Integer> issueEmployeeIDs;
    private int permission;
    private HashMap<Integer, PositionsSelectItem> issueEmployeeItems;
    private SelectItem[] resolverItems;
    private SelectItem[] priorities;
    private SelectItem[] reportedByItems;
    private SelectItem[] statuses;

    private boolean timerIsStarted = false;
    private boolean showTimer = false;

    private IdTime[] assignees;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private ArrayList<CompanyCustomFieldItem> customFieldsForFiltering;

    private boolean isSupplier;

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getDefaultProjectID() {
        return defaultProjectID;
    }

    public void setDefaultProjectID(Integer defaultProjectID) {
        this.defaultProjectID = defaultProjectID;
    }

    public String getDefaultProjectName() {
        return defaultProjectName;
    }

    public void setDefaultProjectName(String defaultProjectName) {
        this.defaultProjectName = defaultProjectName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getCreatedID() {
        return createdID;
    }

    public void setCreatedID(Integer createdID) {
        this.createdID = createdID;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isBillable() {
        return isBillable;
    }

    public void setBillable(boolean billable) {
        isBillable = billable;
    }

    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getReportedByID() {
        return reportedByID;
    }

    public void setReportedByID(Integer reportedByID) {
        this.reportedByID = reportedByID;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Integer getResolverID() {
        return resolverID;
    }

    public void setResolverID(Integer resolverID) {
        this.resolverID = resolverID;
    }

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
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

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isTimeSheetEnabled() {
        return timeSheetEnabled;
    }

    public void setTimeSheetEnabled(boolean timeSheetEnabled) {
        this.timeSheetEnabled = timeSheetEnabled;
    }

    public PositionsSelectItem[] getIssueEmployees() {
        return issueEmployees;
    }

    public void setIssueEmployees(PositionsSelectItem[] issueEmployees) {
        this.issueEmployees = issueEmployees;
    }

    public ArrayList<Integer> getIssueEmployeeIDs() {
        return issueEmployeeIDs;
    }

    public void setIssueEmployeeIDs(ArrayList<Integer> issueEmployeeIDs) {
        this.issueEmployeeIDs = issueEmployeeIDs;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public HashMap<Integer, PositionsSelectItem> getIssueEmployeeItems() {
        return issueEmployeeItems;
    }

    public void setIssueEmployeeItems(HashMap<Integer, PositionsSelectItem> issueEmployeeItems) {
        this.issueEmployeeItems = issueEmployeeItems;
    }

    public SelectItem[] getResolverItems() {
        return resolverItems;
    }

    public void setResolverItems(SelectItem[] resolverItems) {
        this.resolverItems = resolverItems;
    }

    public SelectItem[] getPriorities() {
        return priorities;
    }

    public void setPriorities(SelectItem[] priorities) {
        this.priorities = priorities;
    }

    public SelectItem[] getReportedByItems() {
        return reportedByItems;
    }

    public void setReportedByItems(SelectItem[] reportedByItems) {
        this.reportedByItems = reportedByItems;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public boolean isTimerIsStarted() {
        return timerIsStarted;
    }

    public void setTimerIsStarted(boolean timerIsStarted) {
        this.timerIsStarted = timerIsStarted;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public IdTime[] getAssignees() {
        return assignees;
    }

    public void setAssignees(IdTime[] assignees) {
        this.assignees = assignees;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_ISSUE;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsForFiltering() {
        return customFieldsForFiltering;
    }

    public void setCustomFieldsForFiltering(ArrayList<CompanyCustomFieldItem> customFieldsForFiltering) {
        this.customFieldsForFiltering = customFieldsForFiltering;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }
}