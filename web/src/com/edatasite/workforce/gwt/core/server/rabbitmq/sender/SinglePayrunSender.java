package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import org.springframework.stereotype.Component;

@Component
public class SinglePayrunSender extends BaseAmqpSender<PayslipItemFilter> {

    private final String KEY = "single_payslip_generate_key";

    public void sendMessage(PayslipItemFilter itemFilter, Integer companyId, String clusterType) {
        send(itemFilter, companyId, clusterType, KEY);
    }

    public void sendMessage(PayslipItemFilter itemFilter, Integer userId, Integer companyId, String clusterType) {
        send(itemFilter, userId, companyId, clusterType, KEY);
    }
}
