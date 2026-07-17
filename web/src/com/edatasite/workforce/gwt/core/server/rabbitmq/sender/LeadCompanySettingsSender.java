package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import org.springframework.stereotype.Component;

@Component
public class LeadCompanySettingsSender extends BaseAmqpSender<CompanyData> {

    private final String KEY = "lead_company_settings_key";

    @Override
    public void sendMessage(CompanyData data, Integer companyId, String clusterType) {
        send(data, companyId, clusterType, KEY);
    }
}
