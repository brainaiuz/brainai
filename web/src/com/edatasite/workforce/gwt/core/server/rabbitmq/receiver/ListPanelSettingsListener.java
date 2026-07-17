package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ListPanelItemMQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;


public class ListPanelSettingsListener extends BaseAmqpListener<ListPanelItemMQ> {

    @Autowired
    private CommonServiceLocal commonServiceLocal;


    @Override
    protected void receiveMessage(ListPanelItemMQ message) {
        commonServiceLocal.saveListPanelMq(message);
    }

    @Override
    protected DataMQ<ListPanelItemMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<ListPanelItemMQ>>() {
        }.getType());
    }
}
