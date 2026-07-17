package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by dilsh0d on 09.07.15.
 */
public interface NotificationMsgServiceAsync {

    //void getNewNotifications(Integer latestNotificationId, AsyncCallback<Integer> callback);

    void getNewNotifications(AsyncCallback<ListResult<NotificationItem>> callback);

    void getNotificationsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<NotificationItem>> callback);

    void getCategoriesList(Boolean isShortName,AsyncCallback<SelectItem[]> callback);

    void updateClicked(Integer id, AsyncCallback<Void> callback);

    void clearAll(AsyncCallback<Void> callback);

    void getNotificationsCount(AsyncCallback<Long> callback);

    void getUnreadEmailsCount(AsyncCallback<Long> callback);

}
