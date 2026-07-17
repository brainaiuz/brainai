package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.BaseListItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 11.01.2008
 * Time: 14:34:54
 * To change this template use File | Settings | File Templates.
 */
public class ProjectViewItem extends BaseListItem implements IsSerializable, UserGrant {

    private String name;
    private Date startDate;
    private Date dueDate;
    private String manager;
    private String client;
    private Integer clientId;
    private Integer statusID;
    private String status;
    private String statusCode;
    private String complete;
    private String hoursSpent;
    private String estimatedTime;
    private String actualCost;
    private String estimatedCost;
    private int cancelledTasks;
    private int closedTasks;
    private String description;
    private int membersInvolved;
    private int inProgressTasks;
    private int notStartedTasks;
    private int completedTasks;
    private int waitingTasks;
    private Integer ObjectID;
    private Integer defaultProjectID;
    private Date actualStartDate;
    private Date actualEndDate;
    //    private boolean canModify;
    private String creator;
    private Integer creatorID;
    private Date creationDate;
    private String lastUpdaterName;
    private Date lastUpdateTime;
    private int permission;
    private String projectLocation;
    private Integer locationID;
    private NumberData numberData;
    private ArrayList<SelectItem> backupManagers;
    private Integer managerId;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashSet<String> permissions;
    private boolean isSupplier;
    private PositionsSelectItem[] projectEmployees;
    private String timeSpent;
    private String waitingHours;
    private String rejectedHours;
    private EmployeeAssignmentEnum employeeAssignment;
    private SelectItem[] clients;
    private String contractName;
    private Integer contractID;
    private Double clientBalance;
    private Double clientRetainers;
    private boolean billable;
    private Integer crmProjectID;
    private SelectItem[] templates;
    private ArrayList<SelectItem> owners = new ArrayList<>();
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private List<CheckInLocationItem> checkInLocations;

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private Date endDate;

    public void setPermission(int b) {
        this.permission = b;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermissions(HashSet<String> p) {
        this.permissions = p;
    }

    public HashSet<String> getPermissions() {
        return permissions;
    }

    public String getCreator() {
        return creator;
    }

    private FileItem[] attachments;

    private FileResource[] projectAttachments;


    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdaterName() {
        return lastUpdaterName;
    }

    public void setLastUpdaterName(String lastUpdaterName) {
        this.lastUpdaterName = lastUpdaterName;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
//	public boolean isCanModify() {
//		return canModify;
//	}
//
//	public void setCanModify(boolean canModify) {
//		this.canModify = canModify;
//	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMembersInvolved() {
        return membersInvolved;
    }

    public void setMembersInvolved(int membersInvolved) {
        this.membersInvolved = membersInvolved;
    }

    public int getNotStartedTasks() {
        return notStartedTasks;
    }

    public void setNotStartedTasks(int notStartedTasks) {
        this.notStartedTasks = notStartedTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(int inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public Integer getObjectID() {
        return ObjectID;
    }

    public void setObjectID(Integer objectID) {
        ObjectID = objectID;
    }

    public Integer getDefaultProjectID() {
        return defaultProjectID;
    }

    public void setDefaultProjectID(Integer defaultProjectID) {
        this.defaultProjectID = defaultProjectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(String hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getActualCost() {
        return actualCost;
    }

    public void setActualCost(String actualCost) {
        this.actualCost = actualCost;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public int getCancelledTasks() {
        return cancelledTasks;
    }

    public void setCancelledTasks(int cancelledTasks) {
        this.cancelledTasks = cancelledTasks;
    }

    public int getClosedTasks() {
        return closedTasks;
    }

    public void setClosedTasks(int closedTasks) {
        this.closedTasks = closedTasks;
    }

    public int getWaitingTasks() {
        return waitingTasks;
    }

    public void setWaitingTasks(int waitingTasks) {
        this.waitingTasks = waitingTasks;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
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

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public FileResource[] getProjectAttachments() {
        return projectAttachments;
    }

    public void setProjectAttachments(FileResource[] projectAttachments) {
        this.projectAttachments = projectAttachments;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public ArrayList<SelectItem> getBackupManagers() {
        return backupManagers;
    }

    public void setBackupManagers(ArrayList<SelectItem> backupManagers) {
        this.backupManagers = backupManagers;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_PROJECT;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public PositionsSelectItem[] getProjectEmployees() {
        return projectEmployees;
    }

    public void setProjectEmployees(PositionsSelectItem[] projectEmployees) {
        this.projectEmployees = projectEmployees;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
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

    public EmployeeAssignmentEnum getEmployeeAssignment() {
        return employeeAssignment;
    }

    public void setEmployeeAssignment(EmployeeAssignmentEnum employeeAssignment) {
        this.employeeAssignment = employeeAssignment;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getContractID() {
        return contractID;
    }

    public void setContractID(Integer contractID) {
        this.contractID = contractID;
    }

    public Double getClientBalance() {
        return clientBalance != null ? clientBalance : 0d;
    }

    public void setClientBalance(Double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public Double getClientRetainers() {
        return clientRetainers != null ? clientRetainers : 0d;
    }

    public void setClientRetainers(Double clientRetainers) {
        this.clientRetainers = clientRetainers;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public Integer getCrmProjectID() {
        return crmProjectID;
    }

    public void setCrmProjectID(Integer crmProjectID) {
        this.crmProjectID = crmProjectID;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public ArrayList<SelectItem> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<SelectItem> owners) {
        this.owners = owners;
    }

    public List<CheckInLocationItem> getCheckInLocations() {
        return checkInLocations;
    }

    public void setCheckInLocations(List<CheckInLocationItem> checkInLocations) {
        this.checkInLocations = checkInLocations;
    }
}
