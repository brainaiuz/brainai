package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dilsh0d on 27.08.15.
 */
public interface NotificationMsgServiceLocal {

    String generatedNewNotificationName(EdsNotificationMessage edsNotificationMessage, boolean isIOs);

    void updateUserToken(String deviceToken, String deviceType);

    Integer getNewNotifications(ArrayList<NotificationTypeEnum> entityTypes);

    ListResult<NotificationItem> getNewNotifications();

    ListResult<NotificationItem> getNotificationsList(ListingFilterParameter filterParametrs);

    ListResult<NotificationItem> getNotificationsList(ListingFilterParameter filterParameter, ArrayList<NotificationTypeEnum> entityTypes);

    SelectItem[] getCategoriesList(Boolean isShortName);

    void updateClicked(Integer id);

    void clearAll();

    void clearAll(ArrayList<NotificationTypeEnum> entityTypes);

    ArrayList<NotificationItem> getNotificationsByRequestType(ListingFilterParameter filterParametrs);

    HashMap<String, Integer> getNotificationCountByTypes(ListingFilterParameter fp);
}
