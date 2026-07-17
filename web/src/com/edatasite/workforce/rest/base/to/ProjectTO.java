package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umidbek on 16.02.2015.
 */
public class ProjectTO implements IsSerializable {

    private Integer id;
    private String name;
    private String number;
    private String description;
    private Integer parentId;

    private Integer headcount;
    private Boolean billable;

    private Long startDate;
    private Long dueDate;
    private Long endDate;
    private Long actualStartDate;
    private Long actualEndDate;

    private Integer estimatedTime;
    private Integer hoursSpent;
    private Integer actualHoursSpent;
    private Integer waitingHours;
    private Integer rejectedHours;

    private String estimatedCost;
    private String actualCost;
    private Integer dailyTotal;

    private SelectItemTO status;

    private UserTO client;

    private UserTO manager;
    private ArrayList<UserTO> backupManagers;

    private Long creationDate;
    private Long modifiedDate;
    private UserTO creator;
    private UserTO modifiedBy;

    private LocationTO location;
    private ArrayList<ProjectAssigneeTO> projectAssignees;//for add/edit view
    private ArrayList<ProjectEmployeeTO> projectEmployees;//for summary view

    private List<? extends Object> customFields; //Added for Happy Tenant (Doni)

    public ProjectTO() {
    }

    public ProjectTO(SelectItem item) {
        this.id = item.getId();
        this.name = item.getName();
    }

    public ProjectTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProjectTO(Integer id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public ProjectTO(ProjectListItem item) {
        this.id = item.getObjectId();
        this.name = item.getName();
        this.number = item.getNumber();
        this.status = new SelectItemTO(item.getStatusId(), item.getStatus(), item.getStatusCode(), "");
        this.startDate = WrapUtils.dateToLong(item.getStartDate());
        this.endDate = WrapUtils.dateToLong(item.getEndDate());
    }

    public ProjectTO(ProjectListItem item, boolean isBriefly) {
        this(item);
        this.description = item.getDescription();
        this.dueDate = WrapUtils.dateToLong(item.getDueDate());
        if (!StringUtil.isEmpty(item.getClient())) {
            this.client = new UserTO(null, item.getClient());
        }

        this.manager = new UserTO(item.getManagerId(), item.getManager());
        this.headcount = item.getHeadCount();

        this.estimatedTime = item.getEstimatedTime();
        this.hoursSpent = WrapUtils.timeToMinutes(item.getHoursSpent());
        this.waitingHours = WrapUtils.timeToMinutes(item.getWaitingHours());
        this.rejectedHours = WrapUtils.timeToMinutes(item.getRejectedHours());

        this.estimatedCost = item.getPlanedCost().toString();
        this.actualCost = item.getCost().toString();


        this.creator = new UserTO(item.getProjectCreatorID(), null);
        this.modifiedDate = WrapUtils.dateToLong(item.getLastUpdate());
    }

    public ProjectTO(ProjectViewItem item) {
        this.id = item.getObjectID();
        this.name = item.getName();


        if (item.getNumberData() != null) {
            this.number = item.getNumberData().getNumberString();
        }
        this.description = item.getDescription();
        if (item.getClientId() != null) {
            this.client = new UserTO(item.getClientId(), item.getClient());
        }
        if (item.getManagerId() != null) {
            this.manager = new UserTO(item.getManagerId(), item.getManager());
        }
        if (item.getStatusID() != null) {
            this.status = new SelectItemTO(item.getStatusID(), item.getStatus(), item.getStatusCode(), "");
        }
        this.billable = item.isBillable();

        this.startDate = WrapUtils.dateToLong(item.getStartDate());
        this.dueDate = WrapUtils.dateToLong(item.getDueDate());
        this.endDate = WrapUtils.dateToLong(item.getEndDate());
        this.actualStartDate = WrapUtils.dateToLong(item.getActualStartDate());
        this.actualEndDate = WrapUtils.dateToLong(item.getActualEndDate());

        this.estimatedTime = WrapUtils.timeToMinutes(item.getEstimatedTime());
        this.hoursSpent = WrapUtils.timeToMinutes(item.getHoursSpent());
        this.waitingHours = WrapUtils.timeToMinutes(item.getWaitingHours());
        this.rejectedHours = WrapUtils.timeToMinutes(item.getRejectedHours());

        this.estimatedCost = item.getEstimatedCost();
        this.actualCost = item.getActualCost();

        if (item.getBackupManagers() != null) {
            backupManagers = new ArrayList<>();
            for (SelectItem selectItem : item.getBackupManagers()) {
                this.backupManagers.add(new UserTO(selectItem.getId(), selectItem.getName()));
            }
        }

        this.creationDate = WrapUtils.dateToLong(item.getCreationDate());
        if (item.getCreatorID() != null) {
            this.creator = new UserTO(item.getCreatorID(), item.getCreator());
        }

        this.modifiedDate = WrapUtils.dateToLong(item.getLastUpdateTime());
        this.modifiedBy = new UserTO(null, item.getLastUpdaterName());

        if (item.getProjectLocation() != null && !"N/A".equals(item.getProjectLocation())) {
            String[] chunks = item.getProjectLocation().split(",");

            if (chunks.length == 2) {
                this.location = new LocationTO(item.getLocationID(), new SelectItemTO(chunks[0]), chunks[1]);
            }
        }
        if (item.getProjectEmployees() != null) {
            ArrayList<ProjectEmployeeTO> projectEmployeeList = new ArrayList<>();
            for (PositionsSelectItem projectEmployee : item.getProjectEmployees()) {
                projectEmployeeList.add(new ProjectEmployeeTO(projectEmployee));
            }
            this.projectEmployees = projectEmployeeList;
        }
        if (item.getCustomFields() != null) {
            this.customFields = item.getCustomFields();
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
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

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public UserTO getClient() {
        return client;
    }

    public void setClient(UserTO client) {
        this.client = client;
    }

    public UserTO getManager() {
        return manager;
    }

    public void setManager(UserTO manager) {
        this.manager = manager;
    }

    public ArrayList<UserTO> getBackupManagers() {
        return backupManagers;
    }

    public void setBackupManagers(ArrayList<UserTO> backupManagers) {
        this.backupManagers = backupManagers;
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

    public UserTO getCreator() {
        return creator;
    }

    public void setCreator(UserTO creator) {
        this.creator = creator;
    }

    public UserTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocationTO getLocation() {
        return location;
    }

    public void setLocation(LocationTO location) {
        this.location = location;
    }

    public Integer getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(Integer dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public ArrayList<ProjectAssigneeTO> getProjectAssignees() {
        return projectAssignees;
    }

    public void setProjectAssignees(ArrayList<ProjectAssigneeTO> projectAssignees) {
        this.projectAssignees = projectAssignees;
    }

    public ArrayList<ProjectEmployeeTO> getProjectEmployees() {
        return projectEmployees;
    }

    public void setProjectEmployees(ArrayList<ProjectEmployeeTO> projectEmployees) {
        this.projectEmployees = projectEmployees;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public List<Object> getCustomFields() {
        return (List<Object>) customFields;
    }

    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }
}
