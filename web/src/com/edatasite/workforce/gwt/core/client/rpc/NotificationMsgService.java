package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by dilsh0d on 09.07.15.
 */
public interface NotificationMsgService extends RemoteService {

    //Integer getNewNotifications(Integer latestNotificationId);

    ListResult<NotificationItem> getNewNotifications();

    ListResult<NotificationItem> getNotificationsList(ListingFilterParameter filterParametrs);

    SelectItem[] getCategoriesList(Boolean isShortName);

    void updateClicked(Integer id);

    void clearAll();

    Long getNotificationsCount();

    Long getUnreadEmailsCount();

    class App {
        public static NotificationMsgServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/notificationmsg");
            return (NotificationMsgServiceAsync) target;
        }
    }
}
