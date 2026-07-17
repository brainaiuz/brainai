package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProjectAllItem {

    //EditProject
    private Integer objectID; //objectId
    private String number;
    private String name;
    private String description;
    private String clientName;
    private Date endDate;
    private Date startDate;
    private Date dueDate;
    private Date lastUpdate;
    private String managerName;
    private String backupManagerName;
    private Integer managerID; //id
    private Integer backupManagerID;//backupManagerId
    private List<Integer> backupManagerIDs;
    private Integer clientID; //id
    private Integer statusID;
    private Integer locationID; //id
    private Integer permission;    //int


    //ProjectListItem
    private String client;
    private String complete;
    private Integer defaultProjectID; //id
    //private String description;
    //private Date dueDate;
    //private Date endDate;
    private Integer headCount;
    private String hoursSpent;
    private String invoiceNumber;
    // private Date lastUpdate;
    private String manager;
    private String backupManager;
    //private String name;
    private Boolean newProject;
    //private String number;
    //private Integer objectId;
    //private int permission;
    //private PermissionListItem permissions;
    private String projectLocation;
    //private Date startDate;
    private String status;
    private Long taskCount;
    private String teams;


    //ProjectSingleItem
    //private String name;
    //private String description;
    //private Date startDate;
    //private Date endDate;
    private String timeSpentHM;
    private Date changedOn;
    //private int statusID; //id
    //private int managerID;
    //private int backupManagerID;
    //private int clientID;
    //private int locationID;
    //private ProjectMember[] projectMembers;
    //private Integer[] projectMembersID; //int
    private List<Integer> projectMembersID;
    //private FileItem[] attachments;
    //private NumberData numberData;
    //private List<CompanyCustomFieldItem> customFieldItems;


    public MProjectAllItem() {

    }

    public MProjectAllItem(ProjectSingleItem projectSingleItem) {
       if (projectSingleItem !=null) {
           this.name = projectSingleItem.getName();
           this.description = projectSingleItem.getDescription();
           this.startDate = projectSingleItem.getStartDate();
           this.endDate = projectSingleItem.getEndDate();
           this.timeSpentHM = projectSingleItem.getTimeSpentHM();
           this.changedOn = projectSingleItem.getChangedOn();
           this.statusID = projectSingleItem.getStatusId();
           this.managerID = projectSingleItem.getManagerId();
           this.backupManagerID = projectSingleItem.getBackupManagerId();
           this.backupManagerIDs = projectSingleItem.getBackupManagerIDs();
           this.clientID = projectSingleItem.getClientId();
           this.locationID = projectSingleItem.getLocationId();
           if (projectSingleItem.getProjectMembers() != null) {
               this.projectMembersID = new ArrayList<>();
               for (int memberID : projectSingleItem.getProjectMembersId()) {
                   this.projectMembersID.add(memberID);
               }
//               for (ProjectMember projectMember : projectSingleItem.getProjectMembers()) {
//                   this.projectMembersID.add(projectMember.getId());
//               }
           }
       }
    }


    public MProjectAllItem(EditProject editProject) {
        if (editProject != null) {
            this.objectID = editProject.getObjectId();
            this.number = editProject.getNumber();
            this.name = editProject.getName();
            this.description = editProject.getDescription();
            this.clientName = editProject.getClientName();
            this.endDate = editProject.getEndDate();
            this.startDate = editProject.getStartDate();
            this.dueDate = editProject.getDueDate();
            this.lastUpdate = editProject.getLastUpdate();
            this.managerName = editProject.getManagerName();
            this.backupManagerName = editProject.getBackupManagerName();
            this.managerID = editProject.getManagerId();
            this.clientID = editProject.getClientId();
            this.statusID = editProject.getStatusId();
            this.locationID = editProject.getLocationId();
            this.permission = editProject.getPermission();
        }
    }

    public MProjectAllItem(ProjectListItem projectListItem) {
        if (projectListItem != null) {
            this.client = projectListItem.getClient();
            this.complete = projectListItem.getComplete();
            this.defaultProjectID = projectListItem.getDefaultProjectId();
            this.description = projectListItem.getDescription();
            this.dueDate = projectListItem.getDueDate();
            this.endDate = projectListItem.getEndDate();
            this.headCount = projectListItem.getHeadCount();
            this.hoursSpent = projectListItem.getActualHoursSpent();
            this.invoiceNumber = projectListItem.getInvoiceNumber();
            this.lastUpdate = projectListItem.getLastUpdate();
            this.manager = projectListItem.getManager();
            this.backupManager = projectListItem.getBackupManager();
            this.name = projectListItem.getName();
            this.newProject = projectListItem.isMarked();
            this.number = projectListItem.getNumber();
            this.objectID = projectListItem.getObjectId();
            this.permission = projectListItem.getPermission();
            this.projectLocation = projectListItem.getProjectLocation();
            this.startDate = projectListItem.getStartDate();
            this.status = projectListItem.getStatus();
            this.taskCount = projectListItem.getTaskCount();
            this.teams = projectListItem.getTeams();

        }
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBackupManagerName() {
        return backupManagerName;
    }

    public void setBackupManagerName(String backupManagerName) {
        this.backupManagerName = backupManagerName;
    }

    public Integer getManagerID() {
        return managerID;
    }

    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }

    public Integer getBackupManagerID() {
        return backupManagerID;
    }

    public void setBackupManagerID(Integer backupManagerID) {
        this.backupManagerID = backupManagerID;
    }

    public List<Integer> getBackupManagerIDs() {
        return backupManagerIDs;
    }

    public void setBackupManagerIDs(List<Integer> backupManagerIDs) {
        this.backupManagerIDs = backupManagerIDs;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
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

    public Integer getDefaultProjectID() {
        return defaultProjectID;
    }

    public void setDefaultProjectID(Integer defaultProjectID) {
        this.defaultProjectID = defaultProjectID;
    }

    public Integer getHeadCount() {
        return headCount;
    }

    public void setHeadCount(Integer headCount) {
        this.headCount = headCount;
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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(String backupManager) {
        this.backupManager = backupManager;
    }

    public Boolean getNewProject() {
        return newProject;
    }

    public void setNewProject(Boolean newProject) {
        this.newProject = newProject;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
