package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ListPanelItemMQ;
import org.springframework.stereotype.Component;

@Component
public class ListPanelSettingsSender extends BaseAmqpSender<ListPanelItemMQ> {

    private final String KEY = "list_panel_settings_key";

    public void sendMessage(ListPanelItemMQ data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }

}
