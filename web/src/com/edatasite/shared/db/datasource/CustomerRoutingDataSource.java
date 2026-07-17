package com.edatasite.shared.db.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 3/28/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class CustomerRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return CustomerContextHolder.getCustomerType();

    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

}
