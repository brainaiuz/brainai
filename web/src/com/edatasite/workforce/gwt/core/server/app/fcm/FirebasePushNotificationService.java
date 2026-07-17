package com.edatasite.workforce.gwt.core.server.app.fcm;

/**
 * Created by Dilsh0d Madrahimov on 8/20/2017.
 */
public interface FirebasePushNotificationService {
    void pushTestNotification();

    boolean pushDataNotification(String token, String msgTitle, String msgBody);
}
