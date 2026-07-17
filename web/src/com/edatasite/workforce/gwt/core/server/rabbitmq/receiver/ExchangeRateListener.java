package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ExchangeRateItemMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 13/11/12
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeRateListener extends BaseAmqpListener<ExchangeRateItemMQ> {

    private static Logger log = LoggerFactory.getLogger(ExchangeRateListener.class);

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    @Override
    public void receiveMessage(ExchangeRateItemMQ data) {
        log.info("---------------------------------------- Exchange Rate CompanyID=" + SecurityContext.getInstance().getCompanyId() + " Cluster Type = " + SecurityContext.getInstance().getDatabase());
        accountingServiceLocal.saveCurrenciesExchangeRate(data);
    }

    @Override
    protected DataMQ<ExchangeRateItemMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<ExchangeRateItemMQ>>() {
        }.getType());
    }
}
