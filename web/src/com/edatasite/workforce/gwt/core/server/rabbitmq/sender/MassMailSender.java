package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.shared.massmailler.MassMailerData;
import org.springframework.stereotype.Component;

@Component
public class MassMailSender extends BaseAmqpSender<MassMailerData> {
    private final String KEY = "email_sending_key";

    @Override
    public void sendMessage(MassMailerData data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
