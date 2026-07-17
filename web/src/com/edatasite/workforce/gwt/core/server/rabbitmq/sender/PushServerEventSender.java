package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import de.novanic.eventservice.client.event.domain.DefaultDomain;
import org.springframework.stereotype.Component;

/**
 * Created by dilsh0d on 28.09.15.
 */
@Component
public class PushServerEventSender extends BaseAmqpSender<DefaultDomain> {

    private final String KEY = "push_server_event_key";

    @Override
    public void sendMessage(DefaultDomain data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
