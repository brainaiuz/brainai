package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.MultiCurrencyItemMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 03/10/12
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */
public class MultiCurrencyListener extends BaseAmqpListener<MultiCurrencyItemMQ> {

    private static Logger log = LoggerFactory.getLogger(MultiCurrencyListener.class);

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    @Override
    public void receiveMessage(MultiCurrencyItemMQ data) {
        log.info("---------------------------------------- Multi Currency CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase());
        accountingServiceLocal.createCompanyMultiCurrency(data.getCurrencyIds(), data.getCurrencyRegCompanyId());
    }

    @Override
    protected DataMQ<MultiCurrencyItemMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<MultiCurrencyItemMQ>>() {
        }.getType());
    }
}
