package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.messagecenter.server.app.tracker.EmailTrackerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 */
public class MergeContactAndTrackerListener extends BaseAmqpListener<Map<Integer, String>> {

    private static final Logger log = LoggerFactory.getLogger("messageCenter");

    @Autowired
    private EmailTrackerService emailTrackerService;

    @Override
    public void receiveMessage(Map<Integer, String> data) {
        for (Map.Entry<Integer, String> entry : ((Map<Integer, String>) data).entrySet()) {
            emailTrackerService.addTrackerToCrmContactOrLead(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected DataMQ<Map<Integer, String>> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<Map<Integer, String>>>() {
        }.getType());
    }
}
