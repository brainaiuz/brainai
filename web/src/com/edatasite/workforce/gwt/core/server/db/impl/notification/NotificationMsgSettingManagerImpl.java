package com.edatasite.workforce.gwt.core.server.db.impl.notification;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessageSetting;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgSettingManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dilsh0d on 31.10.15.
 */
@Repository("notificationMsgSettingManager")
public class NotificationMsgSettingManagerImpl extends BaseManager<EdsNotificationMessageSetting> implements NotificationMsgSettingManager {

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public NotificationMsgSettingManagerImpl() {
        super(EdsNotificationMessageSetting.class);
    }

    @Override
    public Map<NotificationTypeEnum, EdsNotificationMessageSetting> getNotificationSettingList() {
        List<EdsNotificationMessageSetting> resultList = find("SELECT nms FROM EdsNotificationMessageSetting nms");
        Map<NotificationTypeEnum, EdsNotificationMessageSetting> resultMap = new HashMap<>();
        for (EdsNotificationMessageSetting edsMessageSetting : resultList) {
            resultMap.put(edsMessageSetting.getEventType(), edsMessageSetting);
        }
        return resultMap;
    }

    @Override
    public EdsNotificationMessageSetting getNotificationEventByType(NotificationTypeEnum notificationTypeEnum) {

        if (notificationTypeEnum == null) {
            return null;
        }
        List<EdsNotificationMessageSetting> resultList = find("SELECT nms FROM EdsNotificationMessageSetting nms WHERE nms.eventType=?", notificationTypeEnum);

        if (resultList == null || resultList.size() == 0) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public boolean isEnableNotification(NotificationTypeEnum entityType) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SERVER_PUSH_NOTIFICATION)) {
            EdsNotificationMessageSetting result = getNotificationEventByType(entityType);
            if (result == null) {
                return entityType != null ? entityType.isDefaultSentEvent() : false;
            }
            return result.getIsShow();
        }
        return false;
    }
}
