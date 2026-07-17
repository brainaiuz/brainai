package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ExchangeRateItemMQ;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 13/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ExchangeRateSender extends BaseAmqpSender<ExchangeRateItemMQ> {

    private final String KEY = "exchange_rate_key";

    @Override
    public void sendMessage(ExchangeRateItemMQ data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
