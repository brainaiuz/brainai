package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DeprecationItemMQ;
import org.springframework.stereotype.Component;

@Component
public class FixedAssetsDeprecationSender extends BaseAmqpSender<DeprecationItemMQ> {

    private final String KEY = "fixed_assets_deprecation_key";

    public void sendMessage(DeprecationItemMQ item, Integer companyId, String clusterType) {
        send(item, companyId, clusterType, KEY);
    }

}
