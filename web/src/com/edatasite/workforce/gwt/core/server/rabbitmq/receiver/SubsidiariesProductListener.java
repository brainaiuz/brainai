package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 22/11/12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class SubsidiariesProductListener extends BaseAmqpListener<List<SelectItem>> {

    private static final Logger log = LoggerFactory.getLogger(SubsidiariesProductListener.class);

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    @Override
    public void receiveMessage(List<SelectItem> data) {
        log.info("---------------------------------------- Subsidiary Product CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase());
        accountingServiceLocal.saveSubsidiariesProduct(data);
    }

    @Override
    protected DataMQ<List<SelectItem>> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<SelectItem>>() {
        }.getType());
    }
}
