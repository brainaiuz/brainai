package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Markedable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Anvarbek
 * Date: 07.01.2008
 * Time: 14:47:09
 */
public class ProjectListItem extends Relational implements IsSerializable, UserGrant, Markedable, ListingCustomFields {

    public static final String CLIENT = "client";
    public static final String PROJECT_RELATION_CLIENT = "PROJECT_RELATION_CLIENT";
    public static final String DESCRIPTION = "description";
    public static final String END_DATE = "endDate";
    public static final String HEAD_COUNT = "headCount";
    public static final String ACTUAL_TIME_SPENT = "actualHoursSpent";
    public static final String HOURS_SPENT = "hoursSpent";
    public static final String INVOICES = "invoices";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String LOCATION = "projectLocation";
    public static final String MANAGER = "manager";
    public static final String BACKUP_MANAGER = "backupManager";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String NUMBER_OF_TASKS = "taskCount";
    public static final String OBJECT_ID = "objectId";
    public static final String PERCENT_COMPLETED = "complete";
    public static final String STATUS = "status";
    public static final String START_DATE = "startDate";
    public static final String TEAMS = "teams";
    public static final String PROJECT_PARENT_ID = "parentId";
    public static final String STRING_VALUE = "string_value";
    public static final String DATE_VALUE = "date_value";
    public static final String NUMBER_VALUE = "double_value";
    public static final String ESTIMATED_TIME = "estimatedTime";
    public static final String PLANED_INCOME = "planedIncome";
    public static final String INCOME = "income";
    public static final String PLANED_COST = "planedCost";
    public static final String COST = "cost";
    public static final String PLANED_PROFIT = "planedProfit";
    public static final String PROFIT = "profit";
    public static final String DIFFERENCE = "difference";
    public static final String WAITING_HOURS = "waitingHours";
    public static final String REJECTED_HOURS = "rejectedHours";
    public static final String CREATED_BY = "createdBy";
    public static final String CONTRACT = "contract";
    public static final String BILLABLE = "billable";
    public static final String CREATED_DATE = "Created date";
    public static final String MODIFIED_BY = "Modified By";
    public static final String MODIFIED_DATE = "Modified date";

    private String client;
    private String complete;
    private HashMap<String, Object> customFields;
    private Integer defaultProjectId;
    private Integer crmProjectId;
    private String description;
    private Date dueDate;
    private Date endDate;
    private Integer headCount;
    private String actualHoursSpent;
    private String hoursSpent;
    private String invoiceNumber;
    private Date lastUpdate;
    private Integer managerId;
    private String manager;
    private Integer backupManagerId;
    private ArrayList<Integer> backupManagerIDs;
    private String backupManager;
    private String name;
    private Boolean newProject;
    private String number;
    private Integer objectId;
    private int permission;
    private Integer projectCreatorID;
    private String createdBy;
    private Integer projectLocationId;
    private String projectLocation;
    private Date startDate;
    private String status;
    private Integer statusId;
    private String statusCode;
    private Long taskCount;
    private String teams;
    private Integer estimatedTime;

    private BigDecimal planedIncome;
    private BigDecimal income;

    private BigDecimal planedCost;
    private BigDecimal cost;

    private BigDecimal planedProfit;
    private BigDecimal profit;

    private BigDecimal difference;

    private String waitingHours;
    private String rejectedHours;
    private String contractName;
    private Integer contractId;
    private Boolean billable;

    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getPlanedProfit() {
        return planedProfit;
    }

    public void setPlanedProfit(BigDecimal planedProfit) {
        this.planedProfit = planedProfit;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getIncome() {
        return income != null ? income : BigDecimal.ZERO;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getCost() {
        return cost != null ? cost : BigDecimal.ZERO;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public HashMap<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFields.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFields.put(columnCodeKey, cellValue);
    }

    public Integer getDefaultProjectId() {
        return defaultProjectId;
    }

    public void setDefaultProjectId(Integer defaultProjectId) {
        this.defaultProjectId = defaultProjectId;
    }

    public Integer getCrmProjectId() {
        return crmProjectId;
    }

    public void setCrmProjectId(Integer crmProjectId) {
        this.crmProjectId = crmProjectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getHeadCount() {
        return headCount;
    }

    public void setHeadCount(Integer headCount) {
        this.headCount = headCount;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getBackupManagerId() {
        return backupManagerId;
    }

    public void setBackupManagerId(Integer backupManagerId) {
        this.backupManagerId = backupManagerId;
    }

    public ArrayList<Integer> getBackupManagerIDs() {
        return backupManagerIDs;
    }

    public void setBackupManagerIDs(ArrayList<Integer> backupManagerIDs) {
        this.backupManagerIDs = backupManagerIDs;
    }

    public String getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(String backupManager) {
        this.backupManager = backupManager;
    }

    @Override
    public Boolean isMarked() {
        return newProject;
    }

    @Override
    public void setMarked(Boolean marked) {
        newProject = marked;
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

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    @Override
    public void setPermission(int permission) {
        this.permission = permission;
    }

    @Override
    public int getPermission() {
        return permission;
    }

    public Integer getProjectCreatorID() {
        return projectCreatorID;
    }

    public void setProjectCreatorID(Integer projectCreatorID) {
        this.projectCreatorID = projectCreatorID;
    }

    public Integer getProjectLocationId() {
        return projectLocationId;
    }

    public void setProjectLocationId(Integer projectLocationId) {
        this.projectLocationId = projectLocationId;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public BigDecimal getPlanedIncome() {
        return planedIncome != null ? planedIncome : BigDecimal.ZERO;
    }

    public void setPlanedIncome(BigDecimal planedIncome) {
        this.planedIncome = planedIncome;
    }

    public BigDecimal getPlanedCost() {
        return planedCost != null ? planedCost : BigDecimal.ZERO;
    }

    public void setPlanedCost(BigDecimal planedCost) {
        this.planedCost = planedCost;
    }

    @Override
    public Integer getRelationID() {
        return getObjectId();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_PROJECT;
    }

    @Override
    public String getRelationName() {
        return getName();
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }
}