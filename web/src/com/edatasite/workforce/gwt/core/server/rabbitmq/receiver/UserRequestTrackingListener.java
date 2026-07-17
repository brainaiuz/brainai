package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.UserRequestItemMQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRequestTrackingListener extends BaseAmqpListener<UserRequestItemMQ> {

    @Autowired
    private CommonServiceLocal commonServiceLocal;


    @Override
    protected void receiveMessage(UserRequestItemMQ message) {
        commonServiceLocal.saveuserRequestTrackingMq(message);
    }

    @Override
    protected DataMQ<UserRequestItemMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<UserRequestItemMQ>>() {
        }.getType());
    }
}
