package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.server.controllers.WebSocketServletImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/*
* Send WEB Push Notifications
*
* */
public class WebPushNotificationListener extends BaseAmqpListener<WebSocketServerObject> {

    private static Logger log = LoggerFactory.getLogger(WebPushNotificationListener.class);

    @Override
    public void receiveMessage(WebSocketServerObject data) {
        log.info("---------------------------------------- WEB Push Notification, CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase() + " ----------------------------------------");
        try {
            if(data!=null && data.getUserId()!=null) {
                WebSocketServletImpl.sendMessage(data.getUserId(), data);
            } else if(data!=null) {
                WebSocketServletImpl.sendMessage(data);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    protected DataMQ<WebSocketServerObject> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<WebSocketServerObject>>() {
        }.getType());
    }
}
