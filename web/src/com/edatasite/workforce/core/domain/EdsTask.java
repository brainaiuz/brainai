package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.lucene.Indexable;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: Lochin Date: 27.03.2001 Time: 22:32:32
 * Software Team
 */


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "task")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsTask extends EdsTraceable implements PropertyChangeListener, ObjectHistory, Indexable/*, Traceable*/ {
    public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    public static final String TASK_TYPES = "_TASK_TYPES";

    public static final String TASK_PRIORITY = "_TASK_PRIORITY";
    public static final String HIGH = "_TASK_PRIORITY_HIGH";
    public static final String MEDIUM = "_TASK_PRIORITY_MEDIUM";
    public static final String LOW = "_TASK_PRIORITY_LOW";

    public static final String TASK_STATUS = "_TASK_STATUS";
    public static final String NOT_STARTED = "NOT_STARTED";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String COMPLETED = Constants._COMPLETED;
    public static final String CANCELLED = Constants.CANCELLED;
    public static final String ON_HOLD = Constants.ON_HOLD;
    public static final String CLOSED = Constants._CLOSED;
    public static final String WAITING_FOR_SOMEONE_ELSE = "WAITING_FOR_SOMEONE_ELSE";

    public static final String QUEUED = "Queued";
    public static final String ACTIVE = "Active";

    // My activity related constants
    public static final String _TASK_ACTIVITY = "_TASK_ACTIVITY";
    public static final String TASK_ADD = "TASK_ADD";
    public static final String TASK_ADD_ASSIGNEES = "TASK_ADD_ASSIGNEES";
    public static final String TASK_EDIT = "TASK_EDIT";
    public static final String TASK_EDIT_ASSIGNEES = "TASK_EDIT_ASSIGNEES";
    public static final String TASK_EDIT_STATUS = "TASK_EDIT_STATUS";
    // FIELD NAMES////
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";
    //CRM TASK STATUS...
    public static final String _CRM_TASK_STATUS = "_CRM_TASK_STATUS";
    public static final String _CRM_TASK_PRIORITY = "_CRM_TASK_PRIORITY";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public EdsTask() {
    }

    @Column(name = "estimatedtime")
    private Integer estimatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentwsid")
    private EdsWorkStream parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todolistassignee")
    private EdsUser toDoListAssignee;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "TaskPredecessor",
            joinColumns = {@JoinColumn(name = "taskId")},
            inverseJoinColumns = {@JoinColumn(name = "predecessorId")}
    )
    private Set<EdsTask> predecessors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "TaskPredecessor",
            joinColumns = {@JoinColumn(name = "predecessorId")},
            inverseJoinColumns = {@JoinColumn(name = "taskId")}
    )
    private Set<EdsTask> successors = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private Set<EdsEmployeeTask> assignments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private Set<EdsPositionTask> positions = new HashSet<>();

    @org.apache.solr.client.solrj.beans.Field("taskName")
    @Column(name = "name")
    @Type(type = "text")
    private String name;

    @org.apache.solr.client.solrj.beans.Field
    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "visibilitystatus")
    @Type(type = "text")
    private String visibility = TodoListItem.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priorityid")
    private EdsReference priority;

    @Transient
    private EdsReference difficulty; //difficulty and weight is numeric

    @Transient
    private int weight; //for calculation purposes..

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "actualStartDate")
    private Date actualStartDate;

    @Column(name = "actualEndDate")
    private Date actualEndDate;

    @Column(name = "dueDate")
    private Date dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    /**
     * @see com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceImpl#synchronizeEvents(Integer, java.util.Date, java.util.Date)
     */
    @Column(name = "lastModifiedDate")
    private Date lastModifiedDate;

    @Column(name = "percent")
    private Float percent;

    @Column(name = "previousPercent")
    private Float previousPercent;

    @Column(name = "timespent")
    private Integer timespent;

    @Column(precision = 25, scale = 5)
    private BigDecimal taskAmount;

    @Column(name = "rated")
    private Boolean rated = false;

    @Column(name = "billable")
    private Boolean billable = true;

    @Transient
    private String timespentHM;

    private Boolean onCriticalPath;

    @Column(name = "indexed")
    private Boolean indexed;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "deletedBy")
    private String deletedby;

    @Column(name = "reminderTime")
    private Integer reminderTime;

    @Column(name = "quickbook_task_id")
    private String quickbookTaskID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

    // In recurring tasks and events this will specify the recurrence frequency, but should not be used in WFT Recurrence Service.
    @Column(name = "recurrenceId")
    private Integer recurrenceID;

    // In recurring tasks and events this will specify the fire time (created time) of the current instance within the series of recurring tasks or events
    @Column(name = "fireTime")
    private Date fireTime;

    //Bad practice, a measure of quick "issue management" support solution.
    //Consider generification of EdsTask entity to propogate effective inheritance
    //of EdsIssue entity

    private Boolean isIssue; //Task in case it is null or false, Issue if true

    /**
     * We have created current variable in order to define which concrete
     * task is created in both in the system and google side. During
     * auto-syncronization we will not check in their title, we will check
     * for their taskIDs that store unique values.
     */
//    @Field
    @Column(name = "google_id")
    private String googleID;

    @Column(name = "officeid")
    @Type(type = "text")
    private String officeID;


    @Column(name = "all_day")
    private Boolean allDay = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskcustomfieldsid", unique = true)
    private EdsTaskCustomFields taskCustomFields;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "task")
    private List<EdsTaskEstimateTimeSpentHistory> estimateTimeSpentHistoryList = new ArrayList<>();

    @Column(name = "number")
    private String number;

    @Column(name = "saveNumberFormula")
    private String savedNumberFormula;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "todoListOrder") // tasks shown order by todoListOrder column in ToDoList
    private Integer todoListOrder;

    @Column(name = "is_changed_calculation_fields")
    private Boolean changedCalculationFields = true;

    @Column(name = "is_calculated")
    private Boolean calculated = false;

    private Double plannedWageAmount = 0.0;
    private Double plannedClientChargeAmount = 0.0;
    private Double actualWageAmount = 0.0;
    private Double actualClientChargeAmount = 0.0;
    private Double remainingWageAmount = 0.0;
    private Double remainingClientChargeAmount = 0.0;

    @Column(name = "showInTimesheet", columnDefinition = "boolean default true")
    private Boolean showInTimesheet = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private EdsIssue edsIssue;

    private Integer taskGanttOrder;
    @Column(name = "isworkflowitem", columnDefinition = "boolean default false")
    private boolean isWorkflowItem;

    private Integer workflowID;
    private String workflowStartDate;
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;

    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    @Column(name = "isactualtimereachednotifation", columnDefinition = "boolean default false")
    private boolean sentActualTimeReachedNotifation = false;

    private Integer timeZoneOffset;
    //Between start and end date
    private Integer dayCount;

    /*
     * Type of the task (General, Feature Request, Problem). Searchable/filterable from listing
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private EdsReference type;

    @Column(name = "kanban_order")
    private Long kanbanOrder;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "task")
    private List<EdsTaskStatusHistory> statusHistories = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private Set<EdsTaskChanges> taskHistory = new HashSet<>();

    public static String getAssigneeAsCommaDelimited(EdsTask task) {
        String assignee = null;
        if (task != null && task.getAssignments() != null && task.getAssignments().size() > 0) {
            assignee = "";
            boolean isFirst = true;
            StringBuilder assigneeBuilder = new StringBuilder(assignee);
            for (EdsEmployeeTask employeeTask : task.getAssignments()) {
                assigneeBuilder.append(isFirst ? "" : ", ").append(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName());
                isFirst = false;
            }
            assignee = assigneeBuilder.toString();
            return assignee;
        }
        return null;
    }

    public static Integer getOverAllTimeSpent(EdsTask task) {
        Integer timeSpent = 0;
        if (task != null && task.getAssignments() != null && task.getAssignments().size() > 0) {
            for (EdsEmployeeTask employeeTask : task.getAssignments()) {
                if (employeeTask.getTimeSheets() != null && employeeTask.getTimeSheets().size() > 0) {
                    for (EdsTimeSheet timeSheet : employeeTask.getTimeSheets()) {
                        timeSpent += (timeSheet.getTimeSpent() != null ? timeSheet.getTimeSpent() : 0);
                    }
                }
            }
        }
        return timeSpent;
    }


    public interface ChangeListener {
        void onStatusChange(EdsReference value);

        void onDatesChanged(Date startDate, Date dueDate);
    }

    private transient PropertyChangeSupport propertyChangeSupport;

    private transient ChangeListener changeListener;

    public void enableTaskChangeListener(ChangeListener changeListener) {
        if (changeListener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        this.changeListener = changeListener;
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(this);
    }

    public void disableTaskChangeListener() {
        this.changeListener = null;
        propertyChangeSupport.removePropertyChangeListener(this);
        this.propertyChangeSupport = null;
    }

    private transient boolean dateChanged = false;

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FIELD_STATUS)) {
            changeListener.onStatusChange((EdsReference) evt.getNewValue());
        }
        if (evt.getPropertyName().equals(FIELD_START_DATE) || evt.getPropertyName().equals(FIELD_END_DATE)) {
            if (!dateChanged) {
                dateChanged = true;
                changeListener.onDatesChanged(this.startDate, this.dueDate);
            }
        }
    }

    public boolean isSentActualTimeReachedNotifation() {
        return sentActualTimeReachedNotifation;
    }

    public void setSentActualTimeReachedNotifation(boolean sentActualTimeReachedNotifation) {
        this.sentActualTimeReachedNotifation = sentActualTimeReachedNotifation;
    }

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
        setWorkflowItem(this.workflowID != null);
    }

    public boolean isWorkflowItem() {
        return isWorkflowItem || this.workflowID != null;
    }

    public void setWorkflowItem(boolean isWorkflowItem) {
        this.setDeleted(this.deleted || isWorkflowItem);
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

    @Transient
    private boolean isNewItem = false;

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
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


    public Boolean getIssue() {
        return isIssue;
    }

    public void setIssue(Boolean issue) {
        isIssue = issue;
    }

    public EdsIssue getEdsIssue() {
        return edsIssue;
    }

    public void setEdsIssue(EdsIssue edsIssue) {
        this.edsIssue = edsIssue;
    }

    public EdsTask(EdsWorkStream parent) {
        this.parent = parent;
        this.project = parent.getProject();
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        if (!ServerUtils.equalsEdsObject(this.project, project)) {
            addChange(CustomFormConstants.PROJECT_);
        }
        this.project = project;
    }

    public EdsWorkStream getParentWS() {
        return parent;
    }

    public void setParentWS(EdsWorkStream parent) {
        this.parent = parent;
    }

    public Set<EdsTask> getPredecessors() {
        if (predecessors == null) {
            predecessors = new HashSet<>();
        }
        return predecessors;
    }

    public void setPredecessors(Set<EdsTask> predecessors) {
        this.predecessors = predecessors;
    }

    public Set<EdsTask> getSuccessors() {
        if (predecessors == null) {
            predecessors = new HashSet<>();
        }
        return successors;
    }

    public EdsWorkStream getParent() {
        return parent;
    }

    public void setParent(EdsWorkStream parent) {
        this.parent = parent;
    }

    public Boolean getBillable() {
        return billable != null ? billable : true;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public void setSuccessors(Set<EdsTask> successors) {
        this.successors = successors;
    }

    public Set<EdsEmployeeTask> getAssignments() {
        return assignments;
    }

    public Integer getTaskGanttOrder() {
        return taskGanttOrder;
    }

    public void setTaskGanttOrder(Integer taskGanttOrder) {
        this.taskGanttOrder = taskGanttOrder;
    }

    public Set<EdsEmployeeTask> getUnDeletedAssignments() {
        Set<EdsEmployeeTask> eTasks = new HashSet<>();
        for (EdsEmployeeTask eTask : getAssignments()) {
            if ((eTask.getDeleted() == null || !eTask.getDeleted()) && eTask.getProjectEmployee() != null && eTask.getProjectEmployee().getEmployeeDepartment() != null) {
                EdsEmployee employee = eTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                if (employee != null && !employee.getDeleted()) {
                    eTasks.add(eTask);
                }
            }
        }
        return eTasks;
    }

    public Float getTaskAveragePercentCompleted() {
        Float average = 0f;
        int count = 0;
        for (EdsEmployeeTask et : getUnDeletedAssignments()) {
            count++;
            average += et.getPercent() != null ? et.getPercent() : 0;
        }
        return count > 0 ? (average / count) : 0;
    }

    public Float getTaskAveragePercentCompletedNewLogic() {
        Float estimatedtime = 0f;
        Float timespent = 0f;
        for (EdsEmployeeTask et : getUnDeletedAssignments()) {
            estimatedtime += et.getEstimatedTime() != null ? et.getEstimatedTime() : 0;
            timespent += et.getTimeSpent() != null ? et.getTimeSpent() : 0;
        }
        return estimatedtime != 0 && estimatedtime != null ? timespent * 100 / estimatedtime : 0;//
    }

    public String getTaskLastStatus(EdsReference taskStatus, List<EdsReference> assigneeStatuses) {
        String TASK_STATUS;
        // should be prime numbers
        int NOT_STARTED = 2;
        int IN_PROGRESS = 3;
        int CLOSED = 5;
        int WAITING = 7;
        int CANCELLED = 13;
        int COMPLETED = 19;

        int RESULT = 1;
        int ACCUMULATOR = 1;
        int BALLAST = 1;
        if (taskStatus != null) {
            TASK_STATUS = taskStatus.getCode();
            if (!taskStatus.isSystemReference()) {
                return taskStatus.getCode();
            }
        } else {
            TASK_STATUS = EdsTask.NOT_STARTED;
        }

        for (EdsReference et : assigneeStatuses) {

            switch (et.getCode()) {
                case EdsTask.COMPLETED -> RESULT *= COMPLETED;
                case EdsTask.IN_PROGRESS -> RESULT *= IN_PROGRESS;
                case EdsTask.CLOSED -> RESULT *= CLOSED;
                case EdsTask.WAITING_FOR_SOMEONE_ELSE -> RESULT *= WAITING;
                case EdsTask.CANCELLED -> RESULT *= CANCELLED;
                default -> RESULT *= NOT_STARTED;
            }
            BALLAST = ACCUMULATOR / RESULT;
            if (ACCUMULATOR != BALLAST * RESULT) {
                ACCUMULATOR *= RESULT;
            }
            RESULT = 1;
            BALLAST = 1;

        }
        BALLAST = 1;
        boolean go = true;
        /////Chekcing for status according to ACCUMULATOR value
        //-------- NOT STARTED ---------     if all assignee statuses are NOT_STARTED then overal status is also NOT_STARTED
        BALLAST = ACCUMULATOR / NOT_STARTED;
        if ((BALLAST == 1) && ((BALLAST * NOT_STARTED) == ACCUMULATOR)) {
            TASK_STATUS = EdsTask.NOT_STARTED;
            go = false;
        }
        //-------CLOSED-------------- if all assignee statuses are CLOSED overal status is also CLOSED
        if (go) {
            BALLAST = ACCUMULATOR / CLOSED;
//            BALLAST = ACCUMULATOR / COMPLETED;
//            boolean anyCompleted = ACCUMULATOR == BALLAST * COMPLETED;
//            BALLAST = ACCUMULATOR / CLOSED;
//            boolean anyClosed = ACCUMULATOR == BALLAST * CLOSED;
            if (ACCUMULATOR == BALLAST * CLOSED && BALLAST == 1/* || anyClosed || anyCompleted*/) {
                TASK_STATUS = EdsTask.CLOSED;
                go = false;
            }
        }
        //------- COMPLETED --------  if all assignee statuses are COMPLETED then overal status is also COMPLETED
        if (go) {
            BALLAST = ACCUMULATOR / COMPLETED;
            if ((BALLAST == 1 && ACCUMULATOR == BALLAST * COMPLETED)/* || (BALLAST / CLOSED) == 1 && ((BALLAST / CLOSED) * CLOSED) == ACCUMULATOR*/) {
                TASK_STATUS = EdsTask.COMPLETED;
                go = false;
            }
        }
        //-------- CANCELLED --------- if all assignee statuses are CANCELLED then overal statuses is also CANCELLED
        if (go) {
            BALLAST = ACCUMULATOR / CANCELLED;
            if ((BALLAST == 1) && ((BALLAST * CANCELLED) == ACCUMULATOR)) {
                TASK_STATUS = EdsTask.CANCELLED;
                go = false;
            }
        }

        //-------WAITING FOR SOMEONE ELSE-------- even if one of assignee statusess is WAITING then overal status is also WAITING
        if (go) {
            BALLAST = ACCUMULATOR / WAITING;
            if (ACCUMULATOR == BALLAST * WAITING) {
                TASK_STATUS = EdsTask.WAITING_FOR_SOMEONE_ELSE;
                go = false;
            }
        }
        //------- IN PROGRESS-------- in other cases that given above, or when all assignee statuses are IN_PROGRESS then overal status is IN_PROGRESS
        if (go) {
            BALLAST = ACCUMULATOR / COMPLETED;
            boolean anyCompleted = ACCUMULATOR == BALLAST * COMPLETED;
            BALLAST = ACCUMULATOR / CLOSED;
            boolean anyClosed = ACCUMULATOR == BALLAST * CLOSED;
            if ((ACCUMULATOR / IN_PROGRESS == 1 || (ACCUMULATOR / IN_PROGRESS) * IN_PROGRESS == ACCUMULATOR) || (anyCompleted) || (anyClosed)) {
                TASK_STATUS = EdsTask.IN_PROGRESS;
            }
        }
        return TASK_STATUS;
    }

    public String getTaskLastStatus() {
        String TASK_STATUS;
        // should be prime numbers
        int NOT_STARTED = 2;
        int IN_PROGRESS = 3;
        int CLOSED = 5;
        int WAITING = 7;
        int CANCELLED = 13;
        int COMPLETED = 19;

        int RESULT = 1;
        int ACCUMULATOR = 1;
        int BALLAST = 1;

        if (getStatus() != null) {
            TASK_STATUS = getStatus().getCode();
        } else {
            TASK_STATUS = EdsTask.NOT_STARTED;
        }

        for (EdsEmployeeTask et : getUnDeletedAssignments()) {

            switch (et.getStatus().getCode()) {
                case EdsTask.COMPLETED -> RESULT *= COMPLETED;
                case EdsTask.IN_PROGRESS -> RESULT *= IN_PROGRESS;
                case EdsTask.CLOSED -> RESULT *= CLOSED;
                case EdsTask.WAITING_FOR_SOMEONE_ELSE -> RESULT *= WAITING;
                case EdsTask.CANCELLED -> RESULT *= CANCELLED;
                default -> RESULT *= NOT_STARTED;
            }
            BALLAST = ACCUMULATOR / RESULT;
            if (ACCUMULATOR != BALLAST * RESULT) {
                ACCUMULATOR *= RESULT;
            }
            RESULT = 1;
            BALLAST = 1;

        }

        boolean go = true;
        /////Chekcing for status according to ACCUMULATOR value
        //-------- NOT STARTED ---------     if all assignee statuses are NOT_STARTED then overal status is also NOT_STARTED
        BALLAST = ACCUMULATOR / NOT_STARTED;
        if ((BALLAST == 1) && ((BALLAST * NOT_STARTED) == ACCUMULATOR)) {
//            TASK_STATUS = EdsTask.NOT_STARTED;
            go = false;
        }
        //-------CLOSED-------------- if all assignee statuses are CLOSED overal status is also CLOSED
        if (go) {
            BALLAST = ACCUMULATOR / CLOSED;
//            BALLAST = ACCUMULATOR / COMPLETED;
//            boolean anyCompleted = ACCUMULATOR == BALLAST * COMPLETED;
//            BALLAST = ACCUMULATOR / CLOSED;
//            boolean anyClosed = ACCUMULATOR == BALLAST * CLOSED;
            if (ACCUMULATOR == BALLAST * CLOSED && BALLAST == 1/* || anyClosed || anyCompleted*/) {
                TASK_STATUS = EdsTask.CLOSED;
                go = false;
            }
        }
        //------- COMPLETED --------  if all assignee statuses are COMPLETED then overal status is also COMPLETED
        if (go) {
            BALLAST = ACCUMULATOR / COMPLETED;
            if ((BALLAST == 1 && ACCUMULATOR == BALLAST * COMPLETED)/* || (BALLAST / CLOSED) == 1 && ((BALLAST / CLOSED) * CLOSED) == ACCUMULATOR*/) {
                TASK_STATUS = EdsTask.COMPLETED;
                go = false;
            }
        }
        //-------- CANCELLED --------- if all assignee statuses are CANCELLED then overal statuses is also CANCELLED
        if (go) {
            BALLAST = ACCUMULATOR / CANCELLED;
            if ((BALLAST == 1) && ((BALLAST * CANCELLED) == ACCUMULATOR)) {
                TASK_STATUS = EdsTask.CANCELLED;
                go = false;
            }
        }

        //-------WAITING FOR SOMEONE ELSE-------- even if one of assignee statusess is WAITING then overal status is also WAITING
        if (go) {
            BALLAST = ACCUMULATOR / WAITING;
            if (ACCUMULATOR == BALLAST * WAITING) {
                TASK_STATUS = EdsTask.WAITING_FOR_SOMEONE_ELSE;
                go = false;
            }
        }
        //------- IN PROGRESS-------- in other cases that given above, or when all assignee statuses are IN_PROGRESS then overal status is IN_PROGRESS
        if (go) {
            BALLAST = ACCUMULATOR / COMPLETED;
            boolean anyCompleted = ACCUMULATOR == BALLAST * COMPLETED;
            BALLAST = ACCUMULATOR / CLOSED;
            boolean anyClosed = ACCUMULATOR == BALLAST * CLOSED;
            if ((ACCUMULATOR / IN_PROGRESS == 1 || (ACCUMULATOR / IN_PROGRESS) * IN_PROGRESS == ACCUMULATOR) || (anyCompleted) || (anyClosed)) {
                TASK_STATUS = EdsTask.IN_PROGRESS;
            }
        }
        if (TASK_STATUS == null || "".equals(TASK_STATUS)) {
            TASK_STATUS = EdsTask.NOT_STARTED;
        }
        return TASK_STATUS;
    }

    public Set<EdsProjectEmployee> getUnDeletedProjectEmployees() {
        Set<EdsProjectEmployee> eTasks = new HashSet<>();
        for (EdsEmployeeTask eTask : getAssignments()) {
            if (eTask.getDeleted() == null || !eTask.getDeleted()) {
                eTasks.add(eTask.getProjectEmployee());
                eTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID();
            }
        }
        return eTasks;
    }

    public Set<Integer> getUndeletedProjectEmployeesIds() {
        Set<Integer> eTasks = new HashSet<>();
        for (EdsEmployeeTask eTask : getAssignments()) {
            if (eTask.getDeleted() == null || !eTask.getDeleted() && !eTask.getProjectEmployee().getDeleted()) {
                eTasks.add(eTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID());
            }
        }
        return eTasks;
    }

    public Set<EdsEmployee> getUndeletedProjectEmployeesForTaskSMS() {
        Set<EdsEmployee> employees = new HashSet<>();
        for (EdsEmployeeTask eTask : getAssignments()) {
            EdsProjectEmployee projectEmployee = eTask.getProjectEmployee();
            if (eTask.getDeleted() == null || !eTask.getDeleted() && !projectEmployee.getDeleted()) {
                EdsEmployeeDepartment employeeDepartment = projectEmployee.getEmployeeDepartment();
                if (employeeDepartment != null) {
                    employees.add(employeeDepartment.getEmployee());
                }
            }
        }
        return employees;
    }


    public void setAssignments(Set<EdsEmployeeTask> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(EdsEmployeeTask assignment) {
        if (!this.equals(assignment.getTask())) {
            assignment.setTask(this);
        }
        getAssignments().add(assignment);
    }

    public void addAssignment(EdsProjectEmployee projectEmployee) {
        EdsEmployeeTask assignment = new EdsEmployeeTask(this, projectEmployee);
        getAssignments().add(assignment);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null && !ServerUtils.equalsString(this.name, name)) {
            addHistoryChange("Name", this.name, name);
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (this.description != null && !ServerUtils.equalsString(this.description, description)) {
            addHistoryChange("Description", this.description, description);
        }
        this.description = description;
    }

    public EdsReference getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(EdsReference difficulty) {
        this.difficulty = difficulty;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartAndDueDates(Date startDate, Date dueDate) {
        Date oldStartDate = this.startDate;
        Date oldDueDate = this.dueDate;
        this.startDate = startDate;
        this.dueDate = dueDate;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_START_DATE, oldStartDate, this.startDate);
            propertyChangeSupport.firePropertyChange(FIELD_END_DATE, oldDueDate, this.dueDate);
        }


    }

    public void setStartDate(Date startDate) {
        if (this.startDate != null && !ServerUtils.equalsDate(this.startDate, startDate)) {
            addHistoryChange("Start Date", this.startDate, startDate);
        }
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        if (this.dueDate != null && !ServerUtils.equalsDate(this.dueDate, dueDate)) {
            addHistoryChange("Due Date", this.dueDate, dueDate);
        }
        this.dueDate = dueDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.STATUS);
        }
        EdsReference oldValue = this.status;
        this.status = status;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_STATUS, oldValue, this.status);
        }
    }


    public EdsReference getPriority() {
        return priority;
    }

    public void setPriority(EdsReference priority) {
        if (this.priority != null && !ServerUtils.equalsReference(this.priority, priority)) {
            addHistoryChange("Priority", this.priority, priority);

        }
        this.priority = priority;
    }

    public void addPredecessor(EdsTask task) {
        getPredecessors().add(task);
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Float getPercent() {
        return percent != null ? new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : 0f;
    }

    public void setPercent(Float percent) {
        if (percent != null) {
            BigDecimal percentDecimal = new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
            this.percent = percentDecimal.floatValue();
        } else {
            this.percent = percent;
        }
    }

    public Float getPreviousPercent() {
        return previousPercent;
    }

    public void setPreviousPercent(Float percent) {
        if (percent != null) {
            BigDecimal percentDecimal = new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
            this.previousPercent = percentDecimal.floatValue();
        } else {
            this.previousPercent = percent;
        }
    }

    public Integer getTimespent() {
        return timespent != null ? timespent : 0;
    }

    public void setTimespent(Integer timespent) {
        this.timespent = timespent;
    }

    public Boolean getRated() {
        return rated;
    }

    public void setRated(Boolean rated) {
        this.rated = rated;
    }

    public String getTimespentHM() {
        if (timespent == null) {
            return timespentHM = "00:00";
        }
        timespentHM = "";
        if (timespent / 60 < 9) {
            timespentHM = "0";
        }
        timespentHM = timespentHM + timespent / 60;
        timespentHM = timespentHM + ":";
        if (timespent % 60 < 9) {
            timespentHM = timespentHM + "0";
        }
        timespentHM = timespentHM + timespent % 60;
        return timespentHM;
    }

    public void setTimespentHM(String timespentHM) {
        this.timespentHM = timespentHM;
    }

    public Boolean getOnCriticalPath() {
        return onCriticalPath;
    }

    public void setOnCriticalPath(Boolean onCriticalPath) {
        this.onCriticalPath = onCriticalPath;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean delete) {
        this.deleted = delete;
    }

    public boolean isAssigned(EdsProjectEmployee projectEmployee) {
        return getUnDeletedAssignments().contains(
                new EdsEmployeeTask(this, projectEmployee));
    }

    public String getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(String deletedby) {
        this.deletedby = deletedby;
    }

    public TaskListItem createTaskListItem() {

        TaskListItem result = new TaskListItem();
        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setGoogleID(getGoogleID());
        result.setCreationDate(getCreationTime());
        if (getWorkflowID() != null) {
            result.setWorkflowItem(isWorkflowItem());
            result.setWorkflowID(getWorkflowID());
            result.setWorkflowStartDate(getWorkflowStartDate());
            result.setWorkflowDueDate(getWorkflowDueDate());
            result.setWorkflowDueDateGranularity(getWorkflowDueDateGranularity());
            if (isWorkflowActionTimeBased()) {
                result.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
                result.setWorkflowActionStartTime(getWorkflowActionStartTime());
                result.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
                result.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
            }
        }
        if (getProject() != null) {
            result.setClient(getProject().getClient() != null ? getProject().getClient().getName() : "");
        } else {
            result.setClient("");
        }
        result.setProjectId(getProject() != null ? getProject().getObjectID()
                : null);

        if (getType() != null) {
            result.setTypeId(getType().getObjectID());
            result.setTypeCode(getType().getCode());
            result.setTypeName(getType().getName());
        } else {
            result.setTypeCode("N/A");
            result.setTypeName("N/A");
        }

        result.setPriorityName(getPriority() != null ? getPriority().getName()
                : "N/A");
        result.setPriorityCode(getPriority() != null ? getPriority().getCode() : null);
        result.setStatusName(getStatus() != null ? getStatus().getName()
                : "N/A");
        if (getStatus() != null) {
            result.setTaskStatusId(getStatus().getObjectID());
            result.setStatusCode(getStatus().getCode());
        }
        result.setStartDate(getStartDate() != null ? new Date(getStartDate()
                .getTime()) : null);
        result.setEndDate(getDueDate() != null ? new Date(getDueDate()
                .getTime()) : null);
        result.setLastModified(getLastUpdateTime() != null ? new Date(
                getLastUpdateTime().getTime()) : null);
        result.setLastModifiedBy(getUpdater() != null ? getUpdater()
                .getFirstName()
                + " " + getUpdater().getLastName() : "N/A");
        Set<EdsEmployeeTask> assignes = getUnDeletedAssignments();
        if (assignes != null) {
            StringBuilder assignee = new StringBuilder();
            for (EdsEmployeeTask emp : assignes) {
                if (emp.getStatus() != null
                        && emp.getStatus().getCode().equals(EdsTask.CANCELLED)) {
                    continue;
                }
                if (emp.getProjectEmployee() != null) {
                    if (emp.getProjectEmployee().getEmployeeDepartment() != null) {
                        assignee.append(emp.getProjectEmployee()
                                .getEmployeeDepartment().getEmployee()
                                .getName());
                        assignee.append(",");
                    }
                }

            }
            if (assignee.lastIndexOf(",") != -1) {
                assignee.deleteCharAt(assignee.lastIndexOf(","));
            }
            result.setAssignedTo(assignee.toString());
        } else {
            result.setAssignedTo("N/A");
        }
        result.setComplete(getPercent() != null ? getPercent().toString() : "0.0");
        result.setActualHoursSpent(getTimespentHM() != null ? getTimespentHM() : "00:00");
        result.setDueDate(getDueDate() != null ? new Date(getDueDate().getTime()) : null);
        return result;
    }

    public TaskSelectItem createTaskSelectItem() {
        TaskSelectItem result = new TaskSelectItem();
        result.setId(getObjectID());
        result.setTaskNumber(getNumber());
        result.setName(getName());
        result.setTaskStartDate(getStartDate());
        result.setTaskDueDate(getDueDate());
        result.setAllDay(isAllDay());
        result.setProjectId(getProject().getObjectID());
        return result;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        if (estimatedTime != null && estimatedTime > getEstimatedTime()) {
            setSentActualTimeReachedNotifation(false);
        }
        this.estimatedTime = estimatedTime;
    }

    public Integer getEstimatedTime() {
        return estimatedTime != null ? estimatedTime : Integer.valueOf(0);
    }

    public Boolean hasAccess(EdsUser user) {
        return user.getCompany().getObjectID().equals(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
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

    public Set<EdsPositionTask> getPositions() {
        return positions;
    }

    public void setPositions(Set<EdsPositionTask> positions) {
        this.positions = positions;
    }

    public Integer getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Integer reminderTime) {
        this.reminderTime = reminderTime;
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

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public EdsTaskCustomFields getTaskCustomFields() {
        return taskCustomFields;
    }

    public void setTaskCustomFields(EdsTaskCustomFields taskCustomFields) {
        this.taskCustomFields = taskCustomFields;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.NUMBER);
        }
        this.number = number;
    }

    public String getSavedNumberFormula() {
        return savedNumberFormula;
    }

    public void setSavedNumberFormula(String savedNumberFormula) {
        this.savedNumberFormula = savedNumberFormula;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Integer getTodoListOrder() {
        return todoListOrder;
    }

    public void setTodoListOrder(Integer todoListOrder) {
        this.todoListOrder = todoListOrder;
    }

    public boolean isAllDayAppointment() {
        long difference = dueDate.getTime() - startDate.getTime();
        long diff = MILLIS_IN_A_DAY - 1000;
        boolean multiDay = (difference + 1000) / MILLIS_IN_A_DAY > 0 && (difference + 1000) % MILLIS_IN_A_DAY == 0;
        return difference == diff || difference == MILLIS_IN_A_DAY || multiDay;
    }

    public boolean isMultiDayAppointment() {
        long difference = dueDate.getTime() - startDate.getTime();
        return difference > MILLIS_IN_A_DAY;
    }

    @Override
    public String toString() {
        return getObjectID() + " " + getName() + " " + getDescription();
    }

    public Boolean isChangedCalculationFields() {
        return changedCalculationFields != null ? changedCalculationFields : true;
    }

    public void setChangedCalculationFields(Boolean changedCalculationFields) {
        this.changedCalculationFields = changedCalculationFields;
    }

    public Boolean isCalculated() {
        return calculated != null ? calculated : false;
    }

    public void setCalculated(Boolean calculated) {
        this.calculated = calculated;
    }

    public Double getPlannedWageAmount() {
        return plannedWageAmount != null ? plannedWageAmount : 0.0;
    }

    public void setPlannedWageAmount(Double plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public Double getPlannedClientChargeAmount() {
        return plannedClientChargeAmount != null ? plannedClientChargeAmount : 0.0;
    }

    public void setPlannedClientChargeAmount(Double plannedClientChargeAmount) {
        this.plannedClientChargeAmount = plannedClientChargeAmount;
    }

    public Double getActualWageAmount() {
        return actualWageAmount != null ? actualWageAmount : 0.0;
    }

    public void setActualWageAmount(Double actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public Double getActualClientChargeAmount() {
        return actualClientChargeAmount != null ? actualClientChargeAmount : 0.0;
    }

    public void setActualClientChargeAmount(Double actualClientChargeAmount) {
        this.actualClientChargeAmount = actualClientChargeAmount;
    }

    public Double getRemainingWageAmount() {
        return remainingWageAmount != null ? remainingWageAmount : 0.0;
    }

    public void setRemainingWageAmount(Double remainingWageAmount) {
        this.remainingWageAmount = remainingWageAmount;
    }

    public Double getRemainingClientChargeAmount() {
        return remainingClientChargeAmount != null ? remainingClientChargeAmount : 0.0;
    }

    public void setRemainingClientChargeAmount(Double remainingClientChargeAmount) {
        this.remainingClientChargeAmount = remainingClientChargeAmount;
    }

    public List<EdsTaskEstimateTimeSpentHistory> getEstimateTimeSpentHistoryList() {
        return estimateTimeSpentHistoryList;
    }

    public void setEstimateTimeSpentHistoryList(List<EdsTaskEstimateTimeSpentHistory> estimateTimeSpentHistoryList) {
        this.estimateTimeSpentHistoryList = estimateTimeSpentHistoryList;
    }

    public Boolean getShowInTimesheet() {
        return showInTimesheet;
    }

    public void setShowInTimesheet(Boolean showInTimesheet) {
        this.showInTimesheet = showInTimesheet;
    }

    public boolean isNewItem() {
        return isNewItem;
    }

    public void setNewItem(boolean newItem) {
        isNewItem = newItem;
    }

    public String getVisibility() {
        return visibility != null ? visibility : TodoListItem.PUBLIC;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility != null ? visibility : TodoListItem.PUBLIC;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset != null ? timeZoneOffset : 0;
    }

    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public EdsUser getToDoListAssignee() {
        return toDoListAssignee;
    }

    public void setToDoListAssignee(EdsUser toDoListAssignee) {
        this.toDoListAssignee = toDoListAssignee;
    }

    public BigDecimal getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(BigDecimal taskAmount) {
        this.taskAmount = taskAmount;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public List<EdsTaskStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<EdsTaskStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public Set<EdsTaskChanges> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(Set<EdsTaskChanges> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public Collection<SolrInputDocument> indexToSolr(List<EdsTaskRbac> taskRbacEntries, List<String> assigneeUserList, Integer companyId, List<EdsRelation> edsRelationList) {
        Map<String, SolrInputDocument> solrDocMap = new HashMap<>();
        Map<String, Integer> ranks = new HashMap<>();
        List<EdsTaskPermission> permissions = new ArrayList<>();
        List<String> permStrings = new ArrayList<>();

        for (EdsTaskRbac tRbac : taskRbacEntries) {
            permissions.add(tRbac.getTaskPermission());
        }

        if (!permissions.isEmpty()) {
            EdsTaskPermission permission = permissions.get(0);
            TaskPermissionItem tPermission = permission.getAsTaskPermissionItem();
            tPermission = permission.getMergedPermissions(tPermission, permissions);
            permStrings = tPermission.getPemissionAsStringList();
        }

        for (EdsTaskRbac tRbac : taskRbacEntries) { //TODO taskRbacEntries 10 ta bo'lsa 10 martta bir xil ishni qayta qayta qilishi mn, need to optimize
            String compositID = "";
            String trusteeType = tRbac.getTrusteeType() + "";

            if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType())) {
                compositID = companyId + "_" + getObjectID() + "_" + tRbac.getUser().getObjectID() + "_" + tRbac.getTrusteeType() + "_" + tRbac.getRelationship();
            } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
                compositID = companyId + "_" + getObjectID() + "_" + tRbac.getGroup().getObjectID() + "_" + tRbac.getTrusteeType();
            }

            SolrInputDocument doc = solrDocMap.get(compositID);
            Integer currentRelationRank;
            if (doc == null) {
                currentRelationRank = tRbac.getRelationRank();
                ranks.put(compositID, currentRelationRank);
                doc = new SolrInputDocument();
                solrDocMap.put(compositID, doc);

                doc.addField(SolrTaskRepresenter.FIELD_COMPOSITE_ID, compositID);
                doc.addField(SolrTaskRepresenter.FIELD_COMPANY_ID, companyId);

                doc.setField(SolrTaskRepresenter.FIELD_RANK, currentRelationRank);

                doc.addField(SolrTaskRepresenter.FIELD_TASK_ID, getObjectID());
                doc.addField(SolrTaskRepresenter.FIELD_TASK_NUMBER, getNumber());
                doc.addField(SolrTaskRepresenter.FIELD_TASK_NAME, getName());
                doc.addField(SolrTaskRepresenter.FIELD_TASK_DESCRIPTION, getDescription());

                if (getProject() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME, getProject().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_NUMBER, getProject().getNumber());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID, getProject().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME, getProject().getObjectID() + SolrTaskRepresenter.SPLIT + getProject().getName());

                    if (getProject().getClients() != null && !getProject().getClients().isEmpty()) {
                        for (EdsCrmAccount client : getProject().getClients()) {
                            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_ID, client.getObjectID());
                            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_NAME, client.getName());
                            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_ID_NAME, client.getObjectID()
                                    + SolrTaskRepresenter.SPLIT + client.getName());
                        }
                    }
                }

                if (getParentWS() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_NAME, getParentWS().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID, getParentWS().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID_NAME, getParentWS().getObjectID() + SolrTaskRepresenter.SPLIT + getParentWS().getName());
                }

                if (tRbac.getClient() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME, tRbac.getClient().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID, tRbac.getClient().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME, tRbac.getClient().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getClient().getName());
                }

                if (tRbac.getDepartment() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_NAME, tRbac.getDepartment().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_ID, tRbac.getDepartment().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_ID_NAME, tRbac.getDepartment().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getDepartment().getName());
                }

                if (getCreator() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_CREATOR_ID, getCreator().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_CREATOR, getCreator().getName());
                }

                if (getUpdater() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_LAST_MODIFIED_BY, getUpdater().getName());
                }

                if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType()) && !tRbac.getUser().getDeleted()) {
                    if (!EdsRelationship.TASK_NOT_ASSIGNEE.equals(tRbac.getRelationship())) {
                        doc.addField(SolrTaskRepresenter.FIELD_USER_ID, tRbac.getUser().getObjectID());
                        EdsEmployee edsEmployee = tRbac.getUser().getEmployee();
                        String employeeNumber = edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null ? edsEmployee.getProfile().getEmployeeCode() + " " : "";
                        doc.addField(SolrTaskRepresenter.FIELD_USER_ID_NAME, tRbac.getUser().getObjectID() + SolrTaskRepresenter.SPLIT + employeeNumber + tRbac.getUser().getName());
                    }

                    for (EdsTaskRbac viewers : taskRbacEntries) {
                        if (EdsTrusteeType.USER.equals(viewers.getTrusteeType())) {
                            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, viewers.getUser().getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_USER_ID);
                        } else if (EdsTrusteeType.GROUP.equals(viewers.getTrusteeType())) {
                            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, viewers.getGroup().getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_GROUP_ID);
                        }
                    }
                } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
                    doc.addField(SolrTaskRepresenter.FIELD_GROUP_ID, tRbac.getGroup().getObjectID());
                    for (EdsTaskRbac viewers : taskRbacEntries) {
                        if (EdsTrusteeType.USER.equals(viewers.getTrusteeType())) {
                            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, viewers.getUser().getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_USER_ID);
                        } else if (EdsTrusteeType.GROUP.equals(viewers.getTrusteeType())) {
                            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, viewers.getGroup().getObjectID() + SolrTaskRepresenter.SPLIT + SolrTaskRepresenter.FIELD_GROUP_ID);
                        }
                    }
                }

                for (String userName : assigneeUserList) {
                    doc.addField(SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES, userName);
                }
                if ((EdsRelationship.TASK_ASSIGNEE.equals(tRbac.getRelationship()) || EdsRelationship.TASK_NOT_ASSIGNEE.equals(tRbac.getRelationship()))
                        && tRbac.getUser() != null && !tRbac.getUser().getDeleted()) {
                    doc.addField(SolrTaskRepresenter.FIELD_ASSIGNEE_ID, tRbac.getUser().getObjectID());
                    if (tRbac.getStatus() != null) {
                        doc.addField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS, tRbac.getStatus().getName());
                        doc.addField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID, tRbac.getStatus().getObjectID());
                        doc.addField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_CODE, tRbac.getStatus().getCode());
                        doc.addField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE, tRbac.getStatus().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getStatus().getCode());
                        doc.addField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME, tRbac.getStatus().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getStatus().getCode() + SolrTaskRepresenter.SPLIT + tRbac.getStatus().getName());
                    }
                }
                //Overall status. This status is shown to admin group. This status represents task overall status!
                if (getStatus() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS, getStatus().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID, getStatus().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_CODE, getStatus().getCode());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE, getStatus().getObjectID() + SolrTaskRepresenter.SPLIT + getStatus().getCode());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME, getStatus().getObjectID() + SolrTaskRepresenter.SPLIT + getStatus().getCode() + SolrTaskRepresenter.SPLIT + getStatus().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_SORDER, getStatus().getSorder());
                }
                if (getProject() != null && getProject().getManager() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_NAME, getProject().getManager().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID, getProject().getManager().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID_NAME, getProject().getManager().getObjectID() + SolrTaskRepresenter.SPLIT
                            + getProject().getManager().getName());
                }
                if (getPredecessors() != null && !getPredecessors().isEmpty()) {
                    String predTaskStatus = EdsTask.COMPLETED;

                    for (EdsTask predTask : getPredecessors()) {
                        String lastStatus = predTask.getTaskLastStatus();

                        if (EdsTask.IN_PROGRESS.equals(lastStatus)) {
                            predTaskStatus = lastStatus;
                            break;
                        }
                        if (!EdsTask.COMPLETED.equals(lastStatus)) {
                            predTaskStatus = lastStatus;
                        }
                    }
                    doc.addField(SolrTaskRepresenter.FIELD_PREDECESSOR_TASK_STATUS, predTaskStatus);
                }

                doc.addField(SolrTaskRepresenter.FIELD_TRUSTEE_TYPE, trusteeType);

                doc.addField(SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED, getPercent());
                doc.addField(SolrTaskRepresenter.FIELD_ESTIMATED_TIME, tRbac.getEstimatedTime());

                if (getTaskAmount() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_AMOUNT, getTaskAmount().doubleValue());
                }

                doc.addField(SolrTaskRepresenter.FIELD_DUE_DATE, getDueDate());
                doc.addField(SolrTaskRepresenter.FIELD_START_DATE, getStartDate());
                doc.addField(SolrTaskRepresenter.FIELD_ACTUAL_START_DATE, getActualStartDate());
                doc.addField(SolrTaskRepresenter.FIELD_END_DATE, getActualEndDate());
                doc.addField(SolrTaskRepresenter.FIELD_CREATION_DATE, getCreationTime());
                doc.addField(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, getLastUpdateTime());
                doc.addField(SolrTaskRepresenter.KANBAN_ORDER, getKanbanOrder());

                if (getType() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_TYPE_ID, getType().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_TYPE_CODE, getType().getCode());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_TYPE_ID_CODE_NAME, getType().getObjectID() + SolrTaskRepresenter.SPLIT + getType().getCode() + SolrTaskRepresenter.SPLIT + getType().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_TYPE, getType().getName());
                }

                if (getPriority() != null) {
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY, getPriority().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID, getPriority().getObjectID());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_CODE, getPriority().getCode());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID_CODE, getPriority().getObjectID() + SolrTaskRepresenter.SPLIT + getPriority().getCode());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID_CODE_NAME, getPriority().getObjectID() + SolrTaskRepresenter.SPLIT + getPriority().getCode() + SolrTaskRepresenter.SPLIT + getPriority().getName());
                    doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_SORDER, getPriority().getSorder());
                }

                CustomFieldsUtils.setInSolrCustomFields(doc, getTaskCustomFields());

            } else {
                currentRelationRank = ranks.get(compositID);
            }

            if (currentRelationRank < tRbac.getRelationRank()) {  // highest rank is assigned to assignee then to creator then to PM and so on according to relationship
                // we should write exact status of task where he is assignee
                doc.setField(SolrTaskRepresenter.FIELD_RANK, currentRelationRank);
                doc.setField(SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED, tRbac.getPercent() != null ? tRbac.getPercent() : getPercent());
                doc.setField(SolrTaskRepresenter.FIELD_ESTIMATED_TIME, tRbac.getEstimatedTime());
            }
            SolrRelationUtils.addToSolrRelations(doc, edsRelationList, EdsRelation.TYPE_TASK);
            for (String perm : permStrings) {
                doc.addField(SolrTaskRepresenter.FIELD_PERMISSIONS, perm);
            }
            doc.addField(SolrTaskRepresenter.FIELD_RELATIONSHIPS, tRbac.getRelationship());
        }
        return solrDocMap.values();
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.NUMBER)) {
                setNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.NAME)) {
                setName((String) value);
            } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
                setDescription((String) value);
            } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
                setStartDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
                setDueDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.PROJECT_)) {
                setProject((EdsProject) value);
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.PRIORITY)) {
                setPriority((EdsReference) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getTaskCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getTaskCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }
    public String getDuration(){
        if (getDueDate() == null) {
            return "N/A";
        }  else if ("COMPLETED".equalsIgnoreCase(getStatus().getCode())) {
            return  "0";
        }
        long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
        long diffMillies = getDueDate().getTime() - System.currentTimeMillis();
        long diffDays = Math.round((double) diffMillies / MILLIS_PER_DAY);
        return String.valueOf(diffDays);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            return isAllDay() ? ServerUtils.shortDateFormat(getStartDate(), user, true) : ServerUtils.longDateFormat(getStartDate(), user, true);
        } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            return isAllDay() ? ServerUtils.shortDateFormat(getDueDate(), user, true) : ServerUtils.longDateFormat(getDueDate(), user, true);
        } else if (fieldID.equals(CustomFormConstants.DURATION)) {
            return getDuration();
        } else if (fieldID.equals(CustomFormConstants.PROJECT_)) {
            return getProject();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.PRIORITY)) {
            return getPriority();
        } else if (fieldID.equals(CustomFormConstants.ASSIGNEE)) {
            if (getAssignments() != null && getAssignments().size() > 0) {
                String assignee = "";
                boolean isFirst = true;
                StringBuilder assigneeBuilder = new StringBuilder(assignee);
                for (EdsEmployeeTask employeeTask : getAssignments()) {
                    assigneeBuilder.append(isFirst ? "" : ", ").append(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getProfile().getEmployeeCode()).append(" - ").append(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName());
                    isFirst = false;
                }
                assignee = assigneeBuilder.toString();
                return assignee;
            }
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getTaskCustomFields() != null ? CustomFieldsUtils.getObjectValue(getTaskCustomFields(), fieldID) : "";
        } else if (fieldID.equals(CustomFormConstants.TASK.TYPE)) {
            return getType() != null ? getType().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.TASK.UPDATED_BY)) {
            return getCreator().getFullName();
        } else if (fieldID.equals(CustomFormConstants.TASK.ACTUAL_END_DATE)) {
            return getActualEndDate();
        } else if (fieldID.equals(CustomFormConstants.TASK.ACTUAL_START_DATE)) {
            return getActualStartDate();
        } else if (fieldID.equals(CustomFormConstants.TASK.TIME_SPENT)) {
            return getTimespent();
        } else if (fieldID.equals(CustomFormConstants.TASK.CREATED_BY)) {
            return getCreator().getFullName();
        }
        return super.getRealValue(fieldID);
    }

    public Map<String, Object> getProjectCustomFieldValueAsMap(Set<String> fieldIDs) {
        Map<String, Object> result = Maps.newHashMap();
        if (fieldIDs != null && fieldIDs.size() > 0) {
            for (String fieldID : fieldIDs) {
                String realFieldID = fieldID;
                fieldID = "${project_" + fieldID.toLowerCase() + "}";
                result.put(fieldID, this.getProject().getStringValueByFieldID(realFieldID));
            }
        }
        return result;
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            EdsTaskChanges taskChanges = new EdsTaskChanges();
            taskChanges.setField(field);
            taskChanges.setEntityID(getObjectID());
            taskChanges.setEntityName(getNumber());
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                taskChanges.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Number) {
                taskChanges.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                taskChanges.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                taskChanges.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            } else if (oldValue instanceof EdsReference) {
                taskChanges.setFromReferenceId(((EdsReference) oldValue).getObjectID());
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                taskChanges.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Number) {
                taskChanges.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                taskChanges.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                taskChanges.setToStringValue((Boolean) newValue ? "Yes" : "No");
            } else if (newValue instanceof EdsReference) {
                taskChanges.setToReferenceId(((EdsReference) newValue).getObjectID());
            }
            taskChanges.setModificationDate(new Date());
            taskChanges.setUpdater((EdsUser) SecurityContext.getInstance().getUser());
            taskChanges.setSuperUser(ServerUtils.isSuperUser());
            getTaskHistory().add(taskChanges);
            addChange(field);
        }
    }
}
