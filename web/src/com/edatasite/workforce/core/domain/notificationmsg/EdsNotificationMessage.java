package com.edatasite.workforce.core.domain.notificationmsg;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dilsh0d on 09.07.15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "notification_msg",
        indexes ={@Index(columnList = "viewer_userid", name = "viewer_userid_index")} )
public class EdsNotificationMessage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "subject")
    private String subject;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum entityType;

    @Column(name = "action_on_entity")
    @Enumerated(EnumType.STRING)
    private ActionOnEntityEnum actionOnEntity;

    @Column(name = "viewer_userid")
    private Integer viewerUserID;

    @Column(name = "actor_userid")
    private Integer actorUserID;

    @Column(name = "read")
    private Boolean read = Boolean.FALSE;

    @Column(name = "clicked")
    private Boolean clicked = Boolean.FALSE;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "date")
    private Date date;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public NotificationTypeEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(NotificationTypeEnum entityType) {
        this.entityType = entityType;
    }

    public ActionOnEntityEnum getActionOnEntity() {
        return actionOnEntity;
    }

    public void setActionOnEntity(ActionOnEntityEnum actionOnEntity) {
        this.actionOnEntity = actionOnEntity;
    }

    public Integer getViewerUserID() {
        return viewerUserID;
    }

    public void setViewerUserID(Integer viewerUserID) {
        this.viewerUserID = viewerUserID;
    }

    public Integer getActorUserID() {
        return actorUserID;
    }

    public void setActorUserID(Integer actorUserID) {
        this.actorUserID = actorUserID;
    }

    public Boolean getRead() {
        return read != null ? read : false;
    }

    public void setRead(Boolean readed) {
        this.read = readed;
    }

    public Boolean getClicked() {
        return clicked != null ? clicked : false;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public NotificationItem getNewNotificationItem() {
        NotificationItem item = new NotificationItem();
        item.setId(getObjectID());
        item.setEntityType(getEntityType().name());
        item.setModuleCode(getEntityType().getModule());
        if (NotificationTypeEnum.CustomFormItem.equals(getEntityType()) && getEntityID() != null) {
            CustomFormItemManager customFormItemManager = (CustomFormItemManager) ApplicationContextProvider.applicationContext.getBean("customFormItemManager");
            EdsCustomFormItems customFormItems = customFormItemManager.get(getEntityID());
            if (customFormItems != null) {
                item.setModuleCode(customFormItems.getCustomForm().getProperty().getModuleCode());
            }
        }
        item.setDate(getDate());
        return item;
    }

    public NotificationItem getNotificationItem() {
        NotificationItem item = new NotificationItem();
        item.setId(getObjectID());
        item.setEntityId(getEntityID());
        item.setEntityType(getEntityType().name());
        if (getActionOnEntity() != null) {
            item.setActionOnEntity(getActionOnEntity().name());
        }
        item.setViewerUserId(getViewerUserID());
        item.setActorUserId(getActorUserID());
        item.setClicked(getClicked());
        item.setRead(getRead());
        item.setDate(getDate());
        return item;
    }
}
