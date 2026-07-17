package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.UserRequestItemMQ;
import org.springframework.stereotype.Component;

@Component
public class UserRequestTrackingSender extends BaseAmqpSender<UserRequestItemMQ> {

    private final String KEY = "user_request_tracking_key";
    private final String LISTENER = "userRequestRrackingListenerId";

    @Override
    public void sendMessage(UserRequestItemMQ data, Integer companyId, String clusterType) {
        sendToDynamic(data, companyId, clusterType, KEY, LISTENER);
    }
}