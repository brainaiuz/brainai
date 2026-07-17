package com.edatasite.workforce.gwt.core.client.rpc.notification;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dilsh0d on 09.07.15.
 */
public class NotificationItem implements IsSerializable, Serializable {

    private Integer id;
    private String name;
    private String value;
    private String uniqueVal;
    private String description;
    private String moduleName;
    private String moduleCode;
    private Integer entityId;
    private String entityType;
    private String actionOnEntity;
    private String actionUrl;
    private Integer viewerUserId;
    private Integer actorUserId;
    private String actorUserName;
    private String actorUserImg;
    private String userInfo;
    private String approver;
    private boolean clicked = false;
    private Date date;
    private boolean read;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActorUserName() {
        return actorUserName;
    }

    public void setActorUserName(String actorUserName) {
        this.actorUserName = actorUserName;
    }

    public String getActorUserImg() {
        return actorUserImg;
    }

    public void setActorUserImg(String actorUserImg) {
        this.actorUserImg = actorUserImg;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getActionOnEntity() {
        return actionOnEntity;
    }

    public void setActionOnEntity(String actionOnEntity) {
        this.actionOnEntity = actionOnEntity;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Integer getViewerUserId() {
        return viewerUserId;
    }

    public void setViewerUserId(Integer viewerUserId) {
        this.viewerUserId = viewerUserId;
    }

    public Integer getActorUserId() {
        return actorUserId;
    }

    public void setActorUserId(Integer actorUserId) {
        this.actorUserId = actorUserId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getSortDate() {
        return date != null ? date : new Date();
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueVal() {
        return uniqueVal;
    }

    public void setUniqueVal(String uniqueVal) {
        this.uniqueVal = uniqueVal;
    }
}
