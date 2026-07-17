package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSocketObject;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventHandler {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    public static void fireEvent(Integer eventType, String message) {
        try {
            WebSocketServerObject notification = new WebSocketServerObject();

            notification.setData(message);
            notification.setEventType(eventType);

            RedisSocketObject redisSocketObject = new RedisSocketObject();
            redisSocketObject.setCompanyId(Integer.parseInt(SecurityContext.getInstance().getCompanyId()));
            redisSocketObject.setWebSocketServerObject(notification);
            redisSocketObject.setSendToAll(true);
            RedisClient.publish(redisSocketObject);
            log.warn("Event sent to websocket: " + notification);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
