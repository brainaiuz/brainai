package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.task.client.rpc.PermissionListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class TaskSingleItem extends TaskBaseItem implements IsSerializable, UserGrant {
    public static TaskSingleItem DEFAULT = new TaskSingleItem(/*DateUtil.getDateWithZeroMinutes(*/new Date());

    private String quickbookTaskID;
    private String quickbookEditSequence;
    private String lastModifiedBy;
    private Date lastModified;
    private Date endDate;
    private Float percent;
    private Float myPercent;
    private boolean isManager;
    private String encryptedID;
    private Integer estimatedTime;
    private Integer actualTime;
    private BigDecimal taskAmount;
    private String estimatedCost;
    private String actualCost;
    private String projectManager;
    private String projectBackupManager;
    private Integer projectManagerID;
    private Integer projectBackupManagerID;
    private String taskCreator;
    private Integer taskCreatorID;
    private Date taskCreationTime;
    private Integer employeeID;
    private String parentTask;
    private SelectItem[] subtasks;
    private SelectItem[] predecessorTasks;
    private SelectItem[] successorTasks;
    private Date actualStartDate;
    private Date actualEndDate;
    private Integer workstreamID;
    private String workstreamName;
    private String clientName;
    private IdTime[] projectEmployees;
    private TaskInvolvedMember[] involvedMembers;
    private FileItem[] attachments;
    private FileResource[] taskAttachments;
    private PositionsSelectItem[] issueEmployees;
    private ArrayList<Integer> issueEmployeeIDs;
    private HashMap<Integer, PositionsSelectItem> issueEmployeeItems;
    private Integer timeSpent;
    private ArrayList<HistoryListItem> notes;
    private SelectItem[] taskStatuses;
    private PermissionListItem permissions;

    private String typeCode;
    private String typeName;

    private int permission;
    private SelectItem[] viewAs;
    private Integer viewAsID;
    private Integer reminderTimes;

    private boolean multiDay;
    private Boolean allDay = true;
    private boolean hasGoogleAccount;
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private String googleID;
    private String officeID;
    private RecurrenceJobItem recurrenceJobItem;
    private Integer recurrenceId;
    private Integer baseTaskID;
    private Date fireTime;
    private Integer instancesCount;
    private boolean timerIsStarted = false;
    private boolean showTimer = false;
    private boolean showLogTime = false;
    private boolean dontKeepDelays = true;
    private boolean showInTimesheet = true;
    private boolean isWorkstream = false;

    private String action;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    //for import tasks from quickbook (added by normurod)
    //private Date creationTime;
    private NumberData numberData;

    private String savedTaskNumberFormula;

    private ClockItem timer;

    private HashSet<String> taskPermissions;

    private boolean fromQuickbooks;

    private boolean isSupplier;
    private Boolean atLeastOneTimerIsRunning;
    private Integer taskGanttOrder;
    private Boolean withoutAssignees = Boolean.FALSE; // Copy Workstream da tasklarni unassigned qilib ko`chirish uchun ishlatilgan;
    private boolean workflowTask = false;
    private String workflowStartDate;
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;
    private Integer workflowID;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;

    private String workflowActionStartTimeGranularity;

    private String waitingHours;
    private String rejectedHours;

    private String visibilityStatus;

    private Integer dayCount;
    private Integer timeZoneOffset;
    private String projectStatusCode;
    private boolean isCallModal = false;

    private String statusColor;

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

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    private TaskSingleItem(Date start) {
        this.startDate = start;
        this.dueDate = DateUtil.addHours(start, 1);
    }

    public SelectItem[] getViewAs() {
        return viewAs;
    }

    public void setViewAs(SelectItem[] viewAs) {
        this.viewAs = viewAs;
    }

    public Integer getViewAsID() {
        return viewAsID;
    }

    public void setViewAsID(Integer viewAsID) {
        this.viewAsID = viewAsID;
    }

    public TaskSingleItem() {
    }

    public TaskSingleItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getPercent() {
        return percent != null ? BigDecimal.valueOf(percent).setScale(2, RoundingMode.HALF_UP).floatValue() : 0f;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectBackupManager() {
        return projectBackupManager;
    }

    public void setProjectBackupManager(String projectBackupManager) {
        this.projectBackupManager = projectBackupManager;
    }

    public Integer getProjectManagerID() {
        return projectManagerID;
    }

    public void setProjectManagerID(Integer projectManagerID) {
        this.projectManagerID = projectManagerID;
    }

    public Integer getProjectBackupManagerID() {
        return projectBackupManagerID;
    }

    public void setProjectBackupManagerID(Integer projectBackupManagerID) {
        this.projectBackupManagerID = projectBackupManagerID;
    }

    public TaskInvolvedMember[] getInvolvedMembers() {
        return involvedMembers;
    }

    public void setInvolvedMembers(TaskInvolvedMember[] involvedMembers) {
        this.involvedMembers = involvedMembers;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getEncryptedID() {
        return encryptedID;
    }

    public void setEncryptedID(String encryptedID) {
        this.encryptedID = encryptedID;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(String taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Integer getTaskCreatorID() {
        return taskCreatorID;
    }

    public void setTaskCreatorID(Integer taskCreatorID) {
        this.taskCreatorID = taskCreatorID;
    }

    public Date getTaskCreationTime() {
        return taskCreationTime;
    }

    public void setTaskCreationTime(Date taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public void setSubtasks(SelectItem[] subtasks) {
        this.subtasks = subtasks;
    }

    public SelectItem[] getSubtasks() {
        return subtasks;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
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

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public SelectItem[] getPredecessorTasks() {
        return predecessorTasks;
    }

    public void setPredecessorTasks(SelectItem[] predecessorTasks) {
        this.predecessorTasks = predecessorTasks;
    }

    public SelectItem[] getSuccessorTasks() {
        return successorTasks;
    }

    public void setSuccessorTasks(SelectItem[] successorTasks) {
        this.successorTasks = successorTasks;
    }

    public Integer getWorkstreamID() {
        return workstreamID;
    }

    public void setWorkstreamID(Integer workstreamID) {
        this.workstreamID = workstreamID;
    }

    public String getWorkstreamName() {
        return workstreamName;
    }

    public void setWorkstreamName(String workstreamName) {
        this.workstreamName = workstreamName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public IdTime[] getProjectEmployees() {
        return projectEmployees;
    }

    public void setProjectEmployees(IdTime[] projectEmployees) {
        this.projectEmployees = projectEmployees;
    }

    public Float getMyPercent() {
        return myPercent;
    }

    public void setMyPercent(Float myPercent) {
        this.myPercent = myPercent;
    }

    public Integer getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(Integer reminderTimes) {
        this.reminderTimes = reminderTimes;
    }

    public FileResource[] getTaskAttachments() {
        return taskAttachments;
    }

    public void setTaskAttachments(FileResource[] taskAttachments) {
        this.taskAttachments = taskAttachments;
    }

    public ArrayList<CalendarEventReminder> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<CalendarEventReminder> reminder) {
        this.reminder = reminder;
    }

    public boolean isHasGoogleAccount() {
        return hasGoogleAccount;
    }

    public void setHasGoogleAccount(boolean hasGoogleAccount) {
        this.hasGoogleAccount = hasGoogleAccount;
    }

    public boolean isMultiDay() {
        return multiDay;
    }

    public void setMultiDay(boolean multiDay) {
        this.multiDay = multiDay;
    }

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public Integer getBaseTaskID() {
        return baseTaskID;
    }

    public void setBaseTaskID(Integer baseTaskID) {
        this.baseTaskID = baseTaskID;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getInstancesCount() {
        return instancesCount;
    }

    public void setInstancesCount(Integer instancesCount) {
        this.instancesCount = instancesCount;
    }

    public TaskSingleItem clone() {
        TaskSingleItem clone = new TaskSingleItem();
        clone.setAllDay(this.allDay);
        clone.setProjectEmployees(this.projectEmployees);
        clone.setTaskCreator(this.taskCreator);
        clone.setDueDate(this.dueDate);
//        clone.setMultiDay(this.multiDay);
        clone.setStartDate(this.startDate);
        clone.setName(this.name);
//        clone.setReminder(this.reminder);
        clone.setProjectID(projectID);
        clone.setPriorityID(priorityID);
        clone.setProjectName(projectName);
        clone.setPriorityName(priorityName);
        clone.setRecurrenceJobItem(this.recurrenceJobItem);
        return clone;
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

    /*public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }*/
    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
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

    public boolean isShowLogTime() {
        return showLogTime;
    }

    public void setShowLogTime(boolean showLogTime) {
        this.showLogTime = showLogTime;
    }

    public boolean isDontKeepDelays() {
        return dontKeepDelays;
    }

    public void setDontKeepDelays(boolean dontKeepDelays) {
        this.dontKeepDelays = dontKeepDelays;
    }

    public String getSavedTaskNumberFormula() {
        return savedTaskNumberFormula;
    }

    public void setSavedTaskNumberFormula(String savedTaskNumberFormula) {
        this.savedTaskNumberFormula = savedTaskNumberFormula;
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

    public void reset() {
        this.objectID = null;
        this.quickbookTaskID = null;
        this.quickbookEditSequence = null;
        this.name = null;
        this.description = null;
        this.startDate = DateUtil.resetTime(new Date());
        this.endDate = new Date();
        this.dueDate = new Date();
        this.statusID = null;
        this.statusName = null;
        this.priorityID = null;
        this.priorityName = null;
        this.typeID = null;
        this.typeCode = null;
        this.typeName = null;
        this.projectID = null;
        this.projectName = null;
        this.lastModified = null;
        this.lastModifiedBy = null;
        this.percent = null;
        this.myPercent = null;
        this.encryptedID = null;
        this.estimatedTime = null;
        this.projectManagerID = null;
        this.projectManager = null;
        this.projectBackupManagerID = null;
        this.projectBackupManager = null;
        this.taskCreator = null;
        this.taskCreatorID = null;
        this.taskCreationTime = null;
        this.employeeID = null;
        this.parentTask = null;
        this.subtasks = null;
        this.predecessorTasks = null;
        this.successorTasks = null;
        this.actualTime = null;
        this.actualStartDate = null;
        this.actualEndDate = null;
        this.workstreamID = null;
        this.workstreamName = null;
        this.clientName = null;
        this.projectEmployees = null;
        this.involvedMembers = null;
        this.attachments = null;
        this.taskAttachments = null;
        this.billable = false;
        this.status = null;
        this.viewAs = null;
        this.viewAsID = null;
        this.reminder = null;
        this.reminderTimes = null;
        this.multiDay = false;
        this.allDay = true;
        this.hasGoogleAccount = false;
        this.googleID = null;
        this.recurrenceId = null;
        this.recurrenceJobItem = null;
        this.fireTime = null;
        this.instancesCount = null;
        this.action = null;
        this.customFieldItems = null;
        this.numberData = null;
        this.savedTaskNumberFormula = null;
        this.taskGanttOrder = null;
    }

    public ClockItem getTimer() {
        return timer;
    }

    public void setTimer(ClockItem timer) {
        this.timer = timer;
    }

    public HashSet<String> getTaskPermissions() {
        return this.taskPermissions;
    }

    public void setTaskPermissions(HashSet<String> permissions) {
        this.taskPermissions = permissions;
    }

    public boolean isShowInTimesheet() {
        return showInTimesheet;
    }

    public void setShowInTimesheet(boolean showInTimesheet) {
        this.showInTimesheet = showInTimesheet;
    }

    public boolean isWorkstream() {
        return isWorkstream;
    }

    public void setWorkstream(boolean workstream) {
        isWorkstream = workstream;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public Boolean getAtLeastOneTimerIsRunning() {
        return atLeastOneTimerIsRunning;
    }

    public void setAtLeastOneTimerIsRunning(Boolean atLeastOneTimerIsRunning) {
        this.atLeastOneTimerIsRunning = atLeastOneTimerIsRunning;
    }

    public Integer getTaskGanttOrder() {
        return taskGanttOrder;
    }

    public void setTaskGanttOrder(Integer taskGanttOrder) {
        this.taskGanttOrder = taskGanttOrder;
    }

    public Boolean getWithoutAssignees() {
        return withoutAssignees;
    }

    public void setWithoutAssignees(Boolean withoutAssignees) {
        this.withoutAssignees = withoutAssignees;
    }

    public boolean isWorkflowTask() {
        return workflowTask;
    }

    public void setWorkflowTask(boolean workflowTask) {
        this.workflowTask = workflowTask;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
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

    public HashMap<Integer, PositionsSelectItem> getIssueEmployeeItems() {
        return issueEmployeeItems;
    }

    public void setIssueEmployeeItems(HashMap<Integer, PositionsSelectItem> issueEmployeeItems) {
        this.issueEmployeeItems = issueEmployeeItems;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
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

    public SelectItem[] getTaskStatuses() {
        return taskStatuses;
    }

    public void setTaskStatuses(SelectItem[] taskStatuses) {
        this.taskStatuses = taskStatuses;
    }

    public String getVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(String visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public BigDecimal getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(BigDecimal taskAmount) {
        this.taskAmount = taskAmount;
    }

    public String getProjectStatusCode() {
        return projectStatusCode;
    }

    public void setProjectStatusCode(String projectStatusCode) {
        this.projectStatusCode = projectStatusCode;
    }

    public boolean isCallModal() {
        return isCallModal;
    }

    public void setCallModal(boolean callModal) {
        isCallModal = callModal;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
}
