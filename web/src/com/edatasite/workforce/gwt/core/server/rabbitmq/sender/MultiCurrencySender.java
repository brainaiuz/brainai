package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.MultiCurrencyItemMQ;
import org.springframework.stereotype.Component;

@Component
public class MultiCurrencySender extends BaseAmqpSender<MultiCurrencyItemMQ> {

    private final String KEY = "multi_currency_key";

    @Override
    public void sendMessage(MultiCurrencyItemMQ data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
