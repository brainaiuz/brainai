package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 2:10:41 PM
 */
public class TaskPolicyItem implements Serializable {

    private int entityType;
    private Integer taskPolicyId;
    private Integer trusteeId;
    private String trusteeName;
    private String trusteeType;
    private String relationCode;
    private String relation;
    private String relationDescription;
    private boolean isDirectRelation;
    private boolean isForTrustee;
    private boolean isGroup;
    private String description;
    private String[] permisions;
    private TaskPermissionItem permissionItems;

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public TaskPolicyItem(Integer id) {
        this.taskPolicyId = id;
    }

    public TaskPolicyItem() {
    }

    public Integer getTaskPolicyId() {
        return taskPolicyId;
    }

    public void setTaskPolicyId(Integer taskPolicyId) {
        this.taskPolicyId = taskPolicyId;
    }

    public Integer getTrusteeId() {
        return trusteeId;
    }

    public void setTrusteeId(Integer trusteeId) {
        this.trusteeId = trusteeId;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public boolean isForTrustee() {
        return isForTrustee;
    }

    public void setForTrustee(boolean forTrustee) {
        isForTrustee = forTrustee;
    }

    public boolean isDirectRelation() {
        return isDirectRelation;
    }

    public void setDirectRelation(boolean directRelation) {
        isDirectRelation = directRelation;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getRelationDescription() {
        return relationDescription;
    }

    public void setRelationDescription(String relationDescription) {
        this.relationDescription = relationDescription;
    }

    public String getTrusteeName() {
        return trusteeName;
    }

    public void setTrusteeName(String trusteeName) {
        this.trusteeName = trusteeName;
    }

    public String getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(String trusteeType) {
        this.trusteeType = trusteeType;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getPermisions() {
        return permisions;
    }

    public void setPermisions(String[] permisions) {
        this.permisions = permisions;
    }

    public TaskPermissionItem getPermissionItems() {
        return permissionItems;
    }

    public void setPermissionItems(TaskPermissionItem permissionItems) {
        this.permissionItems = permissionItems;
    }
}
