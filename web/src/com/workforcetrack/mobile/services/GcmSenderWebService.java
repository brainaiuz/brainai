package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;

/**
 * Created by dilsh0d on 26.08.15.
 */
public interface GcmSenderWebService {

    void sendMessage(String deviceToken, EdsNotificationMessage edsNotificationMessage);
}
