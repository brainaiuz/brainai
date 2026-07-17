package com.edatasite.workforce.gwt.core.server.db.notification;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessageSetting;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Map;

/**
 * Created by dilsh0d on 31.10.15.
 */
public interface NotificationMsgSettingManager extends Manager<EdsNotificationMessageSetting> {
    Map<NotificationTypeEnum, EdsNotificationMessageSetting> getNotificationSettingList();

    EdsNotificationMessageSetting getNotificationEventByType(NotificationTypeEnum notificationTypeEnum);

    boolean isEnableNotification(NotificationTypeEnum entityType);
}
