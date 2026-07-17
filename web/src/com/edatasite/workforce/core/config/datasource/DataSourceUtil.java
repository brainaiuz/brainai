package com.edatasite.workforce.core.config.datasource;

import com.edatasite.workforce.core.config.multitenancy.ClusterConnectionConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DataSourceUtil {

    public static HikariDataSource createAndConfigureDataSource(String cluster, ClusterConnectionConfig connectionConfig, HikariConfigProperties hikariConfigProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(connectionConfig.getUsername());
        hikariConfig.setPassword(connectionConfig.getPassword());
        hikariConfig.setJdbcUrl(connectionConfig.getUrl());
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        // Maximum waiting time for a connection from the pool
        hikariConfig.setConnectionTimeout(hikariConfigProperties.getConnectionTimeout());

        // Minimum number of idle connections in the pool
        hikariConfig.setMinimumIdle(hikariConfigProperties.getMinIdle());

        // Maximum number of actual connection in the pool
        hikariConfig.setMaximumPoolSize(hikariConfigProperties.getMaxPoolSize());

        // Maximum time that a connection is allowed to sit idle in the pool
        hikariConfig.setIdleTimeout(hikariConfigProperties.getIdleTimeout());

        //This property controls the amount of time that a connection can be out
        // of the pool before a message is logged indicating a possible connection leak
        hikariConfig.setLeakDetectionThreshold(hikariConfigProperties.getLeakDetectionThreshold());

        // Setting up a pool name for each tenant datasource
        String tenantConnectionPoolName = "connection-pool-" + cluster + "-" + hikariConfigProperties.getPoolName();
        hikariConfig.setPoolName(tenantConnectionPoolName);

        return new HikariDataSource(hikariConfig);
    }
}
