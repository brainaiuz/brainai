package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@SolrDocument(collection = "taskCore")
public class TaskSolrDoc extends RelationBaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("taskId")
    private Integer taskId;

    @Field("taskNumber")
    private String taskNumber;

    @Field("predecessorTaskStatus")
    private String predecessorTaskStatus;

    @Field("taskStatus")
    private String taskStatus;

    @Field("taskStatusId")
    private Integer taskStatusId;

    @Field("taskStatusIdCode")
    @Indexed(name = "taskStatusIdCode", type = "string", stored = false)
    private String taskStatusIdCode;

    @Field("taskStatusIdCodeName")
    @Indexed(name = "taskStatusIdCodeName", type = "string", stored = false)
    private String taskStatusIdCodeName;

    @Field("taskStatusCode")
    private String taskStatusCode;

    @Field("taskStatusSorder")
    @Indexed(name = "taskStatusSorder", type = "pint", stored = false)
    private Integer taskStatusSorder;

    @Field("taskProjectManagerName")
    private String taskProjectManagerName;

    @Field("taskProjectManagerId")
    private Integer taskProjectManagerId;

    @Field("taskProjectManagerIdName")
    @Indexed(name = "taskProjectManagerIdName", type = "string", stored = false)
    private String taskProjectManagerIdName;

    @Field("taskPriority")
    private String taskPriority;

    @Field("taskPriorityId")
    @Indexed(name = "taskPriorityId", type = "pint", stored = false)
    private Integer taskPriorityId;

    @Field("taskPriorityIdCode")
    @Indexed(name = "taskPriorityIdCode", type = "string", stored = false)
    private String taskPriorityIdCode;

    @Field("taskPriorityIdCodeName")
    @Indexed(name = "taskPriorityIdCodeName", type = "string", stored = false)
    private String taskPriorityIdCodeName;

    @Field("taskPriorityCode")
    private String taskPriorityCode;

    @Field("taskPrioritySorder")
    private Integer taskPrioritySorder;

    @Field("taskType")
    private String taskType;

    @Field("taskTypeId")
    private Integer taskTypeId;

    @Field("taskTypeCode")
    private String taskTypeCode;

    @Field("taskTypeIdCodeName")
    @Indexed(name = "taskTypeIdCodeName", type = "string", stored = false)
    private String taskTypeIdCodeName;

    @Field("creationDate")
    private Date creationDate;

    @Field("startDate")
    private Date startDate;

    @Field("actualStartDate")
    private Date actualStartDate;

    @Field("endDate")
    private Date endDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("lastUpdateDate")
    private Date lastUpdateDate;

    @Field("taskAmount")
    private Double taskAmount;

    @Field("permissions")
    @Indexed(name = "permissions", type = "strings")
    private List<String> permissions = new ArrayList<>();

    @Field("viewers")
    @Indexed(name = "viewers", type = "strings", stored = false)
    private List<String> viewers = new ArrayList<>();

    @Field("assigneeNames")
    @Indexed(name = "assigneeNames", type = "strings", stored = false)
    private List<String> assigneeNames = new ArrayList<>();

    @Field("taskName")
    private String taskName;

    @Field("taskDescription")
    private String taskDescription;

    @Field("lastModifiedBy")
    private String lastModifiedBy;

    @Field("taskCreator")
    private String taskCreator;

    @Field("taskCreatorId")
    private Integer taskCreatorId;

    @Field("taskProjectName")
    private String taskProjectName;

    @Field("taskProjectNumber")
    private String taskProjectNumber;

    @Field("taskProjectId")
    private Integer taskProjectId;

    @Field("taskProjectIdName")
    @Indexed(name = "taskProjectIdName", type = "string", stored = false)
    private String taskProjectIdName;

    @Field("taskProjectMultiClientName")
    @Indexed(name = "taskProjectMultiClientName", type = "strings")
    private List<String> taskProjectMultiClientName = new ArrayList<>();

    @Field("taskProjectMultiClientId")
    @Indexed(name = "taskProjectMultiClientId", type = "pints", stored = false)
    private List<Integer> taskProjectMultiClientId = new ArrayList<>();

    @Field("taskProjectMultiClientIdName")
    @Indexed(name = "taskProjectMultiClientIdName", type = "strings", stored = false)
    private List<String> taskProjectMultiClientIdName = new ArrayList<>();

    @Field("taskWorkstreamName")
    private String taskWorkstreamName;

    @Field("taskWorkstreamId")
    private Integer taskWorkstreamId;

    @Field("taskWorkstreamIdName")
    @Indexed(name = "taskWorkstreamIdName", type = "strings", stored = false)
    private String taskWorkstreamIdName;

    @Field("kanbanOrder")
    @Indexed(name = "kanbanOrder", type = "strings")
    private Long kanbanOrder;

    @Field("rank")
    private Integer rank;

    @Field("taskPercentCompleted")
    private Float taskPercentCompleted;

    @Field("userId")
    @Indexed(name = "userId", type = "pint", stored = false)
    private Integer userId;

    @Field("userIdName")
    @Indexed(name = "userIdName", type = "string", stored = false)
    private String userIdName;

    @Field("groupId")
    @Indexed(name = "groupId", type = "pint", stored = false)
    private Integer groupId;

    @Field("trusteeType")
    @Indexed(name = "trusteeType", type = "pint", stored = false)
    private Integer trusteeType;

    @Field("assigneeId")
    private Integer assigneeId;

    @Field("taskAssigneeStatus")
    private String taskAssigneeStatus;

    @Field("taskAssigneeStatusId")
    @Indexed(name = "taskAssigneeStatusId", type = "pint", stored = false)
    private Integer taskAssigneeStatusId;

    @Field("taskAssigneeStatusIdCode")
    @Indexed(name = "taskAssigneeStatusIdCode", type = "string", stored = false)
    private String taskAssigneeStatusIdCode;

    @Field("taskAssigneeStatusIdCodeName")
    @Indexed(name = "taskAssigneeStatusIdCodeName", type = "string", stored = false)
    private String taskAssigneeStatusIdCodeName;

    @Field("taskAssigneeStatusCode")
    private String taskAssigneeStatusCode;

    @Field("estimatedTime")
    private Integer estimatedTime;

    @Field("relationships")
    @Indexed(name = "relationships", type = "strings")
    private List<String> relationships = new ArrayList<>();

    @Field("taskProjectClientName")
    private String taskProjectClientName;

    @Field("taskProjectClientId")
    @Indexed(name = "taskProjectClientId", type = "pint", stored = false)
    private Integer taskProjectClientId;

    @Field("taskProjectClientIdName")
    @Indexed(name = "taskProjectClientIdName", type = "string", stored = false)
    private String taskProjectClientIdName;

    @Field("taskUserDepartmentName")
    private String taskUserDepartmentName;

    @Field("taskUserDepartmentId")
    @Indexed(name = "taskUserDepartmentId", type = "pint", stored = false)
    private Integer taskUserDepartmentId;

    @Field("taskUserDepartmentIdName")
    @Indexed(name = "taskUserDepartmentIdName", type = "string", stored = false)
    private String taskUserDepartmentIdName;

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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getPredecessorTaskStatus() {
        return predecessorTaskStatus;
    }

    public void setPredecessorTaskStatus(String predecessorTaskStatus) {
        this.predecessorTaskStatus = predecessorTaskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(Integer taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getTaskStatusIdCode() {
        return taskStatusIdCode;
    }

    public void setTaskStatusIdCode(String taskStatusIdCode) {
        this.taskStatusIdCode = taskStatusIdCode;
    }

    public String getTaskStatusIdCodeName() {
        return taskStatusIdCodeName;
    }

    public void setTaskStatusIdCodeName(String taskStatusIdCodeName) {
        this.taskStatusIdCodeName = taskStatusIdCodeName;
    }

    public String getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskStatusCode(String taskStatusCode) {
        this.taskStatusCode = taskStatusCode;
    }

    public String getTaskProjectManagerName() {
        return taskProjectManagerName;
    }

    public void setTaskProjectManagerName(String taskProjectManagerName) {
        this.taskProjectManagerName = taskProjectManagerName;
    }

    public Integer getTaskProjectManagerId() {
        return taskProjectManagerId;
    }

    public void setTaskProjectManagerId(Integer taskProjectManagerId) {
        this.taskProjectManagerId = taskProjectManagerId;
    }

    public String getTaskProjectManagerIdName() {
        return taskProjectManagerIdName;
    }

    public void setTaskProjectManagerIdName(String taskProjectManagerIdName) {
        this.taskProjectManagerIdName = taskProjectManagerIdName;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Integer getTaskStatusSorder() {
        return taskStatusSorder;
    }

    public void setTaskStatusSorder(Integer taskStatusSorder) {
        this.taskStatusSorder = taskStatusSorder;
    }

    public Integer getTaskPriorityId() {
        return taskPriorityId;
    }

    public void setTaskPriorityId(Integer taskPriorityId) {
        this.taskPriorityId = taskPriorityId;
    }

    public Integer getTaskPrioritySorder() {
        return taskPrioritySorder;
    }

    public void setTaskPrioritySorder(Integer taskPrioritySorder) {
        this.taskPrioritySorder = taskPrioritySorder;
    }

    public String getTaskPriorityIdCode() {
        return taskPriorityIdCode;
    }

    public void setTaskPriorityIdCode(String taskPriorityIdCode) {
        this.taskPriorityIdCode = taskPriorityIdCode;
    }

    public String getTaskPriorityIdCodeName() {
        return taskPriorityIdCodeName;
    }

    public void setTaskPriorityIdCodeName(String taskPriorityIdCodeName) {
        this.taskPriorityIdCodeName = taskPriorityIdCodeName;
    }

    public String getTaskPriorityCode() {
        return taskPriorityCode;
    }

    public void setTaskPriorityCode(String taskPriorityCode) {
        this.taskPriorityCode = taskPriorityCode;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(Integer taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(String taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public String getTaskTypeIdCodeName() {
        return taskTypeIdCodeName;
    }

    public void setTaskTypeIdCodeName(String taskTypeIdCodeName) {
        this.taskTypeIdCodeName = taskTypeIdCodeName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Double getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(Double taskAmount) {
        this.taskAmount = taskAmount;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getViewers() {
        return viewers;
    }

    public void setViewers(List<String> viewers) {
        this.viewers = viewers;
    }

    public List<String> getAssigneeNames() {
        return assigneeNames;
    }

    public void setAssigneeNames(List<String> assigneeNames) {
        this.assigneeNames = assigneeNames;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(String taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Integer getTaskCreatorId() {
        return taskCreatorId;
    }

    public void setTaskCreatorId(Integer taskCreatorId) {
        this.taskCreatorId = taskCreatorId;
    }

    public String getTaskProjectName() {
        return taskProjectName;
    }

    public void setTaskProjectName(String taskProjectName) {
        this.taskProjectName = taskProjectName;
    }

    public String getTaskProjectNumber() {
        return taskProjectNumber;
    }

    public void setTaskProjectNumber(String taskProjectNumber) {
        this.taskProjectNumber = taskProjectNumber;
    }

    public Integer getTaskProjectId() {
        return taskProjectId;
    }

    public void setTaskProjectId(Integer taskProjectId) {
        this.taskProjectId = taskProjectId;
    }

    public String getTaskProjectIdName() {
        return taskProjectIdName;
    }

    public void setTaskProjectIdName(String taskProjectIdName) {
        this.taskProjectIdName = taskProjectIdName;
    }

    public List<String> getTaskProjectMultiClientName() {
        return taskProjectMultiClientName;
    }

    public void setTaskProjectMultiClientName(List<String> taskProjectMultiClientName) {
        this.taskProjectMultiClientName = taskProjectMultiClientName;
    }

    public List<Integer> getTaskProjectMultiClientId() {
        return taskProjectMultiClientId;
    }

    public void setTaskProjectMultiClientId(List<Integer> taskProjectMultiClientId) {
        this.taskProjectMultiClientId = taskProjectMultiClientId;
    }

    public List<String> getTaskProjectMultiClientIdName() {
        return taskProjectMultiClientIdName;
    }

    public void setTaskProjectMultiClientIdName(List<String> taskProjectMultiClientIdName) {
        this.taskProjectMultiClientIdName = taskProjectMultiClientIdName;
    }

    public String getTaskWorkstreamName() {
        return taskWorkstreamName;
    }

    public void setTaskWorkstreamName(String taskWorkstreamName) {
        this.taskWorkstreamName = taskWorkstreamName;
    }

    public Integer getTaskWorkstreamId() {
        return taskWorkstreamId;
    }

    public void setTaskWorkstreamId(Integer taskWorkstreamId) {
        this.taskWorkstreamId = taskWorkstreamId;
    }

    public String getTaskWorkstreamIdName() {
        return taskWorkstreamIdName;
    }

    public void setTaskWorkstreamIdName(String taskWorkstreamIdName) {
        this.taskWorkstreamIdName = taskWorkstreamIdName;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Float getTaskPercentCompleted() {
        return taskPercentCompleted;
    }

    public void setTaskPercentCompleted(Float taskPercentCompleted) {
        this.taskPercentCompleted = taskPercentCompleted;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(String userIdName) {
        this.userIdName = userIdName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(Integer trusteeType) {
        this.trusteeType = trusteeType;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getTaskAssigneeStatus() {
        return taskAssigneeStatus;
    }

    public void setTaskAssigneeStatus(String taskAssigneeStatus) {
        this.taskAssigneeStatus = taskAssigneeStatus;
    }

    public Integer getTaskAssigneeStatusId() {
        return taskAssigneeStatusId;
    }

    public void setTaskAssigneeStatusId(Integer taskAssigneeStatusId) {
        this.taskAssigneeStatusId = taskAssigneeStatusId;
    }

    public String getTaskAssigneeStatusIdCode() {
        return taskAssigneeStatusIdCode;
    }

    public void setTaskAssigneeStatusIdCode(String taskAssigneeStatusIdCode) {
        this.taskAssigneeStatusIdCode = taskAssigneeStatusIdCode;
    }

    public String getTaskAssigneeStatusIdCodeName() {
        return taskAssigneeStatusIdCodeName;
    }

    public void setTaskAssigneeStatusIdCodeName(String taskAssigneeStatusIdCodeName) {
        this.taskAssigneeStatusIdCodeName = taskAssigneeStatusIdCodeName;
    }

    public String getTaskAssigneeStatusCode() {
        return taskAssigneeStatusCode;
    }

    public void setTaskAssigneeStatusCode(String taskAssigneeStatusCode) {
        this.taskAssigneeStatusCode = taskAssigneeStatusCode;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<String> relationships) {
        this.relationships = relationships;
    }

    public String getTaskProjectClientName() {
        return taskProjectClientName;
    }

    public void setTaskProjectClientName(String taskProjectClientName) {
        this.taskProjectClientName = taskProjectClientName;
    }

    public Integer getTaskProjectClientId() {
        return taskProjectClientId;
    }

    public void setTaskProjectClientId(Integer taskProjectClientId) {
        this.taskProjectClientId = taskProjectClientId;
    }

    public String getTaskProjectClientIdName() {
        return taskProjectClientIdName;
    }

    public void setTaskProjectClientIdName(String taskProjectClientIdName) {
        this.taskProjectClientIdName = taskProjectClientIdName;
    }

    public String getTaskUserDepartmentName() {
        return taskUserDepartmentName;
    }

    public void setTaskUserDepartmentName(String taskUserDepartmentName) {
        this.taskUserDepartmentName = taskUserDepartmentName;
    }

    public Integer getTaskUserDepartmentId() {
        return taskUserDepartmentId;
    }

    public void setTaskUserDepartmentId(Integer taskUserDepartmentId) {
        this.taskUserDepartmentId = taskUserDepartmentId;
    }

    public String getTaskUserDepartmentIdName() {
        return taskUserDepartmentIdName;
    }

    public void setTaskUserDepartmentIdName(String taskUserDepartmentIdName) {
        this.taskUserDepartmentIdName = taskUserDepartmentIdName;
    }
}
