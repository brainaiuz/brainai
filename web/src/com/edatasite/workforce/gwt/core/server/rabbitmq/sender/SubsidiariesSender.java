package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 16/11/12
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SubsidiariesSender extends BaseAmqpSender<List<SelectItem>> {

    private final String KEY = "subsidiaries_key";

    @Override
    public void sendMessage(List<SelectItem> data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
