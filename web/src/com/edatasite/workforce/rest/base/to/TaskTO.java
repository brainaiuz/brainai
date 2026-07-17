package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umidbek on 16.02.2015.
 */
public class TaskTO implements IsSerializable {
    private Integer id;

    private String name;
    private String number;
    private String description;

    private Boolean billable;
    private Boolean fullTimeEmployment;

    private SelectItemTO status;
    private SelectItemTO priority;

    private UserTO client;

    private Long creationDate;
    private Long modifiedDate;
    private UserTO createdBy;
    private UserTO modifiedBy;

    private Long startDate;
    private Long actualStartDate;
    private Long actualEndDate;
    private Long dueDate;
    private Long endDate;

    private Integer estimatedTime;
    private Integer hoursSpent;
    private Integer actualHoursSpent;
    private Integer dailyTotal;
    private Integer waitingHours;
    private Integer rejectedHours;

    private Double percentComplete;
    private Double myPercentComplete;

    private String estimatedCost;
    private String actualCost;

    private ProjectTO project;
    private WorkStreamTO workStream;
    private ArrayList<TaskAssigneeTO> assignees;

    private Boolean isManager;
    private Boolean addTimeShown;
    private Boolean timerShown;
    private Boolean timerStarted;
    private List<Object> customFields;

    public TaskTO() {
    }

    public TaskTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskTO(TaskListItem item) {
        this.id = item.getObjectID();
        this.number = item.getNumber();
        this.name = item.getName();
        this.status = new SelectItemTO(item.getOverallStatusId(), item.getOverallStatusName());
        this.priority = new SelectItemTO(item.getPriorityId(), item.getPriorityName());

//        this.description = item.getDescription();
//        this.billable = item.isBillable();
//        this.fullTimeEmployment = item.isAllDay();
//
//        this.estimatedTime = item.getEstimated();
//
//        this.percentComplete = WrapUtils.getDouble(item.getComplete());
//        this.hoursSpent = WrapUtils.timeToMinutes(item.getHoursSpent());
//        this.actualHoursSpent = WrapUtils.timeToMinutes(item.getActualHoursSpent());
//        this.waitingHours = WrapUtils.timeToMinutes(item.getWaitingHours());
//        this.rejectedHours = WrapUtils.timeToMinutes(item.getRejectedHours());
//
//        this.createdBy = new UserTO(item.getTaskCreatorID(), null);
//        this.modifiedBy = new UserTO(null, item.getLastModifiedBy());
//
//
//        this.modifiedDate = WrapUtils.dateToLong(item.getLastModified());
//        this.creationDate = WrapUtils.dateToLong(item.getCreationDate());
//
//        this.startDate = WrapUtils.dateToLong(item.getStartDate());
//        this.actualStartDate = WrapUtils.dateToLong(item.getActualStartDate());
//        this.endDate = WrapUtils.dateToLong(item.getEndDate());
//        this.dueDate = WrapUtils.dateToLong(item.getDueDate());
//
//
//        if (item.getAssignedTo() != null) {
//            String[] assignees = item.getAssignedTo().split("[,]");
//
//            for (String assignee : assignees) {
//                if (!StringUtil.isEmpty(assignee)) {
//                    this.assignees.add(new UserTO(null, assignee.trim()));
//                }
//            }
//        }
//
//        this.project = new ProjectTO(item.getProjectId(), item.getProjectName(), item.getProjectNumber());
//        this.project.setManager(new UserTO(item.getProjectManagerID(), item.getProjectManagerName()));
//
//        if (item.getProjectBackupManagerID() != null) {
//            this.project.getBackupManagers().add(new UserTO(item.getProjectBackupManagerID(), null));
//        }
//
//        if (item.getParentWorkstreamId() != null || item.getParentWorkstreamName() != null) {
//            this.workStream = new WorkStreamTO(item.getParentWorkstreamId(), item.getParentWorkstreamName());
//        }
//
//        if (item.getClient() != null) {
//            this.client = new UserTO(null, item.getClient());
//        }
//
//        this.isCurrentApprover = item.isPMorBackupPM();
//        this.timerShown = item.isShowTimer();
//        this.timerStarted = item.timerIsStarted();
//        this.addTimeShown = item.isShowLogTime();
    }

    public TaskTO(TaskSingleItem item) {
        this.id = item.getObjectID();
        this.name = item.getName();
        this.description = item.getDescription();

        if (item.getNumberData() != null) {
            this.number = item.getNumberData().getNumberString();
        }

        this.billable = item.getBillable();
        this.fullTimeEmployment = item.isAllDay();

        this.status = new SelectItemTO(item.getStatusID(), item.getStatusName());
        this.priority = new SelectItemTO(item.getPriorityID(), item.getPriorityName());

        if (item.getClientName() != null && !item.getClientName().equals("N/A")) {
            this.client = new UserTO(null, item.getClientName());
        }

        this.creationDate = WrapUtils.dateToLong(item.getTaskCreationTime());
        this.modifiedDate = WrapUtils.dateToLong(item.getLastModified());
        this.createdBy = new UserTO(item.getTaskCreatorID(), item.getTaskCreator());

        if (item.getLastModifiedBy() != null) {
            this.modifiedBy = new UserTO(null, item.getLastModifiedBy());
        }

        this.startDate = WrapUtils.dateToLong(item.getStartDate());
        this.actualStartDate = WrapUtils.dateToLong(item.getActualStartDate());
        this.actualEndDate = WrapUtils.dateToLong(item.getActualEndDate());
        this.dueDate = WrapUtils.dateToLong(item.getDueDate());
        this.endDate = WrapUtils.dateToLong(item.getEndDate());

        this.estimatedTime = item.getEstimatedTime();

        if (item.getPercent() != null) {
            this.percentComplete = Double.valueOf(item.getPercent());
        }

        if (item.getMyPercent() != null) {
            this.myPercentComplete = Double.valueOf(item.getMyPercent());
        }
        this.actualCost = item.getActualCost();
        this.estimatedCost = item.getEstimatedCost();

        this.hoursSpent = item.getTimeSpent();
        this.actualHoursSpent = item.getActualTime();

        this.waitingHours = WrapUtils.timeToMinutes(item.getWaitingHours());
        this.rejectedHours = WrapUtils.timeToMinutes(item.getRejectedHours());

        this.project = new ProjectTO(item.getProjectID(), item.getProjectName(), null);
        this.project.setManager(new UserTO(item.getProjectManagerID(), item.getProjectManager()));

        if (item.getProjectBackupManagerID() != null) {
            this.project.getBackupManagers().add(new UserTO(item.getProjectBackupManagerID(), null));
        }

        if (item.getWorkstreamName() != null) {
            this.workStream = new WorkStreamTO(item.getWorkstreamID(), item.getWorkstreamName());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Boolean getFullTimeEmployment() {
        return fullTimeEmployment;
    }

    public void setFullTimeEmployment(Boolean fullTimeEmployment) {
        this.fullTimeEmployment = fullTimeEmployment;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public SelectItemTO getPriority() {
        return priority;
    }

    public void setPriority(SelectItemTO priority) {
        this.priority = priority;
    }

    public UserTO getClient() {
        return client;
    }

    public void setClient(UserTO client) {
        this.client = client;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Long actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Long getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Long actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(Integer hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public Integer getActualHoursSpent() {
        return actualHoursSpent;
    }

    public void setActualHoursSpent(Integer actualHoursSpent) {
        this.actualHoursSpent = actualHoursSpent;
    }

    public Integer getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(Integer dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public Integer getWaitingHours() {
        return waitingHours;
    }

    public void setWaitingHours(Integer waitingHours) {
        this.waitingHours = waitingHours;
    }

    public Integer getRejectedHours() {
        return rejectedHours;
    }

    public void setRejectedHours(Integer rejectedHours) {
        this.rejectedHours = rejectedHours;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public Double getMyPercentComplete() {
        return myPercentComplete;
    }

    public void setMyPercentComplete(Double myPercentComplete) {
        this.myPercentComplete = myPercentComplete;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getActualCost() {
        return actualCost;
    }

    public void setActualCost(String actualCost) {
        this.actualCost = actualCost;
    }

    public ProjectTO getProject() {
        return project;
    }

    public void setProject(ProjectTO project) {
        this.project = project;
    }

    public WorkStreamTO getWorkStream() {
        return workStream;
    }

    public void setWorkStream(WorkStreamTO workStream) {
        this.workStream = workStream;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public Boolean getAddTimeShown() {
        return addTimeShown;
    }

    public void setAddTimeShown(Boolean addTimeShown) {
        this.addTimeShown = addTimeShown;
    }

    public Boolean getTimerShown() {
        return timerShown;
    }

    public void setTimerShown(Boolean timerShown) {
        this.timerShown = timerShown;
    }

    public Boolean getTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(Boolean timerStarted) {
        this.timerStarted = timerStarted;
    }

    public ArrayList<TaskAssigneeTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<TaskAssigneeTO> assignees) {
        this.assignees = assignees;
    }

    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    public List<Object> getCustomFields() {
        return customFields;
    }
}
