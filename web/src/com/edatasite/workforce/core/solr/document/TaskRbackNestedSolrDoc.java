package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 24.09.2020 10:45.
 */
public class TaskRbackNestedSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

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
    @Indexed(name = "trusteeType", type = "pint", stored = false, required = true)
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

    @Field("rank")
    private Integer rank;

    @Field("taskPercentCompleted")
    private Float taskPercentCompleted;

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

    public Integer getTaskAssigneeStatusId() {
        return taskAssigneeStatusId;
    }

    public void setTaskAssigneeStatusId(Integer taskAssigneeStatusId) {
        this.taskAssigneeStatusId = taskAssigneeStatusId;
    }

    public Integer getTaskProjectClientId() {
        return taskProjectClientId;
    }

    public void setTaskProjectClientId(Integer taskProjectClientId) {
        this.taskProjectClientId = taskProjectClientId;
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
