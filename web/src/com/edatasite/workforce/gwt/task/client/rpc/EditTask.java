package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Anvarbek Date: Feb 14, 2008
 */

public class EditTask extends Relational implements IsSerializable, UserGrant {
    public static final String FROM_RESOURCE_UTIL = "FROM_RESOURCE_UTIL";

    private static final Integer[] EMPTY = new Integer[0];
    private String createdFrom;
    private Integer objectID;
    private String quickbookTaskID;
    private String quickbookEditSequence;
    private NumberData numberData;
    private String number;
    private String name;
    private String description;
    private Integer priorityId;
    private Integer typeId;
    private String typeCode;
    private String typeName;
    private Float percent;
    private Date startDate;
    private Date endDate;
    private Date dueDate;
    private Integer estimatedTime;
    private Integer statusId;
    private Integer[] assignees = EMPTY; // array of objectId's of EdsProjectEmployee
    private boolean mySelf = false;
    private SelectItem parentWSItem;
    //private TaskSelectItem[] subTaskItems;
    private TaskSelectItem[] predecessorTaskItems;
    private TaskSelectItem[] successorTaskItems;
    private Integer projectId;
    private String projectName;
    private boolean projectBillable;
    private int permission;
    private boolean isEmployeeTask;
    private Integer employeeTaskID;
    private Integer employeeID;
    private boolean recalculateResourceHours;
    private Boolean billable = true;
    private Integer folderId;
    private FolderResource folder;
    private Integer reminderTime;
    private Boolean allDay;
    private ArrayList<String> permissions;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
    private Boolean updateTaskStatusForAll = false;
    private Boolean updateAssignmentTaskStatus = false;
    private SelectItem[] projects;
    private Integer actualTime;
    private boolean dontKeepDelays = true;
    private Integer workflowID;
    private String workflowStartDate;
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeMap;
    private IdTime[] assigneeItems;
    private boolean workflowTask = false;
    private String visibilityStatus;
    private Integer firstAssigneeId;
    private boolean nonAssignedIncluded;

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getWorkflowStartDate() {
        return workflowStartDate;
    }

    public void setWorkflowStartDate(String workflowStartDate) {
        this.workflowStartDate = workflowStartDate;
    }

    public Integer getWorkflowDueDate() {
        return workflowDueDate;
    }

    public void setWorkflowDueDate(Integer workflowDueDate) {
        this.workflowDueDate = workflowDueDate;
    }

    public String getWorkflowDueDateGranularity() {
        return workflowDueDateGranularity;
    }

    public void setWorkflowDueDateGranularity(String workflowDueDateGranularity) {
        this.workflowDueDateGranularity = workflowDueDateGranularity;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public boolean isRecalculateResourceHours() {
        return recalculateResourceHours;
    }

    public void setRecalculateResourceHours(boolean recalculateResourceHours) {
        this.recalculateResourceHours = recalculateResourceHours;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Integer getEmployeeTaskID() {
        return employeeTaskID;
    }

    public void setEmployeeTaskID(Integer employeeTaskID) {
        this.employeeTaskID = employeeTaskID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isEmployeeTask() {
        return isEmployeeTask;
    }

    public void setEmployeeTask(boolean employeeTask) {
        isEmployeeTask = employeeTask;
    }

    public boolean isMySelf() {
        return mySelf;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public void setMySelf(boolean mySelf) {
        this.mySelf = mySelf;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer[] getAssignees() {
        return assignees;
    }

    public void setAssignees(Integer[] assignees) {
        this.assignees = assignees;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public SelectItem getParentWSItem() {
        return parentWSItem;
    }

    public void setParentWSItem(SelectItem parentWSItem) {
        this.parentWSItem = parentWSItem;
    }

    /*public TaskSelectItem[] getSubTaskItems() {
         return subTaskItems;
     }

     public void setSubTaskItems(TaskSelectItem[] subTaskItems) {
         this.subTaskItems = subTaskItems;
     }*/

    public TaskSelectItem[] getPredecessorTaskItems() {
        return predecessorTaskItems;
    }

    public void setPredecessorTaskItems(TaskSelectItem[] predecessorTaskItems) {
        this.predecessorTaskItems = predecessorTaskItems;
    }

    public TaskSelectItem[] getSuccessorTaskItems() {
        return successorTaskItems;
    }

    public void setSuccessorTaskItems(TaskSelectItem[] successorTaskItems) {
        this.successorTaskItems = successorTaskItems;
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

    public boolean isProjectBillable() {
        return projectBillable;
    }

    public void setProjectBillable(boolean projectBillable) {
        this.projectBillable = projectBillable;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public FolderResource getFolder() {
        return folder;
    }

    public void setFolder(FolderResource folder) {
        this.folder = folder;
    }

    public ArrayList<CalendarEventReminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<CalendarEventReminder> reminders) {
        this.reminders = reminders;
    }

    public String getQuickbookTaskID() {
        return quickbookTaskID;
    }

    public void setQuickbookTaskID(String quickbookTaskID) {
        this.quickbookTaskID = quickbookTaskID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public Boolean isUpdateTaskStatusForAll() {
        return updateTaskStatusForAll;
    }

    public void setUpdateTaskStatusForAll(Boolean updateTaskStatusForAll) {
        this.updateTaskStatusForAll = updateTaskStatusForAll;
    }

    public Boolean isUpdateAssignmentTaskStatus() {
        return updateAssignmentTaskStatus;
    }

    public void setUpdateAssignmentTaskStatus(Boolean updateAssignmentTaskStatus) {
        this.updateAssignmentTaskStatus = updateAssignmentTaskStatus;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public boolean isDontKeepDelays() {
        return dontKeepDelays;
    }

    public void setDontKeepDelays(boolean dontKeepDelays) {
        this.dontKeepDelays = dontKeepDelays;
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

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneeMap() {
        return assigneeMap;
    }

    public void setAssigneeMap(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeMap) {
        this.assigneeMap = assigneeMap;
    }

    public IdTime[] getAssigneeItems() {
        return assigneeItems;
    }

    public void setAssigneeItems(IdTime[] assigneeItems) {
        this.assigneeItems = assigneeItems;
    }

    public boolean isWorkflowTask() {
        return workflowTask;
    }

    public void setWorkflowTask(boolean workflowTask) {
        this.workflowTask = workflowTask;
    }

    public String getVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(String visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    public Integer getFirstAssigneeId() {
        return firstAssigneeId;
    }

    public void setFirstAssigneeId(Integer firstAssigneeId) {
        this.firstAssigneeId = firstAssigneeId;
    }

    public boolean isNonAssignedIncluded() {
        return nonAssignedIncluded;
    }

    public void setNonAssignedIncluded(boolean nonAssignedIncluded) {
        this.nonAssignedIncluded = nonAssignedIncluded;
    }
}