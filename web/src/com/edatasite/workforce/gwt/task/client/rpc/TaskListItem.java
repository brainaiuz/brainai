package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Markedable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 27, 2007 Time: 1:20:43 PM To
 * change this template use File | Settings | File Templates.
 */

public class TaskListItem extends Relational implements IsSerializable, Markedable, ListingCustomFields {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String DESCRIPTION = "description";
    public static final String STATUS_NAME = "statusName";
    public static final String OVERALL_STATUS_NAME = "overallStatusName";
    public static final String PRIORITY_NAME = "priorityName";
    public static final String TYPE_NAME = "typeName";
    public static final String PROJECT_NAME = "projectName";
    public static final String PROJECT_CUSTOMER_NAME = "projectCustomerName";
    public static final String PROJECT_NUMBER = "projectNumber";
    public static final String PROJECT_MANAGER_NAME = "projectManagerName";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String CREATION_DATE = "creationDate";
    public static final String CREATED_BY = "createdBy";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String BOTH_DATE = "bothDate";
    public static final String ESTIMATED = "estimated";
    public static final String ASSIGNED_TO = "assignedTo";
    public static final String COMPLETE = "complete";
    public static final String ACTUAL_HOURS_SPENT = "actualHoursSpent";
    public static final String HOUR_SPENT = "hoursSpent";
    public static final String DUE_DATE = "dueDate";
    public static final String DURATION = "duration";
    public static final String CLIENT = "client";
    public static final String TASK_RELATED_CLIENT = "TASK_RELATED_CLIENT";
    public static final String STRING_VALUE = "string_value";
    public static final String DATE_VALUE = "dateValue";
    public static final String NUMBER_VALUE = "doubleValue";
    public static final String PARENT_WORKSTREAM_NAME = "parentWorkstreamName";
    public static final String BILLABLE = "billable";
    public static final String ACTUAL_TIME = "actualTime";
    public static final String ACTUAL_START_DATE = "actualStartDate";
    public static final String ACTUAL_END_DATE = "actualEndDate";
    public static final String WAITING_HOURS = "waitingHours";
    public static final String REJECTED_HOURS = "rejectedHours";
    public static final String TASK_AMOUNT = "taskAmount";

    private Integer objectID;
    private String name;
    private String number;
    private String projectNumber;
    private String description;
    private String statusName;
    private String statusCode;
    private String overallStatusName;
    private Integer overallStatusId;
    private String priorityName;
    private String priorityCode;
    private Integer projectId;
    private String projectName;
    private String projectCustomerName;
    private String projectStatusCode;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModified;
    private Date creationDate;
    private Date startDate;
    private Date dueDate;
    private Date actualStartDate;
    private Date endDate;
    private String assignedTo;
    private String complete;
    private String actualHoursSpent;
    private String hoursSpent;
    private Boolean newTask;
    private String client;
    private String highlite;
    private Integer taskStatusId;
    private Integer priorityId;
    private Integer typeId;
    private String typeCode;
    private String typeName;
    private Integer estimated;
    private PermissionListItem permissions;
    private Boolean billable;
    private String googleID;
    private String parentWorkstreamName;
    private Integer parentWorkstreamId;
    private Boolean allDay;

    private Integer projectManagerID;
    private String projectManagerName;
    private Integer projectBackupManagerID;
    private ArrayList<Integer> projectBackupManagerIDs;
    private Integer taskCreatorID;
    private HashMap<String, Object> customFields;
    private boolean timerIsStarted = false;
    private boolean showTimer = false;
    private boolean showLogTime = false;
    private boolean isPMorBackupPM = false;
    private boolean isAtleastOneTimerStarted = false;

    private boolean isWorkflowItem;
    private Integer workflowID;

    private String workflowStartDate;
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;

    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    private String waitingHours;
    private String rejectedHours;
    private BigDecimal taskAmount;

    private Long kanbanOrder;
    private String note;

    private String assigneeFullNames;
    private String priorityColor;
    private SelectItem status;

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public boolean isWorkflowItem() {
        return isWorkflowItem;
    }

    public void setWorkflowItem(boolean isWorkflowItem) {
        this.isWorkflowItem = isWorkflowItem;
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

    public String getParentWorkstreamName() {
        return parentWorkstreamName;
    }

    public void setParentWorkstreamName(String parentWorkstreamName) {
        this.parentWorkstreamName = parentWorkstreamName;
    }

    public Integer getParentWorkstreamId() {
        return parentWorkstreamId;
    }

    public void setParentWorkstreamId(Integer parentWorkstreamId) {
        this.parentWorkstreamId = parentWorkstreamId;
    }

    public PermissionListItem getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionListItem permissions) {
        this.permissions = permissions;
    }

    public String getHighlite() {
        return highlite;
    }

    public void setHighlite(String highlite) {
        this.highlite = highlite;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {return projectName;}

    public void setProjectName(String projectName) {this.projectName = projectName;}

    public void setProjectCustomerName(String projectCustomerName) {this.projectCustomerName = projectCustomerName;}

    public String getProjectCustomerName() {return projectCustomerName;}

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {return startDate;}

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getActualHoursSpent() {
        return actualHoursSpent;
    }

    public void setActualHoursSpent(String actualHoursSpent) {
        this.actualHoursSpent = actualHoursSpent;
    }

    public String getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(String hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public Boolean isMarked() {
        return newTask;
    }

    public void setMarked(Boolean marked) {
        newTask = marked;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(Integer taskStatusId) {
        this.taskStatusId = taskStatusId;
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

    public Integer getEstimated() {
        return estimated == null ? 0 : estimated;
    }

    public void setEstimated(Integer estimated) {
        this.estimated = estimated;
    }

    public Boolean isBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public Integer getProjectManagerID() {
        return projectManagerID;
    }

    public void setProjectManagerID(Integer projectManagerID) {
        this.projectManagerID = projectManagerID;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public Integer getProjectBackupManagerID() {
        return projectBackupManagerID;
    }

    public void setProjectBackupManagerID(Integer projectBackupManagerID) {
        this.projectBackupManagerID = projectBackupManagerID;
    }

    public ArrayList<Integer> getProjectBackupManagerIDs() {
        return projectBackupManagerIDs;
    }

    public void setProjectBackupManagerIDs(ArrayList<Integer> projectBackupManagerIDs) {
        this.projectBackupManagerIDs = projectBackupManagerIDs;
    }

    public Integer getTaskCreatorID() {
        return taskCreatorID;
    }

    public void setTaskCreatorID(Integer taskCreatorID) {
        this.taskCreatorID = taskCreatorID;
    }

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public HashMap<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    @Override
    public void setCustomFieldsValue(String key, Object value) {
        customFields.put(key, value);
    }

    @Override
    public Object getCustomFieldsValue(String columnKey) {
        return customFields.get(columnKey);
    }

    public boolean timerIsStarted() {
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

    public boolean isShowLogTime() {
        return showLogTime;
    }

    public void setShowLogTime(boolean showLogTime) {
        this.showLogTime = showLogTime;
    }

    public String getOverallStatusName() {
        return overallStatusName;
    }

    public void setOverallStatusName(String overallStatusName) {
        this.overallStatusName = overallStatusName;
    }

    public Integer getOverallStatusId() {
        return overallStatusId;
    }

    public void setOverallStatusId(Integer overallStatusId) {
        this.overallStatusId = overallStatusId;
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

    public boolean isPMorBackupPM() {
        return isPMorBackupPM;
    }

    public void setPMorBackupPM(boolean PMorBackupPM) {
        isPMorBackupPM = PMorBackupPM;
    }

    public String getWaitingHours() {
        return waitingHours;
    }

    public void setWaitingHours(String waitingHours) {
        this.waitingHours = waitingHours;
    }

    public String getRejectedHours() {
        return rejectedHours;
    }

    public void setRejectedHours(String rejectedHours) {
        this.rejectedHours = rejectedHours;
    }

    public String getProjectStatusCode() {
        return projectStatusCode;
    }

    public void setProjectStatusCode(String projectStatusCode) {
        this.projectStatusCode = projectStatusCode;
    }

    public BigDecimal getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(BigDecimal taskAmount) {
        this.taskAmount = taskAmount;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAssigneeFullNames() {
        return assigneeFullNames;
    }

    public void setAssigneeFullNames(String assigneeFullNames) {
        this.assigneeFullNames = assigneeFullNames;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public String calculateDueDays() {
        if (this.dueDate == null) return "N/A";
         else if ("COMPLETED".equalsIgnoreCase(this.statusCode)) return  "0";

        long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

        // Core Logic: Due Date - Now
        long diffMillies = this.dueDate.getTime() - System.currentTimeMillis();
        // Math.round handles the "Today = 0" requirement
        long diffDays = Math.round((double) diffMillies / MILLIS_PER_DAY);
        return String.valueOf(diffDays);
    }


}
