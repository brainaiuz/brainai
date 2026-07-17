package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

@Component
public class EmailFetchSender extends BaseAmqpSender<Integer> {

    private final String KEY = "email_fetch_key";

    @Override
    public void sendMessage(Integer emailSettingId, Integer companyId, String clusterType) {
        send(emailSettingId, companyId, clusterType, KEY);
    }
}
