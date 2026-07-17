package com.workforcetrack.mobile.rpc.calendar;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.workforcetrack.api.controllers.TaskApiController.*;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taskListItem")
public class MTaskListItem implements Serializable {

    private Integer objectID;
    private String name;
    private MNumberData number;
    private String numberString;
    private String description;
    private String statusName;
    private Integer statusID;
    private String priorityName;
    private Integer priorityID;
    private Integer projectID;
    private String projectName;
    private String projectCustomerName;
    private String lastModifiedBy;
    private Date lastModified;
    private Date startDate;
    private Date endDate;
    private Date dueDate;
    private Float percent;
    private Integer projectManagerID;
    private Integer projectBackupManagerID;
    private ArrayList<Integer> projectBackupManagerIDs;
    private String taskCreator;
    private Integer taskCreatorID;
    private boolean multiDay;
    private Boolean billable = true;
    private Boolean allDay = false;

    private String client;
    private Integer taskStatusID;
    private String complete;
    private Integer actualTime;

/*
    @XmlElementWrapper(name = "taskAssignees")
    @XmlElement(name = "taskAssignee")
    private List<MIdTime> taskAssignees;
*/

    private String projectManager;
    private String projectBackupManager;

    private List<MSelectItem> assignee;
    private Integer permission;

/*
private String quickbookTaskID;
private String quickbookEditSequence;
private Float myPercent;
private boolean isCurrentApprover;
private String encryptedID;
private Integer estimatedTime;
private String projectManager;
private String projectBackupManager;
private String parentTask;
private SelectItem[] subtasks;
private SelectItem[] predecessorTasks;
private SelectItem[] successorTasks;
private Integer actualTime;
private Date actualStartDate;
private Date actualEndDate;
private Integer workstreamID;
private String workstreamName;
private String clientName;
private IdTime[] projectEmployees;
private TaskInvolvedMember[] involvedMembers;
private FileItem[] attachments;
private FileResource[] taskAttachments;

private int permission;
private SelectItem[] status;
private SelectItem[] viewAs;
private Integer viewAsID;
private Integer reminderTimes;

private boolean hasGoogleAccount;
private ArrayList<CalendarEventReminder> reminder = new ArrayList<CalendarEventReminder>();

private RecurrenceJobItem recurrenceJobItem;
private Integer recurrenceId;
private Date fireTime;
private Integer instancesCount;
private Integer objectID;
private String name;
private String number;
private String description;
private String statusName;
private String priorityName;
private Integer projectId;
private String projectName;
private String lastModifiedBy;
private Date lastModified;
private Date startDate;
private Date endDate;
private Date dueDate;
private String assignedto;
private String complete;
private String hoursSpent;
private Boolean newTask;
private String client;
private String highlite;
private Integer taskStatusId;
private Integer estimated;
//private PermissionListItem permissions;
private Boolean billable;
private String googleID;
private String parentWorkstreamName;
private Boolean allDay;

private Integer projectManagerID;
private Integer projectBackupManagerID;
private Integer taskCreatorID;

*/

    public MTaskListItem() {
    }

    public MTaskListItem(ActivityItem item) {
        if (item != null) {
            this.objectID = item.getTaskObjectId();
            this.name = item.getSubject();
            this.statusName = item.getStatus();
            this.startDate = item.getCreationDate();
            this.startDate = item.getStartDate();
            this.dueDate = item.getDueDate();
            this.percent = item.getPercent();
        }
    }

    // FOR API
    public MTaskListItem(Map<String, Object> map) throws ParseException, ClassCastException {
        if (map != null && !map.isEmpty()) {
            this.objectID = (Integer) map.get(OBJECT_ID);
            this.name = (String) map.get(NAME);
            this.description = (String) map.get(DESCRIPTION);
            //this.number = (String) map.get(NUMBER);
            this.priorityID = (Integer) map.get(PRIORITY_ID);
            this.statusID = (Integer) map.get(STATUS_ID);
            this.projectID = (Integer) map.get(PROJECT_ID);
            this.projectManagerID = (Integer) map.get(PROJECT_MANAGER_ID);
            this.projectBackupManagerID = (Integer) map.get(PROJECT_BACKUP_MANAGER_ID);
            this.projectBackupManagerIDs = (ArrayList) map.get(PROJECT_BACKUP_MANAGER_IDS);
            if (map.get(PERCENT) != null) {
                if (map.get(PERCENT) instanceof Double) {
                    this.percent = (float) ((Double) map.get(PERCENT)).doubleValue();
                } else {
                    this.percent = (Float) map.get(PERCENT);
                }
            }
            //this.percent = (Float) map.get(PERCENT);
            SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
            this.startDate = (map.get(START_DATE) != null) ? dateFormat.parse((String) map.get(START_DATE)) : null;
            this.endDate = (map.get(END_DATE) != null) ? dateFormat.parse((String) map.get(END_DATE)) : null;
            this.dueDate = (map.get(DUE_DATE) != null) ? dateFormat.parse((String) map.get(DUE_DATE)) : null;

            this.billable = (Boolean) map.get(BILLABLE);
            List<Map<String, Object>> assigneesMap = (List<Map<String, Object>>) map.get(ASSIGNEE);
            if (assigneesMap != null) {
                this.assignee = new ArrayList<>();
                for (Map<String, Object> assigneeMap : assigneesMap) {
                    this.assignee.add(new MSelectItem((Integer) assigneeMap.get(OBJECT_ID), null));
                }
            }
        }
    }

    public MTaskListItem(TaskListItem taskListItem) {
        if (taskListItem != null) {
            this.objectID = taskListItem.getObjectID();
            this.name = taskListItem.getName();
            this.numberString = taskListItem.getNumber();
            this.description = taskListItem.getDescription();
            this.statusName = taskListItem.getStatusName();
            this.projectID = taskListItem.getProjectId();
            this.priorityName = taskListItem.getPriorityName();
            this.projectName = taskListItem.getProjectName();
            this.lastModified = taskListItem.getLastModified();
            this.startDate = taskListItem.getStartDate();
            this.endDate = taskListItem.getEndDate();
            this.dueDate = taskListItem.getDueDate();
            this.billable = taskListItem.isBillable();
            // this.googleID = taskListItem.getGoogleID();
            this.allDay = taskListItem.isAllDay();
            this.projectManagerID = taskListItem.getProjectManagerID();
            this.projectBackupManagerID = taskListItem.getProjectBackupManagerID();
            if (taskListItem.getProjectBackupManagerIDs() != null && taskListItem.getProjectBackupManagerIDs().size() > 0) {
                this.projectBackupManagerIDs = new ArrayList<>();
                for (Integer projectBackupManagerID : taskListItem.getProjectBackupManagerIDs()) {
                    if (projectBackupManagerID != null) {
                        this.projectBackupManagerIDs.add(projectBackupManagerID);
                    }
                }
            }

            this.taskCreatorID = taskListItem.getTaskCreatorID();
            this.billable = taskListItem.isBillable();
            this.allDay = taskListItem.isAllDay();

            this.complete = taskListItem.getComplete();
            this.client = taskListItem.getClient();
        }
    }

    public MTaskListItem(TaskSingleItem taskSingleItem) {
        if (taskSingleItem != null) {
            this.objectID = taskSingleItem.getObjectID();
            this.name = taskSingleItem.getName();
            this.number = new MNumberData(taskSingleItem.getNumberData());
            this.description = taskSingleItem.getDescription();
            this.priorityID = taskSingleItem.getPriorityID();
            this.statusName = taskSingleItem.getStatusName();
            this.priorityName = taskSingleItem.getPriorityName();
            this.projectName = taskSingleItem.getProjectName();
            this.lastModified = taskSingleItem.getLastModified();
            this.startDate = taskSingleItem.getStartDate();
            this.endDate = taskSingleItem.getEndDate();
            this.dueDate = taskSingleItem.getDueDate();
            this.billable = taskSingleItem.getBillable();
            //this.googleID = taskSingleItem.getGoogleID();
            this.allDay = taskSingleItem.isAllDay();
            this.projectManagerID = taskSingleItem.getProjectManagerID();
            this.projectManager = taskSingleItem.getProjectManager();
            this.projectBackupManagerID = taskSingleItem.getProjectBackupManagerID();
            if (taskSingleItem.getBackupManagers() != null && taskSingleItem.getBackupManagers().size() > 0) {
                ArrayList<Integer> backupManagerIDs = new ArrayList<>();
                for (SelectItem item : taskSingleItem.getBackupManagers()) {
                    if (item != null && item.getId() != null) {
                        backupManagerIDs.add(item.getId());
                    }
                }
                this.projectBackupManagerIDs = backupManagerIDs;
            }
            this.projectBackupManager = taskSingleItem.getProjectBackupManager();
            this.taskCreatorID = taskSingleItem.getTaskCreatorID();
            this.client = taskSingleItem.getClientName();

            this.statusID = taskSingleItem.getStatusID();
            this.projectID = taskSingleItem.getProjectID();
            this.lastModifiedBy = taskSingleItem.getLastModifiedBy();
            this.percent = taskSingleItem.getPercent();
            this.taskCreatorID = taskSingleItem.getTaskCreatorID();
            this.taskCreator = taskSingleItem.getTaskCreator();
            this.multiDay = taskSingleItem.isMultiDay();
            this.actualTime = taskSingleItem.getActualTime();

            if (taskSingleItem.getInvolvedMembers() != null && taskSingleItem.getInvolvedMembers().length > 0) {
                List<MSelectItem> assignees = new ArrayList<>();
                for (TaskInvolvedMember member : taskSingleItem.getInvolvedMembers()) {
                    assignees.add(new MSelectItem(member.getEmployeeID(), member.getEmployee()));
                }
                this.assignee = assignees;
            }
        }
    }


    public MTaskListItem(EditTask editTask) {
        if (editTask != null) {
            this.objectID = editTask.getObjectID();
            this.name = editTask.getName();
            this.number = new MNumberData(editTask.getNumberData());
            this.description = editTask.getDescription();
            this.projectID = editTask.getProjectId();
            this.projectName = editTask.getProjectName();
            this.startDate = editTask.getStartDate();
            this.endDate = editTask.getEndDate();
            this.dueDate = editTask.getDueDate();
            this.billable = editTask.getBillable();
            this.allDay = editTask.isAllDay();
            this.percent = editTask.getPercent();
            this.statusID = editTask.getStatusId();
            this.priorityID = editTask.getPriorityId();
            this.permission = editTask.getPermission();
        }
    }

    public static MTaskListItem getForMK(TaskSingleItem item) {
        MTaskListItem resultItem = new MTaskListItem(item);
        if (item.getInvolvedMembers() != null && item.getInvolvedMembers().length > 0) {
            List<MSelectItem> assignees = new ArrayList<>();
            for (TaskInvolvedMember member : item.getInvolvedMembers()) {
                assignees.add(new MSelectItem(member.getEmployeeID(), member.getEmployee()));
            }
            resultItem.setAssignee(assignees);
        }
        return resultItem;
    }


    public TaskListItem convertToTaskListItem(TaskListItem taskListItem) {
        if (taskListItem == null) {
            taskListItem = new TaskListItem();
        }

        taskListItem.setObjectID(this.objectID);
        taskListItem.setName(this.name);
        taskListItem.setDescription(this.description);
        taskListItem.setStatusName(this.statusName);
        taskListItem.setPriorityName(this.priorityName);
        taskListItem.setProjectName(this.projectName);
        taskListItem.setLastModified(this.lastModified);
        taskListItem.setStartDate(this.startDate);
        taskListItem.setEndDate(this.endDate);
        taskListItem.setDueDate(this.dueDate);
        taskListItem.setBillable(this.billable);
        //taskListItem.setGoogleID(this.googleID);
        taskListItem.setAllDay(this.allDay);
        taskListItem.setProjectManagerID(this.projectManagerID);
        taskListItem.setProjectManagerName(this.projectManager);
        taskListItem.setProjectBackupManagerID(this.projectBackupManagerID);
        taskListItem.setProjectBackupManagerIDs(this.projectBackupManagerIDs);
        taskListItem.setTaskCreatorID(this.taskCreatorID);
        taskListItem.setComplete(this.complete);
        taskListItem.setClient(this.client);

        return taskListItem;
    }

    public TaskSingleItem convertToTaskSingleItem(TaskSingleItem taskSingleItem) {
        if (taskSingleItem == null) {
            taskSingleItem = new TaskSingleItem();
        }
        taskSingleItem.setObjectID(this.objectID == null || this.objectID.equals(0) ? null : this.objectID);
        taskSingleItem.setName(this.name);
        taskSingleItem.setDescription(this.description);
        taskSingleItem.setStatusName(this.statusName);
        taskSingleItem.setProjectID(this.projectID == null || this.projectID.equals(0) ? null : this.projectID);
        taskSingleItem.setProjectName(this.projectName);
        taskSingleItem.setLastModified(this.lastModified);
        taskSingleItem.setStartDate(this.startDate);
        taskSingleItem.setEndDate(this.endDate);
        taskSingleItem.setDueDate(this.dueDate);
        taskSingleItem.setBillable(this.billable);

        taskSingleItem.setAllDay(this.allDay);
        taskSingleItem.setProjectManagerID(this.projectManagerID == null || this.projectManagerID.equals(0) ? null : this.projectManagerID);
        taskSingleItem.setProjectBackupManagerID(this.projectBackupManagerID == null || this.projectBackupManagerID.equals(0) ? null : this.projectBackupManagerID);
        taskSingleItem.setTaskCreatorID(this.taskCreatorID == null || this.taskCreatorID.equals(0) ? null : this.taskCreatorID);
        taskSingleItem.setClientName(this.client);

        taskSingleItem.setStatusID(this.statusID);
        taskSingleItem.setPriorityID(this.priorityID == null || this.priorityID.equals(0) ? null : this.priorityID);
        taskSingleItem.setLastModifiedBy(this.lastModifiedBy);
        taskSingleItem.setPercent(this.percent);
        taskSingleItem.setTaskCreator(this.taskCreator);
        taskSingleItem.setMultiDay(this.multiDay);
        taskSingleItem.setActualTime(this.actualTime);

        if (this.assignee != null) {
            ArrayList<IdTime> assignees = new ArrayList<>();
            for (MSelectItem mIdTime : this.assignee) {
                assignees.add(new IdTime(mIdTime.getObjectID(), null));
            }
            taskSingleItem.setProjectEmployees(assignees.toArray(new IdTime[]{}));
        }

        return taskSingleItem;
    }

    public EditTask convertToEditTask(EditTask editTask) {
        if (editTask == null) {
            editTask = new EditTask();
        }
        editTask.setObjectID(this.objectID);
        editTask.setName(this.name);
        editTask.setDescription(this.description);
        editTask.setProjectName(this.projectName);
        editTask.setStartDate(this.startDate);
        editTask.setEndDate(this.endDate);
        editTask.setDueDate(this.dueDate);
        editTask.setBillable(this.billable);
        editTask.setAllDay(this.allDay);
        editTask.setProjectId(this.projectID);
        editTask.setPercent(this.percent);
        editTask.setStatusId(this.statusID);
        editTask.setPriorityId(this.priorityID);
        editTask.setProjectName(this.priorityName);
        return editTask;
    }

    public static boolean convert(TaskSingleItem taskSingleItem, MTaskListItem mTaskListItem, boolean fromTaskSingleItem) {

        if (taskSingleItem == null || mTaskListItem == null) {
            return false;
        }

        try {

            if (fromTaskSingleItem) {
                mTaskListItem.setObjectID(taskSingleItem.getObjectID());
                mTaskListItem.setName(taskSingleItem.getName());
                mTaskListItem.setDescription(taskSingleItem.getDescription());
                mTaskListItem.setStatusName(taskSingleItem.getStatusName());
                mTaskListItem.setPriorityName(taskSingleItem.getPriorityName());
                mTaskListItem.setProjectName(taskSingleItem.getProjectName());
                mTaskListItem.setLastModified(taskSingleItem.getLastModified());
                mTaskListItem.setStartDate(taskSingleItem.getStartDate());
                mTaskListItem.setEndDate(taskSingleItem.getEndDate());
                mTaskListItem.setDueDate(taskSingleItem.getDueDate());
                mTaskListItem.setBillable(taskSingleItem.getBillable());

                mTaskListItem.setAllDay(taskSingleItem.isAllDay());
                mTaskListItem.setProjectManagerID(taskSingleItem.getProjectManagerID());
                mTaskListItem.setProjectBackupManagerID(taskSingleItem.getProjectBackupManagerID());
                ArrayList<Integer> backupManagerIDs = new ArrayList<>();
                for (SelectItem backupManager : taskSingleItem.getBackupManagers()) {
                    backupManagerIDs.add(backupManager.getId());
                }
                mTaskListItem.setProjectBackupManagerIDs(backupManagerIDs);
                mTaskListItem.setTaskCreatorID(taskSingleItem.getTaskCreatorID());

                mTaskListItem.setStatusID(taskSingleItem.getStatusID());
                mTaskListItem.setPriorityID(taskSingleItem.getPriorityID());
                mTaskListItem.setLastModifiedBy(taskSingleItem.getLastModifiedBy());
                mTaskListItem.setPercent(taskSingleItem.getPercent());
                mTaskListItem.setTaskCreator(taskSingleItem.getTaskCreator());
                mTaskListItem.setMultiDay(taskSingleItem.isMultiDay());
                mTaskListItem.setBillable(taskSingleItem.getBillable());
                mTaskListItem.setAllDay(taskSingleItem.isAllDay());


            } else {
                taskSingleItem.setObjectID(mTaskListItem.getObjectID());
                taskSingleItem.setName(mTaskListItem.getName());
                taskSingleItem.setDescription(mTaskListItem.getDescription());
                taskSingleItem.setStatusName(mTaskListItem.getStatusName());
                taskSingleItem.setPriorityName(mTaskListItem.getPriorityName());
                taskSingleItem.setProjectName(mTaskListItem.getProjectName());
                taskSingleItem.setLastModified(mTaskListItem.getLastModified());
                taskSingleItem.setStartDate(mTaskListItem.getStartDate());
                taskSingleItem.setEndDate(mTaskListItem.getEndDate());
                taskSingleItem.setDueDate(mTaskListItem.getDueDate());
                taskSingleItem.setBillable(mTaskListItem.getBillable());

                taskSingleItem.setAllDay(mTaskListItem.getAllDay());
                taskSingleItem.setProjectManagerID(mTaskListItem.getProjectManagerID());
                taskSingleItem.setProjectBackupManagerID(mTaskListItem.getProjectBackupManagerID());
                taskSingleItem.setTaskCreatorID(mTaskListItem.getTaskCreatorID());

                taskSingleItem.setStatusID(mTaskListItem.getStatusID());
                taskSingleItem.setPriorityID(mTaskListItem.getPriorityID());
                taskSingleItem.setLastModifiedBy(mTaskListItem.getLastModifiedBy());
                taskSingleItem.setPercent(mTaskListItem.getPercent());
                taskSingleItem.setTaskCreator(mTaskListItem.getTaskCreator());
                taskSingleItem.setMultiDay(mTaskListItem.isMultiDay());
                taskSingleItem.setBillable(mTaskListItem.getBillable());
                taskSingleItem.setAllDay(mTaskListItem.getAllDay());

            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean convert(TaskListItem taskListItem, MTaskListItem mTaskListItem, boolean fromTaskListItem) {

        if (taskListItem == null || mTaskListItem == null) {
            return false;
        }

        try {

            if (fromTaskListItem) {
                mTaskListItem.setObjectID(taskListItem.getObjectID());
                mTaskListItem.setName(taskListItem.getName());
                mTaskListItem.setDescription(taskListItem.getDescription());
                mTaskListItem.setStatusName(taskListItem.getStatusName());
                mTaskListItem.setPriorityName(taskListItem.getPriorityName());
                mTaskListItem.setProjectName(taskListItem.getProjectName());
                mTaskListItem.setLastModified(taskListItem.getLastModified());
                mTaskListItem.setStartDate(taskListItem.getStartDate());
                mTaskListItem.setEndDate(taskListItem.getEndDate());
                mTaskListItem.setDueDate(taskListItem.getDueDate());
                mTaskListItem.setBillable(taskListItem.isBillable());
                mTaskListItem.setAllDay(taskListItem.isAllDay());
                mTaskListItem.setProjectManagerID(taskListItem.getProjectManagerID());
                mTaskListItem.setProjectBackupManagerID(taskListItem.getProjectBackupManagerID());
                mTaskListItem.setProjectBackupManagerIDs(taskListItem.getProjectBackupManagerIDs());
                mTaskListItem.setTaskCreatorID(taskListItem.getTaskCreatorID());

            } else {
                taskListItem.setObjectID(mTaskListItem.getObjectID());
                taskListItem.setName(mTaskListItem.getName());
                taskListItem.setDescription(mTaskListItem.getDescription());
                taskListItem.setStatusName(mTaskListItem.getStatusName());
                taskListItem.setPriorityName(mTaskListItem.getPriorityName());
                taskListItem.setProjectName(mTaskListItem.getProjectName());
                taskListItem.setLastModified(mTaskListItem.getLastModified());
                taskListItem.setStartDate(mTaskListItem.getStartDate());
                taskListItem.setEndDate(mTaskListItem.getEndDate());
                taskListItem.setDueDate(mTaskListItem.getDueDate());
                taskListItem.setBillable(mTaskListItem.getBillable());
                taskListItem.setAllDay(mTaskListItem.getAllDay());
                taskListItem.setProjectManagerID(mTaskListItem.getProjectManagerID());
                taskListItem.setProjectManagerName(mTaskListItem.getProjectManager());
                taskListItem.setProjectBackupManagerID(mTaskListItem.getProjectBackupManagerID());
                taskListItem.setProjectBackupManagerIDs(mTaskListItem.getProjectBackupManagerIDs());
                taskListItem.setTaskCreatorID(mTaskListItem.getTaskCreatorID());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static boolean convert(EditTask editTask, MTaskListItem mTaskListItem, boolean fromEditTask) {

        if (editTask == null || mTaskListItem == null) {
            return false;
        }

        try {

            if (fromEditTask) {
                mTaskListItem.setObjectID(editTask.getObjectID());
                mTaskListItem.setName(editTask.getName());
                mTaskListItem.setDescription(editTask.getDescription());
                mTaskListItem.setProjectName(editTask.getProjectName());
                mTaskListItem.setStartDate(editTask.getStartDate());
                mTaskListItem.setEndDate(editTask.getEndDate());
                mTaskListItem.setDueDate(editTask.getDueDate());
                mTaskListItem.setBillable(editTask.getBillable());
                mTaskListItem.setAllDay(editTask.isAllDay());

            } else {
                editTask.setObjectID(mTaskListItem.getObjectID());
                editTask.setName(mTaskListItem.getName());
                editTask.setDescription(mTaskListItem.getDescription());
                editTask.setProjectName(mTaskListItem.getProjectName());
                editTask.setStartDate(mTaskListItem.getStartDate());
                editTask.setEndDate(mTaskListItem.getEndDate());
                editTask.setDueDate(mTaskListItem.getDueDate());
                editTask.setBillable(mTaskListItem.getBillable());
                editTask.setAllDay(mTaskListItem.getAllDay());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getTaskStatusID() {
        return taskStatusID;
    }

    public void setTaskStatusID(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
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

    public MNumberData getNumber() {
        return number;
    }

    public void setNumber(MNumberData number) {
        this.number = number;
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

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
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

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
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

    public ArrayList<Integer> getProjectBackupManagerIDs() {
        return projectBackupManagerIDs;
    }

    public void setProjectBackupManagerIDs(ArrayList<Integer> projectBackupManagerIDs) {
        this.projectBackupManagerIDs = projectBackupManagerIDs;
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

    public boolean isMultiDay() {
        return multiDay;
    }

    public void setMultiDay(boolean multiDay) {
        this.multiDay = multiDay;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
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

    public List<MSelectItem> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<MSelectItem> assignee) {
        this.assignee = assignee;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }
}
