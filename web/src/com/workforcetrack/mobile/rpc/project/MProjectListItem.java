package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.workforcetrack.api.controllers.ProjectApiController.BACKUP_MANAGER_ID;
import static com.workforcetrack.api.controllers.ProjectApiController.BACKUP_MANAGER_IDS;
import static com.workforcetrack.api.controllers.ProjectApiController.BACKUP_MANAGER_NAME;
import static com.workforcetrack.api.controllers.ProjectApiController.CLIENT;
import static com.workforcetrack.api.controllers.ProjectApiController.CLIENT_ID;
import static com.workforcetrack.api.controllers.ProjectApiController.COMPLETE;
import static com.workforcetrack.api.controllers.ProjectApiController.DESCRIPTION;
import static com.workforcetrack.api.controllers.ProjectApiController.DUE_DATE;
import static com.workforcetrack.api.controllers.ProjectApiController.END_DATE;
import static com.workforcetrack.api.controllers.ProjectApiController.MANAGER_ID;
import static com.workforcetrack.api.controllers.ProjectApiController.MANAGER_NAME;
import static com.workforcetrack.api.controllers.ProjectApiController.NAME;
import static com.workforcetrack.api.controllers.ProjectApiController.OBJECT_ID;
import static com.workforcetrack.api.controllers.ProjectApiController.PROJECT_MEMBERS;
import static com.workforcetrack.api.controllers.ProjectApiController.START_DATE;
import static com.workforcetrack.api.controllers.ProjectApiController.STATUS_ID;
import static com.workforcetrack.api.controllers.ProjectApiController.STATUS_NAME;


/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "projectLisItem")
public class MProjectListItem {

    private Integer objectID;
    private String name;
    private MNumberData number;
    private String manager;
    private String client;
    private String hoursSpent;
    private String teams;
    private Date startDate;
    private Date endDate;
    private String status;
    private String complete;
    private String description;
    private int permission;
    private Date lastUpdate;
    private Date dueDate;
    private Integer headCount;
    private Long taskCount;
//    private Integer defaultProjectID;
//    private String projectLocation;


    //EditProject
    private String managerName;
    private String backupManagerName;
    private Integer managerID;
    private Integer backupManagerID;
    private ArrayList<Integer> backupManagerIDs;
    private Integer clientID;
    private Integer statusID;
    //private Boolean copyNewEmployeesToProjectTasks;


    //ProjectSingleItem
    //private String timeSpentHM;
    //private Date changedOn;
    //private int locationID;
    private Integer parentID; // not need )))
    private List<MProjectMember> projectMembers;
    //private int[] projectMembersId;
    //private NumberData numberData;

    public MProjectListItem() {
    }

    //FOR API
    public MProjectListItem(Map<String, Object> map) throws ParseException, ClassCastException, NumberFormatException {
        if (map != null && !map.isEmpty()) {
            this.objectID = (Integer) map.get(OBJECT_ID);
            this.name = (String) map.get(NAME);
            this.managerName = (String) map.get(MANAGER_NAME);
            this.managerID = (Integer) map.get(MANAGER_ID);
            this.backupManagerID = (Integer) map.get(BACKUP_MANAGER_ID);
            this.backupManagerIDs = (ArrayList) map.get(BACKUP_MANAGER_IDS);
            this.backupManagerName = (String) map.get(BACKUP_MANAGER_NAME);
            this.clientID = (Integer) map.get(CLIENT_ID);
            this.client = (String) map.get(CLIENT);
            this.complete = (String) map.get(COMPLETE);
            this.statusID = (Integer) map.get(STATUS_ID);
            this.status = (String) map.get(STATUS_NAME);
            this.description = (String) map.get(DESCRIPTION);

            SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
            this.startDate = (map.get(START_DATE) != null) ? dateFormat.parse((String) map.get(START_DATE)) : null;
            this.endDate = (map.get(END_DATE) != null) ? dateFormat.parse((String) map.get(END_DATE)) : null;
            this.dueDate = (map.get(DUE_DATE) != null) ? dateFormat.parse((String) map.get(DUE_DATE)) : null;

            if (map.get(PROJECT_MEMBERS) != null) {
                List<Map<String, Object>> projectMembers = (List<Map<String, Object>>) map.get(PROJECT_MEMBERS);
                if (projectMembers.size() > 0) {
                    this.projectMembers = new ArrayList<>();
                    for (Map<String, Object> member : projectMembers) {
                        this.projectMembers.add(new MProjectMember(member));
                    }
                }
            }
        }
    }

    public MProjectListItem(ProjectListItem projectListItem) {
        if (projectListItem != null) {
            this.objectID = projectListItem.getObjectId();
            this.name = projectListItem.getName();
            this.manager = projectListItem.getManager();
            this.client = projectListItem.getClient();
            this.hoursSpent = projectListItem.getActualHoursSpent();
            this.teams = projectListItem.getTeams();
            this.startDate = projectListItem.getStartDate();
            this.endDate = projectListItem.getEndDate();
            this.status = projectListItem.getStatus();
            this.complete = projectListItem.getComplete();
            this.description = projectListItem.getDescription();
            this.permission = projectListItem.getPermission();
            this.lastUpdate = projectListItem.getLastUpdate();
            this.dueDate = projectListItem.getDueDate();
            this.headCount = projectListItem.getHeadCount();
            this.taskCount = projectListItem.getTaskCount();
        }
    }

    public MProjectListItem(EditProject projectListItem) {
        if (projectListItem != null) {
            this.objectID = projectListItem.getObjectId();
            this.name = projectListItem.getName();
            this.number = new MNumberData(projectListItem.getNumberData());
            this.startDate = projectListItem.getStartDate();
            this.endDate = projectListItem.getEndDate();
            this.description = projectListItem.getDescription();
            this.permission = projectListItem.getPermission();
            this.lastUpdate = projectListItem.getLastUpdate();
            this.dueDate = projectListItem.getDueDate();

            this.manager = projectListItem.getManagerName();
            this.client = projectListItem.getClientName();

            this.managerID = projectListItem.getManagerId();
            this.backupManagerID = projectListItem.getBackupManagerId();
            this.backupManagerIDs = projectListItem.getBackupManagerIDs();
            this.clientID = projectListItem.getClientId();
            this.statusID = projectListItem.getStatusId();
            this.managerName = projectListItem.getManagerName();
            this.backupManagerName = projectListItem.getBackupManagerName();
            this.complete = projectListItem.getComplete() != null ? projectListItem.getComplete() : "0";
            this.taskCount = projectListItem.getTaskCount();
            this.hoursSpent = projectListItem.getHoursSpent();


        }

    }


    public MProjectListItem(ProjectSingleItem projectListItem) {
        if (projectListItem != null) {
            this.name = projectListItem.getName();
            this.managerID = projectListItem.getManagerId();
            this.clientID = projectListItem.getClientId();
            this.startDate = projectListItem.getStartDate();
            this.endDate = projectListItem.getEndDate();
            this.statusID = projectListItem.getStatusId();
            this.description = projectListItem.getDescription();
            this.backupManagerID = projectListItem.getBackupManagerId();
            this.backupManagerIDs = projectListItem.getBackupManagerIDs();
            this.parentID = projectListItem.getParentId();
            if (projectListItem.getProjectMembers() != null) {
                this.projectMembers = new ArrayList<>();
                for (ProjectMember projectMember : projectListItem.getProjectMembers()) {
                    this.projectMembers.add(new MProjectMember(projectMember));
                }
            }


        }
    }

    public ProjectListItem convertToProjectListItem(ProjectListItem projectListItem) {
        if (projectListItem == null)
            projectListItem = new ProjectListItem();

        projectListItem.setObjectId(this.objectID == 0 ? null : this.objectID);
        projectListItem.setName(this.name);
        projectListItem.setManager(this.manager);
        projectListItem.setClient(this.client);
        projectListItem.setActualHoursSpent(this.hoursSpent);
        projectListItem.setTeams(this.teams);
        projectListItem.setStartDate(this.startDate);
        projectListItem.setEndDate(this.endDate);
        projectListItem.setStatus(this.status);
        projectListItem.setComplete(this.complete);
        projectListItem.setDescription(this.description);
        projectListItem.setPermission(this.permission);
        projectListItem.setLastUpdate(this.lastUpdate);
        projectListItem.setDueDate(this.dueDate);
        projectListItem.setHeadCount(this.headCount);
        projectListItem.setTaskCount(this.taskCount);

        return projectListItem;
    }

    public EditProject convertToEditProject(EditProject projectListItem) {
        if (projectListItem == null)
            projectListItem = new EditProject();

        projectListItem.setObjectId(this.objectID == 0 ? null : this.objectID);
        projectListItem.setName(this.name);
        projectListItem.setManagerName(this.managerName);
        projectListItem.setClientName(this.client);
        projectListItem.setStartDate(this.startDate);
        projectListItem.setEndDate(this.endDate);
        projectListItem.setDescription(this.description);
        projectListItem.setPermission(this.permission);
        projectListItem.setLastUpdate(this.lastUpdate);
        projectListItem.setDueDate(this.dueDate);
        projectListItem.setClientId(this.clientID);
        projectListItem.setManagerId(this.managerID);
        projectListItem.setBackupManagerId(this.backupManagerID);
        projectListItem.setBackupManagerIDs(this.backupManagerIDs);
        projectListItem.setStatusId(this.statusID);

        if (this.projectMembers != null) {
            List<ProjectMember> projectMembers = new ArrayList<>();
            for (MProjectMember mProjectMember : this.projectMembers) {
                ProjectMember projectMember = new ProjectMember();
                if (MProjectMember.convert(projectMember, mProjectMember, false)) {
                    projectMembers.add(projectMember);
                }
            }
            projectListItem.setMembers(projectMembers.toArray(new ProjectMember[]{}));
        }

        return projectListItem;
    }

    public ProjectSingleItem convertToProjectSingleItem(ProjectSingleItem projectListItem) {
        if (projectListItem == null)
            projectListItem = new ProjectSingleItem();
        projectListItem.setName(this.name);

        projectListItem.setClientId(this.clientID == null ? 0 : this.clientID);
        projectListItem.setStartDate(this.startDate);
        projectListItem.setEndDate(this.endDate);
        if (this.managerID != null)
            projectListItem.setManagerId(this.managerID);
        if (this.statusID != null)
            projectListItem.setStatusId(this.statusID);
        projectListItem.setDescription(this.description);
        if (this.backupManagerID != null) {
            projectListItem.setBackupManagerId(this.backupManagerID);
            ArrayList<Integer> Ids = new ArrayList<>();
            Ids.add(this.backupManagerID);
            projectListItem.setBackupManagerIDs(Ids);
        }
        projectListItem.setParentId(this.parentID);
        if (this.projectMembers != null) {
            List<ProjectMember> projectMembers = new ArrayList<>();
            for (MProjectMember mProjectMember : this.projectMembers) {
                ProjectMember projectMember = new ProjectMember();
                if (MProjectMember.convert(projectMember, mProjectMember, false)) {
                    projectMembers.add(projectMember);
                }
            }
            projectListItem.setProjectMembers(projectMembers.toArray(new ProjectMember[]{}));
        }


        return projectListItem;
    }

    public static Boolean convert(MProjectListItem mProjectListItem, ProjectListItem projectListItem, boolean toProjectListItem) {
        if (projectListItem == null || mProjectListItem == null)
            return null;
        try {
            if (toProjectListItem) {
                projectListItem.setObjectId(mProjectListItem.getObjectID() == null || mProjectListItem.getObjectID() == 0 ? null : mProjectListItem.getObjectID());
                projectListItem.setName(mProjectListItem.getName());
                projectListItem.setManager(mProjectListItem.getManager());
                projectListItem.setClient(mProjectListItem.getClient());
                projectListItem.setActualHoursSpent(mProjectListItem.getHoursSpent());
                projectListItem.setTeams(mProjectListItem.getTeams());
                projectListItem.setStartDate(mProjectListItem.getStartDate());
                projectListItem.setEndDate(mProjectListItem.getEndDate());
                projectListItem.setStatus(mProjectListItem.getStatus());
                projectListItem.setComplete(mProjectListItem.getComplete());
                projectListItem.setDescription(mProjectListItem.getDescription());
                projectListItem.setPermission(mProjectListItem.getPermission());
                projectListItem.setLastUpdate(mProjectListItem.getLastUpdate());
                projectListItem.setDueDate(mProjectListItem.getDueDate());
                projectListItem.setHeadCount(mProjectListItem.getHeadCount());
                projectListItem.setTaskCount(mProjectListItem.getTaskCount());
            } else {
                mProjectListItem.setObjectID(projectListItem.getObjectId());
                mProjectListItem.setName(projectListItem.getName());
                mProjectListItem.setManager(projectListItem.getManager());
                mProjectListItem.setClient(projectListItem.getClient());
                mProjectListItem.setHoursSpent(projectListItem.getActualHoursSpent());
                mProjectListItem.setTeams(projectListItem.getTeams());
                mProjectListItem.setStartDate(projectListItem.getStartDate());
                mProjectListItem.setEndDate(projectListItem.getEndDate());
                mProjectListItem.setStatus(projectListItem.getStatus());
                mProjectListItem.setComplete(projectListItem.getComplete());
                mProjectListItem.setDescription(projectListItem.getDescription());
                mProjectListItem.setPermission(projectListItem.getPermission());
                mProjectListItem.setLastUpdate(projectListItem.getLastUpdate());
                mProjectListItem.setDueDate(projectListItem.getDueDate());
                mProjectListItem.setHeadCount(projectListItem.getHeadCount());
                mProjectListItem.setTaskCount(projectListItem.getTaskCount());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean convert(MProjectListItem mProjectListItem, EditProject projectListItem, boolean toEditProject) {
        if (projectListItem == null || mProjectListItem == null)
            return null;
        try {
            if (toEditProject) {
                projectListItem.setObjectId(mProjectListItem.getObjectID() == null || mProjectListItem.getObjectID() == 0 ? null : mProjectListItem.getObjectID());
                projectListItem.setName(mProjectListItem.getName());
                projectListItem.setManagerName(mProjectListItem.getManager());
                projectListItem.setClientName(mProjectListItem.getClient());
                projectListItem.setStartDate(mProjectListItem.getStartDate());
                projectListItem.setEndDate(mProjectListItem.getEndDate());
                projectListItem.setDescription(mProjectListItem.getDescription());
                projectListItem.setPermission(mProjectListItem.getPermission());
                projectListItem.setLastUpdate(mProjectListItem.getLastUpdate());
                projectListItem.setDueDate(mProjectListItem.getDueDate());
                projectListItem.setClientId(mProjectListItem.getClientID());
                projectListItem.setManagerId(mProjectListItem.getManagerID());
                projectListItem.setBackupManagerId(mProjectListItem.getBackupManagerID());
                projectListItem.setBackupManagerIDs(mProjectListItem.getBackupManagerIDs());
                projectListItem.setStatusId(mProjectListItem.getStatusID());

            } else {
                mProjectListItem.setObjectID(projectListItem.getObjectId());
                mProjectListItem.setName(projectListItem.getName());
                mProjectListItem.setManager(projectListItem.getManagerName());
                mProjectListItem.setClient(projectListItem.getClientName());
                mProjectListItem.setStartDate(projectListItem.getStartDate());
                mProjectListItem.setEndDate(projectListItem.getEndDate());
                mProjectListItem.setDescription(projectListItem.getDescription());
                mProjectListItem.setPermission(projectListItem.getPermission());
                mProjectListItem.setLastUpdate(projectListItem.getLastUpdate());
                mProjectListItem.setDueDate(projectListItem.getDueDate());
                mProjectListItem.setClientID(projectListItem.getClientId());
                mProjectListItem.setManagerID(projectListItem.getManagerId());
                mProjectListItem.setBackupManagerID(projectListItem.getBackupManagerId());
                mProjectListItem.setBackupManagerIDs(projectListItem.getBackupManagerIDs());
                mProjectListItem.setStatusID(projectListItem.getStatusId());

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean convert(MProjectListItem mProjectListItem, ProjectSingleItem projectListItem, boolean toProjectSingleItem) {
        if (projectListItem == null || mProjectListItem == null)
            return null;
        try {

            if (toProjectSingleItem) {
                projectListItem.setName(mProjectListItem.getName());
                if (mProjectListItem.getClientID() != null)
                    projectListItem.setClientId(mProjectListItem.getClientID());
                projectListItem.setStartDate(mProjectListItem.getStartDate());
                projectListItem.setEndDate(mProjectListItem.getEndDate());
                if (mProjectListItem.getStatusID() != null)
                    projectListItem.setStatusId(mProjectListItem.getStatusID());
                projectListItem.setDescription(mProjectListItem.getDescription());
                if (mProjectListItem.getBackupManagerID() != null)
                    projectListItem.setBackupManagerId(mProjectListItem.getBackupManagerID());
                mProjectListItem.setBackupManagerIDs(mProjectListItem.getBackupManagerIDs());
                projectListItem.setParentId(mProjectListItem.getParentID());
                if (mProjectListItem.getProjectMembers() != null) {
                    List<ProjectMember> projectMembers = new ArrayList<>();
                    for (MProjectMember mProjectMember : mProjectListItem.getProjectMembers()) {
                        ProjectMember projectMember = new ProjectMember();
                        if (MProjectMember.convert(projectMember, mProjectMember, false)) {
                            projectMembers.add(projectMember);
                        }
                    }
                    projectListItem.setProjectMembers(projectMembers.toArray(new ProjectMember[]{}));
                }
            } else {
                mProjectListItem.setName(projectListItem.getName());
                mProjectListItem.setClientID(projectListItem.getClientId());
                mProjectListItem.setStartDate(projectListItem.getStartDate());
                mProjectListItem.setEndDate(projectListItem.getEndDate());
                mProjectListItem.setStatusID(projectListItem.getStatusId());
                mProjectListItem.setDescription(projectListItem.getDescription());
                if (projectListItem.getProjectMembers() != null) {
                    List<MProjectMember> projectMemberList = new ArrayList<>();
                    for (ProjectMember projectMember : projectListItem.getProjectMembers()) {
                        projectMemberList.add(new MProjectMember(projectMember));
                    }
                    mProjectListItem.setProjectMembers(projectMemberList);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    public ArrayList<Integer> getBackupManagerIDs() {
        return backupManagerIDs;
    }

    public void setBackupManagerIDs(ArrayList<Integer> backupManagerIDs) {
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

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public List<MProjectMember> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<MProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
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

    public String getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(String hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getHeadCount() {
        return headCount;
    }

    public void setHeadCount(Integer headCount) {
        this.headCount = headCount;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }
}
