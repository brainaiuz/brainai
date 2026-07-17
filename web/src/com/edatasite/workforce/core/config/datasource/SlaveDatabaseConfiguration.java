package com.edatasite.workforce.core.config.datasource;

import com.edatasite.workforce.core.config.multitenancy.Cluster;
import com.edatasite.workforce.core.config.multitenancy.TenantDatabaseConfigProperties;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTransactionManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AutoConfigureAfter(GlobalDatabaseConfiguration.class)
/*@EnableJpaRepositories(entityManagerFactoryRef = "slaveEntityManagerFactory",
    transactionManagerRef = "slaveTransactionManager",
    repositoryBaseClass = BaseRepositoryImpl.class)*/
public class SlaveDatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SlaveDatabaseConfiguration.class);

    private final Environment env;
    private final ConfigurableListableBeanFactory beanFactory;
    private final TenantDatabaseConfigProperties databaseConfigProperties;
    private final SlaveDatabaseConfigProperties hikariConfigProperties;

    public SlaveDatabaseConfiguration(Environment env, ConfigurableListableBeanFactory beanFactory, TenantDatabaseConfigProperties databaseConfigProperties, SlaveDatabaseConfigProperties hikariConfigProperties) {
        this.env = env;
        this.beanFactory = beanFactory;
        this.databaseConfigProperties = databaseConfigProperties;
        this.hikariConfigProperties = hikariConfigProperties;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        log.info("Initializing Slave DataSource...");
        List<Cluster> clusters = databaseConfigProperties.getClusters();
        Map<Object, Object> targetDataSources = new HashMap<>();
        clusters
                .forEach(cluster -> {
                    /*HikariConfig hikariConfig = new HikariConfig();
                    hikariConfig.setUsername(cluster.getSlave().getUsername());
                    hikariConfig.setPassword(cluster.getSlave().getPassword());
                    hikariConfig.setJdbcUrl(cluster.getSlave().getUrl());
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
                    String tenantConnectionPoolName = "connection-pool-" + cluster.getCode() + "-" + hikariConfigProperties.getPoolName();
                    hikariConfig.setPoolName(tenantConnectionPoolName);

                    hikariConfig.addDataSourceProperty("preparedStatementCacheQueries", "0");
                    hikariConfig.addDataSourceProperty("prepareThreshold", "0");

                    HikariDataSource ds = new HikariDataSource(hikariConfig);*/

                    DriverManagerDataSource ds = new DriverManagerDataSource();
                    ds.setUsername(cluster.getSlave().getUsername());
                    ds.setPassword(cluster.getSlave().getPassword());
                    ds.setUrl(cluster.getSlave().getUrl());
                    ds.setDriverClassName("org.postgresql.Driver");
                    targetDataSources.put(cluster.getCode(), ds);
                });
        TenantRoutingDataSource dataSource = new TenantRoutingDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(targetDataSources.get(TenantContextHolder.DEFAULT_DB));
        return dataSource;
    }

    @Bean("slaveEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory(@Qualifier("slaveDataSource") DataSource slaveDataSource) {
        log.info("Initializing Slave EntityManagerFactory...");

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(slaveDataSource);
        emfBean.setPersistenceUnitName("slavePersistenceUnit");
        emfBean.setPackagesToScan(
                "com.edatasite.workforce.core.domain",
                "com.finnetlimited.reportservice.core.server.domain"
        );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DIALECT, env.getProperty("tenant.datasource.slave.jpa.database-platform"));
        properties.put(AvailableSettings.SHOW_SQL, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.show_sql"));
        properties.put(AvailableSettings.FORMAT_SQL, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.format_sql"));
        properties.put(AvailableSettings.HBM2DDL_AUTO, env.getProperty("tenant.datasource.slave.jpa.hibernate.ddl-auto"));
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(this.beanFactory));
        properties.put(AvailableSettings.CONNECTION_PROVIDER, slaveConnectionProvider(slaveDataSource));
        properties.put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, true);

        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.cache.use_second_level_cache"));
        properties.put(AvailableSettings.USE_QUERY_CACHE, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.cache.use_query_cache"));
        properties.put(AvailableSettings.CACHE_REGION_PREFIX, "hibernate");
        properties.put(AvailableSettings.USE_MINIMAL_PUTS, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.cache.use_minimal_puts"));
        properties.put(AvailableSettings.CACHE_REGION_FACTORY, env.getProperty("tenant.datasource.slave.jpa.properties.hibernate.cache.region.factory_class"));

        properties.put(AvailableSettings.FLUSH_MODE, "AUTO");
        properties.put(AvailableSettings.JPA_VALIDATION_MODE, "AUTO");
        properties.put(AvailableSettings.ISOLATION, "2");
        properties.put(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        properties.put(AvailableSettings.AUTO_CLOSE_SESSION, "true");
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "100");
        properties.put(AvailableSettings.GENERATE_STATISTICS, "false");
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");
        properties.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        properties.put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        emfBean.setJpaPropertyMap(properties);

        return emfBean;
    }

    @Bean("slaveTransactionManager")
    public JpaTransactionManager slaveTransactionManager(@Qualifier("slaveEntityManagerFactory") EntityManagerFactory emf) {
        WfmJpaTransactionManager slaveTransactionManager = new WfmJpaTransactionManager();
        slaveTransactionManager.setEntityManagerFactory(emf);
        return slaveTransactionManager;
    }

    @Bean("sessionSlaveFactory")
    public HibernateJpaSessionFactoryBean sessionSlaveFactory(@Qualifier("slaveEntityManagerFactory") EntityManagerFactory emf) {
        HibernateJpaSessionFactoryBean sessionSlaveFactory = new HibernateJpaSessionFactoryBean();
        sessionSlaveFactory.setEntityManagerFactory(emf);
        return sessionSlaveFactory;
    }

    @Bean("slaveConnectionProvider")
    public ConnectionProvider slaveConnectionProvider(DataSource slaveDataSource) {
        KpiHikariCPConnectionProvider slaveConnectionProvider = new KpiHikariCPConnectionProvider();
        slaveConnectionProvider.setDataSource(slaveDataSource);
        return slaveConnectionProvider;
    }
}
