package com.edatasite.workforce.core.config.websocket;

import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSubscriber;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class WebsocketServerConfig {
    private static final Logger log = LoggerFactory.getLogger(WebsocketServerConfig.class);

    private RedisSubscriber subscriber;
    private Thread redisSubscriberThread;

    @PostConstruct
    private void subscribeToRedis() {
        try {
            subscriber = new RedisSubscriber();
            redisSubscriberThread = new Thread(() -> RedisClient.subscribe(subscriber, Constants.WEBSOCKETS_CHANEL));
            redisSubscriberThread.start();
            log.info("Websocket Redis chanel initialized");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    private void unsubscribeFromRedis() {
        try {
            subscriber.unsubscribe();
            log.info("Websocket Redis chanel unsubscribed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            redisSubscriberThread.interrupt();
            log.info("Websocket Redis chanel thread interrupted");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
