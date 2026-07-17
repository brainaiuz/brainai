package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

@Component
public class EndOfYearProcessSender extends BaseAmqpSender<Integer> {

    private final String KEY = "end_of_year_process_key";

    @Override
    public void sendMessage(Integer emailSettingId, Integer companyId, String clusterType) {
        send(emailSettingId, companyId, clusterType, KEY);
    }
}
