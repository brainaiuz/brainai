package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.InterCompanyDataMQ;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/21/12
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class InterCompanySalesSender extends BaseAmqpSender<InterCompanyDataMQ> {

    private final String KEY = "inter_company_sales_key";

    @Override
    public void sendMessage(InterCompanyDataMQ data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
