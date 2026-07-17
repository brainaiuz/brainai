package com.edatasite.workforce.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitCallbackConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitCallbackConfig.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message confirmed successfully: {}", correlationData);
            } else {
                log.error("Message confirmation failed: {}", cause);
            }
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("Message NOT routed! replyCode={}, replyText={}, exchange={}, routingKey={}",
                    replyCode, replyText, exchange, routingKey);
        });
    }
}
