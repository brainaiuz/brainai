package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot
 */
@Component
public class StealContactsSender extends BaseAmqpSender<String> {

    private final String KEY = "steal_contacts_key";

    public void sendMessage(String emailId, Integer companyId, String clusterType) {
        send(emailId, companyId, clusterType, KEY);
    }
}
