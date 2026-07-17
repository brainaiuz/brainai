package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dilsh0d on 27.08.15.
 */
@Service("apnsWebService")
public class ApnsSenderWebServiceImpl implements ApnsSenderWebService {
    private static final Logger log = LoggerFactory.getLogger(ApnsSenderWebServiceImpl.class);

    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;

    @Override
    public void sendMessage(String deviceToken, EdsNotificationMessage edsNotificationMessage) {
        try {
            String messageName = edsNotificationMessage.getSubject() != null ? edsNotificationMessage.getSubject() : notificationMsgServiceLocal.generatedNewNotificationName(edsNotificationMessage, true);
            PushNotificationPayload payload = new PushNotificationPayload();
            payload.addBadge(1);
            payload.addSound("default");
            payload.addCustomDictionary("message", messageName);
            payload.addCustomDictionary("id", edsNotificationMessage.getObjectID());
            payload.addCustomDictionary("entityID", edsNotificationMessage.getEntityID());
            payload.addCustomDictionary("entityType", edsNotificationMessage.getEntityType().name());
            payload.addCustomDictionary("actionOnEntity", edsNotificationMessage.getActionOnEntity() != null ? edsNotificationMessage.getActionOnEntity().name() : "");
//            payload1.addCustomDictionary("viewerUserID", edsNotificationMessage.getViewerUserID());
//            payload1.addCustomDictionary("actorUserID", edsNotificationMessage.getActorUserID());
            payload.addAlert(messageName);
            boolean isProduction = false;
            String certificat = "Certificates_Kpi_Push_Dev.p12";
            if (EdsContextParams.isLiveEnvironment() && !EdsContextParams.isAWS()) {
                isProduction = true;
                certificat = "Certificates_Kpi_Push_Live.p12";
            }
            PushedNotifications pushedNotifications = Push.payload(payload, this.getClass().getResource("/").getPath() + certificat, "kpi.com", isProduction, deviceToken);
            if (pushedNotifications.getSuccessfulNotifications() != null && pushedNotifications.getSuccessfulNotifications().size() > 0) {
                log.info("PushedNotifications Notification has been sent to: " + edsNotificationMessage.getEntityType().name() + ", deviceToken=" + deviceToken);
            } else {
                logFailedPushNotifications(pushedNotifications.getFailedNotifications());
            }
        } catch (CommunicationException e) {
            log.error("CommunicationException:" + e.getMessage());
        } catch (KeystoreException e) {
            log.error("KeyStoreException:" + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageForCookieDev(String deviceToken, String msgTitle, String msgBody) {
        try {
            PushNotificationPayload payload = PushNotificationPayload.fromJSON("{\n" +
                    "    \"aps\" : {\n" +
                    "        \"alert\" : {\n" +
                    "            \"title\" : \"" + msgTitle + "\",\n" +
                    "            \"body\" : \"" + msgBody + "\"\n" +
                    "        },\n" +
                    "        \"sound\" : \"default\"\n" +
                    "    }\n" +
                    "}");
            /*PushNotificationPayload payload = new PushNotificationPayload();
            payload.addBadge(3);
            payload.addSound("default");
//            payload.addCustomDictionary("message", msgTitle);
            payload.addCustomDictionary("title", msgTitle);
            payload.addCustomDictionary("body", msgTitle);
//            String jsonData = "{\"title\":\""+msgTitle+"\", \"body\":\"" + msgBody + "\"}";
            payload.addAlert(msgBody);*/
//            boolean isProduction = false;
            boolean isProduction = true;
            String certificat = "cookiedev_apns_prod_only.p12";
            //String certificat = "aps_distribution_single_pass.p12";
            /*if (EdsContextParams.isLiveEnvironment() && !EdsContextParams.isAWS()) {
                isProduction = true;
//                certificat = "cookiedev_apns_prod_only.p12";
                certificat = "aps_distribution_single_pass.p12";
            }*/
            PushedNotifications pushedNotifications = Push.payload(payload, this.getClass().getResource("/").getPath() + certificat, "16h98k5S", isProduction, deviceToken);
            if (pushedNotifications.getSuccessfulNotifications() != null && pushedNotifications.getSuccessfulNotifications().size() > 0) {
                log.info("PushedNotifications Notification has been sent to: " + msgTitle + ", deviceToken=" + deviceToken);
            } else {
                logFailedPushNotifications(pushedNotifications.getFailedNotifications());
            }
        } catch (CommunicationException e) {
            log.error("CommunicationException:" + e.getMessage());
        } catch (KeystoreException e) {
            log.error("KeyStoreException:" + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logFailedPushNotifications(PushedNotifications failedPushNotifications) {
        if (failedPushNotifications != null && failedPushNotifications.size() > 0) {
            for (PushedNotification failedPush : failedPushNotifications) {
                log.error("Failed push (iOS) : " + failedPush);
            }
        }
    }

}
