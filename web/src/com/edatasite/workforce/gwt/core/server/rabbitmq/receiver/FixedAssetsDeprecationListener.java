package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DeprecationItemMQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class FixedAssetsDeprecationListener extends BaseAmqpListener<DeprecationItemMQ> {

    private static final Logger log = LoggerFactory.getLogger(SinglePayslipListener.class);

    @Autowired
    private FixedAssetServiceLocal fixedAssetServiceLocal;

    @Override
    protected void receiveMessage(DeprecationItemMQ item) {
        long millis = new Date().getTime();
        log.info("*** Fixed Assets update deprecation has been started ***");
        fixedAssetServiceLocal.updateDeprecations(item);
        log.info("*** Fixed Assets update deprecation has finished, duration {}ms ***", millis - new Date().getTime());
    }

    @Override
    protected DataMQ<DeprecationItemMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<DeprecationItemMQ>>() {}.getType());
    }
}
