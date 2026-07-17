package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.server.app.ConsignmentServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 15/06/15
 * To change this template use File | Settings | File Templates.
 */
public class SubsidiariesConsignmentListener extends BaseAmqpListener<Consignment> {

    private static Logger log = LoggerFactory.getLogger(SubsidiariesConsignmentListener.class);

    @Autowired
    private ConsignmentServiceLocal consignmentServiceLocal;

    @Autowired
    private CompanyManager companyManager;

    @Override
    public void receiveMessage(Consignment data) {
        log.info("---------------------------------------- Subsidiary Product CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase());
        Integer companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        EdsCompany company = companyManager.get(companyID);
        ServerSecurityContext.getInstance().setStaticUserID(company.getCreator().getObjectID());

        consignmentServiceLocal.saveSubsidiariesConsignment(data);
    }

    @Override
    protected DataMQ<Consignment> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<Consignment>>() {
        }.getType());
    }
}
