package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.CloneTaskItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: 12.08.2009
 * Time: 14:42:34
 */
public class CloneProjectItem extends Relational {
    private Integer projectId;
    private Integer parentId;
    private String projectName;
    private String projectDescription;
    private Date startDate;
    private Date dueDate;
    private boolean copyAssignments;
    private boolean copyClient;
    private boolean copyTasks;
    private boolean copyWorkstream;
    private boolean copyAssignmentsToAllProjectMembers;
    private boolean copyDocuments;
    private ProjectMember[] members;
    private CloneTaskItem taskItem;
    private Integer clientId;
    private SelectItem[] clients;
    private Integer locationId;
    private boolean copyProjectLocation;
    private Integer statusId;
    private Integer manager;
    private ArrayList<Integer> backupManagerIDs;
    private FileItem[] attachments;
    private NumberData numberData;
    private boolean billable;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    //
    private String projectSource; //project source -> copied from project, insert MSProject, convert from opportunity, and etc.
    private Integer contractId;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getProjectDescription() {

        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
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

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public ArrayList<Integer> getBackupManagerIDs() {
        return backupManagerIDs;
    }

    public void setBackupManagerIDs(ArrayList<Integer> backupManagerIDs) {
        this.backupManagerIDs = backupManagerIDs;
    }

    public ProjectMember[] getMembers() {
        return members;
    }

    public void setMembers(ProjectMember[] members) {
        this.members = members;
    }

    public boolean isCopyAssignments() {
        return copyAssignments;
    }

    public void setCopyAssignments(boolean copyAssignments) {
        this.copyAssignments = copyAssignments;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public boolean isCopyProjectLocation() {
        return copyProjectLocation;
    }

    public void setCopyProjectLocation(boolean copyProjectLocation) {
        this.copyProjectLocation = copyProjectLocation;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isCopyClient() {
        return copyClient;
    }

    public void setCopyClient(boolean copyClient) {
        this.copyClient = copyClient;
    }

    public boolean isCopyTasks() {
        return copyTasks;
    }

    public void setCopyTasks(boolean copyTasks) {
        this.copyTasks = copyTasks;
    }

    public boolean isCopyDocuments() {
        return copyDocuments;
    }

    public void setCopyDocuments(boolean copyDocuments) {
        this.copyDocuments = copyDocuments;
    }

    public CloneTaskItem getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(CloneTaskItem taskItem) {
        this.taskItem = taskItem;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isCopyWorkstream() {
        return copyWorkstream;
    }

    public boolean isCopyAssignmentsToAllProjectMembers() {
        return copyAssignmentsToAllProjectMembers;
    }

    public void setCopyWorkstream(boolean copyWorkstream) {
        this.copyWorkstream = copyWorkstream;
    }

    public void setCopyAssignmentsToAllProjectMembers(boolean copyAssignmentsToAllProjectMembers) {
        this.copyAssignmentsToAllProjectMembers = copyAssignmentsToAllProjectMembers;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
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

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
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
            member.setCheck(info.isSelected());
            member.setWageRate(info.getWageRate());
            member.setClientChargeRate(info.getClientChargeRate());
            member.setWorkloadPercentage(info.getWorkloadPercentage());
            members.add(member);
        }
        setMembers(members.toArray(new ProjectMember[members.size()]));
    }

    @Override
    public Integer getRelationID() {
        return getProjectId();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_PROJECT;
    }

    @Override
    public String getRelationName() {
        return getProjectName();
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }
}