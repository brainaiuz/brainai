package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

@Component
public class EmailTrackerRelationSender extends BaseAmqpSender<String> {

    private final String KEY = "email_tracker_relation_key";

    @Override
    public void sendMessage(String emailId, Integer companyId, String clusterType) {
        send(emailId, companyId, clusterType, KEY);
    }
}
