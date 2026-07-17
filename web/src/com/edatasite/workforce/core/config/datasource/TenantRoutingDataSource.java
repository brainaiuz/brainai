package com.edatasite.workforce.core.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 3/28/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource implements Closeable {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getCustomerType();
    }

    public DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    @Override
    public void close() throws IOException {
        getResolvedDataSources().forEach((o, dataSource) -> {
            if (dataSource instanceof Closeable) {
                try {
                    ((Closeable) dataSource).close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
