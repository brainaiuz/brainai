package com.edatasite.workforce.gwt.core.client.rpc.project;

import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectSingleItem extends Relational {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String timeSpentHM;
    private Date changedOn;
    private int statusId;
    private Integer managerId;
    private String managerName;
    private int backupManagerId;
    private ArrayList<Integer> backupManagerIDs;
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private Integer clientId;
    private String clientName;
    private int locationId;
    private Integer parentId;
    private ProjectPosition[] projectPositions;
    private ProjectMember[] projectMembers;
    private int[] projectMembersId;
    private FileItem[] attachments;
    private NumberData numberData;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private SelectItem[] clients;
    private boolean billable;
    private boolean fromImport;

    private EmployeeAssignmentEnum employeeAssignment;

    private Integer objectID;
    private Integer projectNumberID;
    private Integer nameID;
    private Integer descriptionID;
    private Integer startDateID;
    private Integer dueDateID;
    private Integer statusID;
    private Integer assigneeID;
    private HashMap<String, Object> customFieldsMap;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private ArrayList<CheckInLocationItem> checkInLocations;
    //
    private String projectSource; //project source -> copied from project, insert MSProject, convert from opportunity, and etc.
    private Integer contractId;
    private String ownersId;
    private List<HistoryListItem> notes;

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();


    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
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

    public ProjectMember[] getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(ProjectMember[] projectMembers) {
        this.projectMembers = projectMembers;
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

    public String getTimeSpentHM() {
        return timeSpentHM;
    }

    public void setTimeSpentHM(String timeSpentHM) {
        this.timeSpentHM = timeSpentHM;
    }

    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public int getBackupManagerId() {
        return backupManagerId;
    }

    public void setBackupManagerId(int backupManagerId) {
        this.backupManagerId = backupManagerId;
    }

    public ArrayList<Integer> getBackupManagerIDs() {
        return backupManagerIDs;
    }

    public void setBackupManagerIDs(ArrayList<Integer> backupManagerIDs) {
        this.backupManagerIDs = backupManagerIDs;
    }

    public ArrayList<CalendarEventReminder> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<CalendarEventReminder> reminder) {
        this.reminder = reminder;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int[] getProjectMembersId() {
        return projectMembersId;
    }

    public void setProjectMembersId(int[] projectMembersId) {
        this.projectMembersId = projectMembersId;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
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
            member.setPositionId(info.getPositionId());
            member.setCheck(info.isSelected());
            member.setWageRate(info.getWageRate());
            member.setClientChargeRate(info.getClientChargeRate());
            member.setWorkloadPercentage(info.getWorkloadPercentage());
            member.setContractStart(info.getContractStart() != null ? info.getContractStart() : null);
            member.setContractEnd(info.getContractEnd() != null ? info.getContractEnd() : null);
            member.setUnit(info.getUnit());
            members.add(member);
        }
        setProjectMembers(members.toArray(new ProjectMember[members.size()]));
    }

    @Override
    public Integer getRelationID() {
        return null;
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_PROJECT;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getProjectNumberID() {
        return projectNumberID;
    }

    public void setProjectNumberID(Integer projectNumberID) {
        this.projectNumberID = projectNumberID;
    }

    public Integer getNameID() {
        return nameID;
    }

    public void setNameID(Integer nameID) {
        this.nameID = nameID;
    }

    public Integer getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(Integer descriptionID) {
        this.descriptionID = descriptionID;
    }

    public Integer getStartDateID() {
        return startDateID;
    }

    public void setStartDateID(Integer startDateID) {
        this.startDateID = startDateID;
    }

    public Integer getDueDateID() {
        return dueDateID;
    }

    public void setDueDateID(Integer dueDateID) {
        this.dueDateID = dueDateID;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(Integer assigneeID) {
        this.assigneeID = assigneeID;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFields) {
        this.customFieldsMap = customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem customField : customFields) {
                if ((customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue())) || customField.getFieldDateNonConvertedValue() != null) {
                    if (customField.getColumnCode() != null) {
                        Object value = null;
                        if ((customField.getDataType().equals(CompanyCustomFieldItem.TEXT) || customField.getDataType().equals(CompanyCustomFieldItem.NUMBER)) && customField.getFieldStringValue() != null) {
                            try {
                                value = customField.getDataType().equals(CompanyCustomFieldItem.TEXT) ? customField.getFieldStringValue() : Double.valueOf(customField.getFieldStringValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (customField.getDataType().equals(CompanyCustomFieldItem.DATE) && customField.getFieldDateNonConvertedValue() != null) {
                                value = customField.getFieldDateNonConvertedValue().getNonConvertedDate();
                            }
                        }
                        if (value != null) {
                            getCustomFieldsMap().put(customField.getColumnCode(), value);
                        }
                    }
                }
            }
        }
    }

    public boolean isFromImport() {
        return fromImport;
    }

    public void setFromImport(boolean fromImport) {
        this.fromImport = fromImport;
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

    public ArrayList<CheckInLocationItem> getCheckInLocations() {
        return checkInLocations;
    }

    public void setCheckInLocations(ArrayList<CheckInLocationItem> checkInLocations) {
        this.checkInLocations = checkInLocations;
    }
}
