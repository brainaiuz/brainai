package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by dilsh0d on 26.08.15.
 */
@Service("gcmSenderWebService")
public class GcmSenderWebServiceImpl implements GcmSenderWebService {

    private static final Logger log = LoggerFactory.getLogger(GcmSenderWebServiceImpl.class);
    private final static String REGISTER_API_KEY = "AIzaSyD7Uk55BuUs1CdPTvxdUIZXJnne8330b-c";

    private final Sender sender = new Sender(REGISTER_API_KEY);

    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;

    @Override
    public void sendMessage(String deviceToken, EdsNotificationMessage edsNotificationMessage) {
        // Use this line to send message without payload data
        // Message message = new Message.Builder().build();
        JSONObject data = new JSONObject();
        data.put("objectId",edsNotificationMessage.getObjectID());
        data.put("message", edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : notificationMsgServiceLocal.generatedNewNotificationName(edsNotificationMessage, false));
        data.put("entityID", edsNotificationMessage.getEntityID());
        data.put("entityType", edsNotificationMessage.getEntityType().name());
        data.put("actionOnEntity", edsNotificationMessage.getActionOnEntity() != null ? edsNotificationMessage.getActionOnEntity().name() : "");
        data.put("viewerUserID", edsNotificationMessage.getViewerUserID());
        data.put("actorUserID", edsNotificationMessage.getActorUserID());
        data.put("date", edsNotificationMessage.getDate().getTime());


        // use this line to send message with payload data
        Message message = new Message.Builder()
                .delayWhileIdle(true)
                .addData("message", data.toString())
                .build();

        // Use this for multicast messages
        try {
            /*Result result =*/
            sender.send(message, deviceToken, 1);
            /*if (result == null) {//todo discussed with Dilshod Tajiev
                log.error("Android PUSH_NOTIFICATION_ERROR with token " + deviceToken);
            } else if (ServerUtils.isNullOrEmpty(result.getMessageId())) {
                log.error("Android PUSH_NOTIFICATION_ERROR with token " + deviceToken);
            }*/
        } catch (IOException e) {
            //log.error("Android PUSH_NOTIFICATION_ERROR with token " + deviceToken);
        }
    }
}
