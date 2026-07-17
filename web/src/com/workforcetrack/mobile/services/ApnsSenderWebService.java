package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;

/**
 * Created by dilsh0d on 27.08.15.
 */
public interface ApnsSenderWebService {
    void sendMessage(String deviceToken, EdsNotificationMessage edsNotificationMessage);

    void sendMessageForCookieDev(String deviceToken, String msgTitle, String msgBody);
}
