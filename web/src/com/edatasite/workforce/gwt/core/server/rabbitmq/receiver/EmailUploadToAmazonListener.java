package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;

public class EmailUploadToAmazonListener extends BaseAmqpListener {

    @Override
    protected void receiveMessage(Object message) {

    }

    @Override
    protected DataMQ convertMessage(String message) {
        return null;
    }
}
