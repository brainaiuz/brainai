package com.edatasite.workforce.gwt.core.client.rpc.websocket;

import com.edatasite.workforce.gwt.core.server.controllers.WebSocketServletImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber extends JedisPubSub {

    private static Logger log = LoggerFactory.getLogger(RedisSubscriber.class);

    @Override
    public void onMessage(String channel, String message) {
        log.info("Message received. Channel: {}, Msg: {}", channel, message);
        try {
            RedisSocketObject socketObject = new Gson().fromJson(message, RedisSocketObject.class);
            if(socketObject!=null && socketObject.getCompanyId()!=null && socketObject.getWebSocketServerObject()!=null) {
                if(socketObject.isSendToAll()) {
                    WebSocketServletImpl.sendMessageToAll(socketObject.getCompanyId(), socketObject.getWebSocketServerObject());
                } else if(socketObject.getWebSocketServerObject().getUserId()!=null){
                    SecurityContext.getInstance().setCompanyId(socketObject.getCompanyId());
                    WebSocketServletImpl.sendMessage(socketObject.getWebSocketServerObject().getUserId(), socketObject.getWebSocketServerObject());
                }
            }
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }
}
