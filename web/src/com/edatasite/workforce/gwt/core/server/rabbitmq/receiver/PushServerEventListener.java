package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationReloadEvent;
import com.edatasite.workforce.gwt.core.server.app.ServerSentEventService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dilsh0d on 28.09.15.
 */
public class PushServerEventListener extends BaseAmqpListener<DefaultDomain> {

    private static final Logger log = LoggerFactory.getLogger(PushServerEventListener.class);

    @Autowired
    private ServerSentEventService serverSentEventService;

    @Override
    public void receiveMessage(DefaultDomain data) {
        log.info(data.getName());
        serverSentEventService.addEvent(data, new NotificationReloadEvent());
    }

    @Override
    protected DataMQ<DefaultDomain> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<DefaultDomain>>() {
        }.getType());
    }
}
