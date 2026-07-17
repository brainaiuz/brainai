package com.edatasite.workforce.core.config.multitenancy;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
//        return DataSourceUtils.getConnection(dataSource);
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
//        DataSourceUtils.releaseConnection(connection, dataSource);
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        logger.debug("Release connection for tenant {}", tenantIdentifier);
        connection.setSchema(TenantContextHolder.DEFAULT_SCHEMA);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType)) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException(unwrapType);
        }
    }
}

