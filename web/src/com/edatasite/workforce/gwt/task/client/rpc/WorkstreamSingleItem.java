package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 12.11.2008
 * Time: 20:35:50
 * To change this template use File | Settings | File Templates.
 */
public class WorkstreamSingleItem implements IsSerializable {

    private Integer objectID;
    private String name;
    private String description;
    private Date startDate;
    private Integer estimatedTime;
    private Date endDate;
    private Integer creatorID;
    private String creatorName;
    private Date creationTime;
    private Date lastUpdateTime;
    private String lastUpdaterName;
    private TaskListItem[] tasks;
    private WorkstreamSingleItem parentWS;
    private Integer parentWSID;
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private String parentWSName;
    private Integer projectID;
    private String projectName;
    private WorkstreamSingleItem[] subWorkstreams;
    private WorkstreamAssigneeItem[] assignees;
    private WorkstreamAssigneeItem[] positions;
    private Double wageAmount;
    private Double clientChargeAmmount;

    private Float percent;
    private String projectManager;
    private Integer projectManagerID;
    private Double estimatedCost;
    private Integer actualTime;
    private Double actualCost;

    private Integer notStartedTasksCount;
    private Integer inProgressTasksCount;
    private Integer completedTasksCount;
    private Integer cancelledTasksCount;
    private Integer waitingForTasksCount;
    private Integer closedTasksCount;
    private NumberData numberData;
    private String number;
    private ArrayList<SelectItem> backupManagers;
	private Integer taskGanttOrder;

    public WorkstreamSingleItem() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdaterName() {
        return lastUpdaterName;
    }

    public void setLastUpdaterName(String lastUpdaterName) {
        this.lastUpdaterName = lastUpdaterName;
    }

    public TaskListItem[] getTasks() {
        return tasks;
    }

    public void setTasks(TaskListItem[] tasks) {
        this.tasks = tasks;
    }

    public WorkstreamSingleItem getParentWS() {
        return parentWS;
    }

    public void setParentWS(WorkstreamSingleItem parentWS) {
        this.parentWS = parentWS;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public WorkstreamSingleItem[] getSubWorkstreams() {
        return subWorkstreams;
    }

    public void setSubWorkstreams(WorkstreamSingleItem[] subWorkstreams) {
        this.subWorkstreams = subWorkstreams;
    }

    public Integer getParentWSID() {
        return parentWSID;
    }

    public void setParentWSID(Integer parentWSID) {
        this.parentWSID = parentWSID;
    }

    public ArrayList<CalendarEventReminder> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<CalendarEventReminder> reminder) {
        this.reminder = reminder;
    }

    public String getParentWSName() {
        return parentWSName;
    }

    public void setParentWSName(String parentWSName) {
        this.parentWSName = parentWSName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public WorkstreamAssigneeItem[] getAssignees() {
        return assignees;
    }

    public void setAssignees(WorkstreamAssigneeItem assignees[]) {
        this.assignees = assignees;
    }

    public Double getWageAmount() {
        return wageAmount;
    }

    public void setWageAmount(Double wageAmount) {
        this.wageAmount = wageAmount;
    }

    public Double getClientChargeAmmount() {
        return clientChargeAmmount;
    }

    public void setClientChargeAmmount(Double clientChargeAmmount) {
        this.clientChargeAmmount = clientChargeAmmount;
    }

    public WorkstreamAssigneeItem[] getPositions() {
        return positions;
    }

    public void setPositions(WorkstreamAssigneeItem[] positions) {
        this.positions = positions;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Integer getProjectManagerID() {
        return projectManagerID;
    }

    public void setProjectManagerID(Integer projectManagerID) {
        this.projectManagerID = projectManagerID;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public Integer getNotStartedTasksCount() {
        return notStartedTasksCount != null ? notStartedTasksCount : 0;
    }

    public void setNotStartedTasksCount(Integer notStartedTasksCount) {
        this.notStartedTasksCount = notStartedTasksCount;
    }

    public void updateNotStartedTasksCount(Integer notStartedTasksCount) {
        if (this.notStartedTasksCount == null) {
            this.notStartedTasksCount = 0;
        }

        this.notStartedTasksCount += notStartedTasksCount != null ? notStartedTasksCount : 0;
    }

    public Integer getInProgressTasksCount() {
        return inProgressTasksCount != null ? inProgressTasksCount : 0;
    }

    public void setInProgressTasksCount(Integer inProgressTasksCount) {
        this.inProgressTasksCount = inProgressTasksCount;
    }

    public Integer getCompletedTasksCount() {
        return completedTasksCount != null ? completedTasksCount : 0;
    }

    public void setCompletedTasksCount(Integer completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public Integer getCancelledTasksCount() {
        return cancelledTasksCount != null ? cancelledTasksCount : 0;
    }

    public void setCancelledTasksCount(Integer cancelledTasksCount) {
        this.cancelledTasksCount = cancelledTasksCount;
    }

    public Integer getWaitingForTasksCount() {
        return waitingForTasksCount != null ? waitingForTasksCount : 0;
    }

    public void setWaitingForTasksCount(Integer waitingForTasksCount) {
        this.waitingForTasksCount = waitingForTasksCount;
    }

    public Integer getClosedTasksCount() {
        return closedTasksCount != null ? closedTasksCount : 0;
    }

    public void setClosedTasksCount(Integer closedTasksCount) {
        this.closedTasksCount = closedTasksCount;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void updateInProgressTasksCount(Integer inProgressTasksCount) {
        if (this.inProgressTasksCount == null) {
            this.inProgressTasksCount = 0;
        }
        this.inProgressTasksCount += inProgressTasksCount != null ? inProgressTasksCount : 0;
    }

    public void updateCompletedTasksCount(Integer completedTasksCount) {
        if (this.completedTasksCount == null) {
            this.completedTasksCount = 0;
        }
        this.completedTasksCount += completedTasksCount != null ? completedTasksCount : 0;
    }

    public void updateClosedTasksCount(Integer closedTasksCount) {
        if (this.closedTasksCount == null) {
            this.closedTasksCount = 0;
        }
        this.closedTasksCount += closedTasksCount != null ? closedTasksCount : 0;
    }

    public void updateWaitingForTasksCount(Integer waitingForTasksCount) {
        if (this.waitingForTasksCount == null) {
            this.waitingForTasksCount = 0;
        }
        this.waitingForTasksCount += waitingForTasksCount != null ? waitingForTasksCount : 0;
    }

    public void updateCancelledTasksCount(Integer cancelledTasksCount) {
        if (this.cancelledTasksCount == null) {
            this.cancelledTasksCount = 0;
        }
        this.cancelledTasksCount += cancelledTasksCount != null ? cancelledTasksCount : 0;
    }

    public ArrayList<SelectItem> getBackupManagers() {
        return backupManagers;
    }

    public void setBackupManagers(ArrayList<SelectItem> backupManagers) {
        this.backupManagers = backupManagers;
    }

	public Integer getTaskGanttOrder() {
		return taskGanttOrder;
	}

	public void setTaskGanttOrder(Integer taskGanttOrder) {
		this.taskGanttOrder = taskGanttOrder;
	}
}
