package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import org.springframework.stereotype.Component;

@Component
public class WebPushNotificationsSender extends BaseAmqpSender<WebSocketServerObject> {

    private final String KEY = "web_push_notification_key";

    @Override
    public void sendMessage(WebSocketServerObject data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
