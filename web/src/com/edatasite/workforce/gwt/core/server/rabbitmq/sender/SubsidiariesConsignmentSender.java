package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import org.springframework.stereotype.Component;

/**
 * Created by Normurod on 6/15/15.
 */
@Component
public class SubsidiariesConsignmentSender extends BaseAmqpSender<Consignment> {

    private final String KEY = "subsidiaries_consignment_key";

    @Override
    public void sendMessage(Consignment data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
