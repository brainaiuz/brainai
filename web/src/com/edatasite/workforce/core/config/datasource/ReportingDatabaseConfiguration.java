package com.edatasite.workforce.core.config.datasource;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter(MasterDatabaseConfiguration.class)
public class ReportingDatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ReportingDatabaseConfiguration.class);

    private final ReportingDatabaseConfigProperties properties;

    public ReportingDatabaseConfiguration(ReportingDatabaseConfigProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "reportingDataSource")
    public DataSource reportingDataSource() {
        log.info("Initializing Reporting DataSource...");
        /*HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setUsername(properties.getUsername());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setJdbcUrl(properties.getUrl());
        hikariConfig.setDriverClassName(properties.getDriverClassName());
        // HikariCP settings
        hikariConfig.setMaximumPoolSize(properties.getMaxPoolSize());
        hikariConfig.setMinimumIdle(properties.getMinIdle());
        hikariConfig.setConnectionTimeout(properties.getConnectionTimeout());
        hikariConfig.setIdleTimeout(properties.getIdleTimeout());
        hikariConfig.setLeakDetectionThreshold(properties.getLeakDetectionThreshold());
        hikariConfig.setPoolName("connection-pool-" + properties.getPoolName());

        return new HikariDataSource(hikariConfig);*/

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        ds.setUrl(properties.getUrl());
        ds.setDriverClassName(properties.getDriverClassName());
        return ds;
    }
}
