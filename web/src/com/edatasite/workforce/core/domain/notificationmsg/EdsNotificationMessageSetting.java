package com.edatasite.workforce.core.domain.notificationmsg;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;

import javax.persistence.*;

/**
 * Created by dilsh0d on 23.10.15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "notification_msg_setting")
public class EdsNotificationMessageSetting extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private NotificationTypeEnum eventType;

    @Column(name = "show")
    private Boolean isShow = Boolean.FALSE;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public NotificationTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(NotificationTypeEnum eventType) {
        this.eventType = eventType;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }
}
