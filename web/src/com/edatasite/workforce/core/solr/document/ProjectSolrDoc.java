package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@SolrDocument(collection = "projectCore")
public class ProjectSolrDoc extends RelationBaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("projectId")
    private Integer projectId;

    @Field("projectNumber")
    private String projectNumber;

    @Field("projectName")
    private String projectName;

    @Field("parentId")
    @Indexed(name = "parentId", type = "pint", stored = false)
    private Integer parentId;

    @Field("projectCreatorId")
    private Integer projectCreatorId;

    @Field("projectCreator")
    private String projectCreator;

    @Field("statusId")
    @Indexed(name = "statusId", type = "pint", stored = false)
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdCode")
    @Indexed(name = "statusIdCode", type = "pint", stored = false)
    private String statusIdCode;

    @Field("statusIdCodeName")
    @Indexed(name = "statusIdCodeName", type = "pint", stored = false)
    private String statusIdCodeName;

    @Field("statusSorder")
    @Indexed(name = "statusSorder", type = "pint", stored = false)
    private Integer statusSorder;

    @Field("completed")
    private Float completed;

    @Field("managerId")
    private Integer managerId;

    @Field("managerName")
    private String managerName;

    @Field("managerIdName")
    @Indexed(name = "managerIdName", type = "string", stored = false)
    private String managerIdName;

    @Field("backupManagerId")
    @Indexed(name = "backupManagerId", type = "pints")
    private ArrayList<Integer> backupManagerId = new ArrayList<>();

    @Field("backupManagerName")
    @Indexed(name = "backupManagerName", type = "strings")
    private List<String> backupManagerName = new ArrayList<>();

    @Field("backupManagerIdName")
    @Indexed(name = "backupManagerIdName", type = "strings", stored = false)
    private List<String> backupManagerIdName = new ArrayList<>();

    @Field("projectMultiClientId")
    @Indexed(name = "projectMultiClientId", type = "pints", stored = false)
    private List<Integer> projectMultiClientId = new ArrayList<>();

    @Field("projectMultiClientName")
    @Indexed(name = "projectMultiClientName", type = "strings")
    private List<String> projectMultiClientName = new ArrayList<>();

    @Field("projectMultiClientIdName")
    @Indexed(name = "projectMultiClientIdName", type = "strings", stored = false)
    private List<String> projectMultiClientIdName = new ArrayList<>();

    @Field("clientNameSort")
    private String clientNameSort;

    @Field("clientName")
    private String clientName;

    @Field("clientId")
    @Indexed(name = "clientId", type = "pint", stored = false)
    private Integer clientId;

    @Field("clientIdName")
    @Indexed(name = "clientIdName", type = "string", stored = false)
    private String clientIdName;

    @Field("locationName")
    private String locationName;

    @Field("locationId")
    private Integer locationId;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("userLocationId")
    @Indexed(name = "userLocationId", type = "pints", stored = false)
    private List<Integer> userLocationId = new ArrayList<>();

    @Field("userName")
    @Indexed(name = "userName", type = "strings", stored = false)
    private List<String> userName = new ArrayList<>();

    @Field("userId")
    @Indexed(name = "userId", type = "pints", stored = false)
    private List<Integer> userId = new ArrayList<>();

    @Field("userIdName")
    @Indexed(name = "userIdName", type = "strings", stored = false)
    private List<String> userIdName = new ArrayList<>();

    @Field("hourSpent")
    private String hourSpent;

    @Field("planedWageAmount")
    private Double planedWageAmount;

    @Field("planedClientChargeAmount")
    private Double planedClientChargeAmount;

    @Field("planedExpensesAmount")
    private Double planedExpensesAmount;

    @Field("planedIncomeAmount")
    private Double planedIncomeAmount;

    @Field("actualWageAmount")
    private Double actualWageAmount;

    @Field("actualClientChargeAmount")
    private Double actualClientChargeAmount;

    @Field("expensesAmount")
    private Double expensesAmount;

    @Field("incomeAmount")
    private Double incomeAmount;

    @Field("invoice")
    private String invoice;

    @Field("description")
    private String description;

    @Field("startDate")
    private Date startDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("endDate")
    private Date endDate;

    @Field("lastUpdate")
    @Indexed(name = "lastUpdate", type = "pdate", stored = false)
    private Date lastUpdate;

    @Field("billible")
    private Boolean billible;

    @Field("projectModifiedDate")
    private Date projectModifiedDate;

    @Field("projectModifiedBy")
    private String projectModifiedBy;

    @Field("projectCreatedDate")
    private Date projectCreatedDate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getProjectCreatorId() {
        return projectCreatorId;
    }

    public void setProjectCreatorId(Integer projectCreatorId) {
        this.projectCreatorId = projectCreatorId;
    }

    public String getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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

    public String getStatusIdCode() {
        return statusIdCode;
    }

    public void setStatusIdCode(String statusIdCode) {
        this.statusIdCode = statusIdCode;
    }

    public String getStatusIdCodeName() {
        return statusIdCodeName;
    }

    public void setStatusIdCodeName(String statusIdCodeName) {
        this.statusIdCodeName = statusIdCodeName;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public Float getCompleted() {
        return completed;
    }

    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerIdName() {
        return managerIdName;
    }

    public void setManagerIdName(String managerIdName) {
        this.managerIdName = managerIdName;
    }

    public ArrayList<Integer> getBackupManagerId() {
        return backupManagerId;
    }

    public void setBackupManagerId(ArrayList<Integer> backupManagerId) {
        this.backupManagerId = backupManagerId;
    }

    public List<String> getBackupManagerName() {
        return backupManagerName;
    }

    public void setBackupManagerName(List<String> backupManagerName) {
        this.backupManagerName = backupManagerName;
    }

    public List<String> getBackupManagerIdName() {
        return backupManagerIdName;
    }

    public void setBackupManagerIdName(List<String> backupManagerIdName) {
        this.backupManagerIdName = backupManagerIdName;
    }

    public List<Integer> getProjectMultiClientId() {
        return projectMultiClientId;
    }

    public void setProjectMultiClientId(List<Integer> projectMultiClientId) {
        this.projectMultiClientId = projectMultiClientId;
    }

    public List<String> getProjectMultiClientName() {
        return projectMultiClientName;
    }

    public void setProjectMultiClientName(List<String> projectMultiClientName) {
        this.projectMultiClientName = projectMultiClientName;
    }

    public List<String> getProjectMultiClientIdName() {
        return projectMultiClientIdName;
    }

    public void setProjectMultiClientIdName(List<String> projectMultiClientIdName) {
        this.projectMultiClientIdName = projectMultiClientIdName;
    }

    public String getClientNameSort() {
        return clientNameSort;
    }

    public void setClientNameSort(String clientNameSort) {
        this.clientNameSort = clientNameSort;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientIdName() {
        return clientIdName;
    }

    public void setClientIdName(String clientIdName) {
        this.clientIdName = clientIdName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationIdName() {
        return locationIdName;
    }

    public void setLocationIdName(String locationIdName) {
        this.locationIdName = locationIdName;
    }

    public List<Integer> getUserLocationId() {
        return userLocationId;
    }

    public void setUserLocationId(List<Integer> userLocationId) {
        this.userLocationId = userLocationId;
    }

    public List<String> getUserName() {
        return userName;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public List<Integer> getUserId() {
        return userId;
    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }

    public List<String> getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(List<String> userIdName) {
        this.userIdName = userIdName;
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

    public Boolean getBillible() {
        return billible != null && billible;
    }

    public void setBillible(Boolean billible) {
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
}
