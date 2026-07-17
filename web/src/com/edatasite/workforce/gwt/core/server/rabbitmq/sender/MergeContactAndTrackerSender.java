package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot
 */
@Component
public class MergeContactAndTrackerSender extends BaseAmqpSender<Map<Integer, String>> {

    private final String KEY = "merge_contact_and_tracker_key";

    @Override
    public void sendMessage(Map<Integer, String> data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
