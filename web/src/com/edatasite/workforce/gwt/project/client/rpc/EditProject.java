package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditProject extends Relational implements UserGrant {

    private Integer objectId;
    private Integer parentId;
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
    private Integer managerId;
    private Integer backupManagerId;
    private ArrayList<Integer> backupManagerIDs;
    private Integer clientId;
    private String clientContactEmail;
    private Integer clientContactId;
    private Integer statusId;
    private Integer locationId;
    private boolean isCopyNewEmployeesToProjectTasks;
    private ProjectMember[] members;
    private ProjectPosition[] projectPositions;
    private boolean changeTaskStatus = false;
    private FileItem[] attachments;
    private String complete;
    private ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
	private SelectItem[] priorities;
    private NumberData numberData;

    private boolean crmActivityProject;
    private boolean isDefaultProject;

    private int permission;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private String hoursSpent;
    private Long taskCount;
    private HashSet<String> permissions;
    private EmployeeAssignmentEnum employeeAssignment;
    private SelectItem[] clients;
    private boolean billable;
    private Set<SelectItem> managers;
    private String department;
    private ArrayList<SelectItem> owners = new ArrayList<>();
    private String ownersId;
    private List<HistoryListItem> notes;
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private List<CheckInLocationItem> checkInLocations;

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setPermission(int b) {
        permission = b;
    }

    public int getPermission() {
        return permission;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
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

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public ProjectMember[] getMembers() {
        return members;
    }

    public void setMembers(ProjectMember[] members) {
        this.members = members;
    }

    public boolean isCopyNewEmployeesToProjectTasks() {
        return isCopyNewEmployeesToProjectTasks;
    }

    public void setCopyNewEmployeesToProjectTasks(boolean copyNewEmployeesToProjectTasks) {
        isCopyNewEmployeesToProjectTasks = copyNewEmployeesToProjectTasks;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public boolean isChangeTaskStatus() {
        return changeTaskStatus;
    }

    public void setChangeTaskStatus(boolean changeTaskStatus) {
        this.changeTaskStatus = changeTaskStatus;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complate) {
        this.complete = complate;
    }

    public ArrayList<CalendarEventReminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<CalendarEventReminder> reminders) {
        this.reminders = reminders;
    }

	public SelectItem[] getPriorities() {
		return priorities;
	}

	public void setPriorities(SelectItem[] priorities) {
		this.priorities = priorities;
	}

	public void setProjectMemberFromTreeInfo(ArrayList<KpiTreeInfo> projectSelectedEmployees) {
        ProjectMember member;
        ArrayList<ProjectMember> members = new ArrayList<>();
        for (KpiTreeInfo info : projectSelectedEmployees) {
            member = new ProjectMember();
            member.setId(info.getId());
            member.setName(info.getName());
            member.setDefaulDepartmentId(info.getDepartmentId());
            member.setDepartmentId(info.getDepartmentId());
            member.setPosititon(info.getPositionName());
            member.setCheck(info.isSelected());
            member.setWageRate(info.getWageRate());
            member.setClientChargeRate(info.getClientChargeRate());
            member.setWorkloadPercentage(info.getWorkloadPercentage());
            member.setPositionId(info.getPositionId());
            member.setContractStart(info.getContractStart() != null ? info.getContractStart() : null);
            member.setContractEnd(info.getContractEnd() != null ? info.getContractEnd() : null);
            member.setProjectEmployeeId(info.getProjectEmployeeId());
            member.setUnit(info.getUnit());
            members.add(member);
        }
        setMembers(members.toArray(new ProjectMember[members.size()]));
    }

    public String getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(String hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
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

    public String getClientContactEmail() {
        return clientContactEmail;
    }

    public void setClientContactEmail(String clientContactEmail) {
        this.clientContactEmail = clientContactEmail;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

	public HashSet<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashSet<String> permissions) {
		this.permissions = permissions;
	}

    public EmployeeAssignmentEnum getEmployeeAssignment() {
        return employeeAssignment;
    }

    public void setEmployeeAssignment(EmployeeAssignmentEnum employeeAssignment) {
        this.employeeAssignment = employeeAssignment;
    }

    public ProjectPosition[] getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(ProjectPosition[] projectPositions) {
        this.projectPositions = projectPositions;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public Set<SelectItem> getManagers() {
        return managers;
    }

    public void setManagers(Set<SelectItem> managers) {
        this.managers = managers;
    }

    public boolean isCrmActivityProject() {
        return crmActivityProject;
    }

    public void setCrmActivityProject(boolean crmActivityProject) {
        this.crmActivityProject = crmActivityProject;
    }

    public boolean isDefaultProject() {
        return isDefaultProject;
    }

    public void setDefaultProject(boolean defaultProject) {
        isDefaultProject = defaultProject;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<SelectItem> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<SelectItem> owners) {
        this.owners = owners;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
    }

    public List<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(List<HistoryListItem> notes) {
        this.notes = notes;
    }

    public List<CheckInLocationItem> getCheckInLocations() {
        return checkInLocations;
    }

    public void setCheckInLocations(List<CheckInLocationItem> checkInLocations) {
        this.checkInLocations = checkInLocations;
    }
}
