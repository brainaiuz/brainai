package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * UPdate LEAD of signed up company
 *
 * */
public class LeadCompanySettingsListener extends BaseAmqpListener<CompanyData> {

    private static Logger log = LoggerFactory.getLogger(LeadCompanySettingsListener.class);

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;

    @Override
    public void receiveMessage(CompanyData data) {
        log.info("---------------------------------------- LEAD Company Settings CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase() + " ----------------------------------------");

        Integer companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        EdsCompany company = companyManager.get(companyID);
        ServerSecurityContext.getInstance().setStaticUserID(company.getCreator().getObjectID());
        crmServiceLocal.updateLeadData(data);
    }

    @Override
    protected DataMQ<CompanyData> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<CompanyData>>() {
        }.getType());
    }
}
