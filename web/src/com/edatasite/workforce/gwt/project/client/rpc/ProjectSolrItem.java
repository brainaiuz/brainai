package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectSolrItem implements IsSerializable {

    private SelectItem project;
    private Integer parentId;
    private SelectItem createdBy;
    private ReferenceItem status;
    private Float completed;
    private SelectItem manager;
    private ArrayList<SelectItem> backupManager = new ArrayList<>();
    private ArrayList<SelectItem> projectMultiClient = new ArrayList<>();
    private SelectItem client;
    private String clientNameSort;
    private SelectItem location;
    private List<Integer> userLocationIds = new ArrayList<>();
    private List<SelectItem> user = new ArrayList<>();
    private String hourSpent;
    private Double planedWageAmount;
    private Double planedClientChargeAmount;
    private Double planedExpensesAmount;
    private Double planedIncomeAmount;
    private Double actualWageAmount;
    private Double actualClientChargeAmount;
    private Double expensesAmount;
    private Double incomeAmount;
    private String invoice;
    private String description;
    private Date startDate;
    private Date dueDate;
    private Date endDate;
    private Date lastUpdate;
    private Boolean billible;
    private Date projectModifiedDate;
    private String projectModifiedBy;
    private Date projectCreatedDate;
    private Float projectTasksAveragePercentCompleted;
    private Float projectTasksAveragePercentCompletedNewLogic1;

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public Float getCompleted() {
        return completed;
    }

    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public SelectItem getManager() {
        return manager;
    }

    public void setManager(SelectItem manager) {
        this.manager = manager;
    }

    public ArrayList<SelectItem> getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(ArrayList<SelectItem> backupManager) {
        this.backupManager = backupManager;
    }

    public ArrayList<SelectItem> getProjectMultiClient() {
        return projectMultiClient;
    }

    public void setProjectMultiClient(ArrayList<SelectItem> projectMultiClient) {
        this.projectMultiClient = projectMultiClient;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public String getClientNameSort() {
        return clientNameSort;
    }

    public void setClientNameSort(String clientNameSort) {
        this.clientNameSort = clientNameSort;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public List<Integer> getUserLocationIds() {
        return userLocationIds;
    }

    public void setUserLocationIds(List<Integer> userLocationIds) {
        this.userLocationIds = userLocationIds;
    }

    public List<SelectItem> getUser() {
        return user;
    }

    public void setUser(List<SelectItem> user) {
        this.user = user;
    }

    public String getHourSpent() {
        return hourSpent;
    }

    public void setHourSpent(String hourSpent) {
        this.hourSpent = hourSpent;
    }

    public Double getPlanedWageAmount() {
        return planedWageAmount;
    }

    public void setPlanedWageAmount(Double planedWageAmount) {
        this.planedWageAmount = planedWageAmount;
    }

    public Double getPlanedClientChargeAmount() {
        return planedClientChargeAmount;
    }

    public void setPlanedClientChargeAmount(Double planedClientChargeAmount) {
        this.planedClientChargeAmount = planedClientChargeAmount;
    }

    public Double getPlanedExpensesAmount() {
        return planedExpensesAmount;
    }

    public void setPlanedExpensesAmount(Double planedExpensesAmount) {
        this.planedExpensesAmount = planedExpensesAmount;
    }

    public Double getPlanedIncomeAmount() {
        return planedIncomeAmount;
    }

    public void setPlanedIncomeAmount(Double planedIncomeAmount) {
        this.planedIncomeAmount = planedIncomeAmount;
    }

    public Double getActualWageAmount() {
        return actualWageAmount;
    }

    public void setActualWageAmount(Double actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public Double getActualClientChargeAmount() {
        return actualClientChargeAmount;
    }

    public void setActualClientChargeAmount(Double actualClientChargeAmount) {
        this.actualClientChargeAmount = actualClientChargeAmount;
    }

    public Double getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(Double expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getBillable() {
        return billible;
    }

    public void setBillable(Boolean billible) {
        this.billible = billible;
    }

    public Date getProjectModifiedDate() {
        return projectModifiedDate;
    }

    public void setProjectModifiedDate(Date projectModifiedDate) {
        this.projectModifiedDate = projectModifiedDate;
    }

    public String getProjectModifiedBy() {
        return projectModifiedBy;
    }

    public void setProjectModifiedBy(String projectModifiedBy) {
        this.projectModifiedBy = projectModifiedBy;
    }

    public Date getProjectCreatedDate() {
        return projectCreatedDate;
    }

    public void setProjectCreatedDate(Date projectCreatedDate) {
        this.projectCreatedDate = projectCreatedDate;
    }

    public Float getProjectTasksAveragePercentCompleted() {
        return projectTasksAveragePercentCompleted;
    }

    public void setProjectTasksAveragePercentCompleted(Float projectTasksAveragePercentCompleted) {
        this.projectTasksAveragePercentCompleted = projectTasksAveragePercentCompleted;
    }

    public Float getProjectTasksAveragePercentCompletedNewLogic1() {
        return projectTasksAveragePercentCompletedNewLogic1;
    }

    public void setProjectTasksAveragePercentCompletedNewLogic1(Float projectTasksAveragePercentCompletedNewLogic1) {
        this.projectTasksAveragePercentCompletedNewLogic1 = projectTasksAveragePercentCompletedNewLogic1;
    }

    public Float getProjectTasksAveragePercentCompletedNewLogic1(Float timespent, Float estimatedtime) {
        return estimatedtime != null && estimatedtime != 0 && timespent != null ? timespent * 100 / estimatedtime : 0;
    }
}
