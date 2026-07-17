package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

@Component
public class EmailConvertToCaseSender extends BaseAmqpSender<String>{

    private final String KEY = "email_convert_to_case_key";

    public void sendMessage(String emailId, Integer companyId, String clusterType) {
        send(emailId, companyId, clusterType, KEY);
    }
}