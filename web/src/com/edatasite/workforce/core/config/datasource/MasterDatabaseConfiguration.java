package com.edatasite.workforce.core.config.datasource;

import com.edatasite.workforce.core.config.multitenancy.Cluster;
import com.edatasite.workforce.core.config.multitenancy.TenantDatabaseConfigProperties;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTransactionManager;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter(GlobalDatabaseConfiguration.class)
public class MasterDatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MasterDatabaseConfiguration.class);

    private final Environment env;
    private final ConfigurableListableBeanFactory beanFactory;
    private final TenantDatabaseConfigProperties databaseConfigProperties;
    private final MasterDatabaseConfigProperties hikariConfigProperties;

    public MasterDatabaseConfiguration(Environment env, ConfigurableListableBeanFactory beanFactory, TenantDatabaseConfigProperties databaseConfigProperties, MasterDatabaseConfigProperties hikariConfigProperties) {
        this.env = env;
        this.beanFactory = beanFactory;
        this.databaseConfigProperties = databaseConfigProperties;
        this.hikariConfigProperties = hikariConfigProperties;
    }

    @Primary
    @Bean("dataSource")
    public DataSource dataSource() {
        log.info("Initializing Master DataSource...");
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<Cluster> clusters = databaseConfigProperties.getClusters();
        clusters
                .forEach(cluster -> {
                    DriverManagerDataSource ds = new DriverManagerDataSource();
                    ds.setUsername(cluster.getMaster().getUsername());
                    ds.setPassword(cluster.getMaster().getPassword());
                    ds.setUrl(cluster.getMaster().getUrl());
                    ds.setDriverClassName("org.postgresql.Driver");
                    targetDataSources.put(cluster.getCode(), ds);
                });
        TenantRoutingDataSource dataSource = new TenantRoutingDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(targetDataSources.get(TenantContextHolder.DEFAULT_DB));
        return dataSource;
    }

    @Primary
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(DataSource dataSource) {
        log.info("Initializing Master EntityManagerFactory...");

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPersistenceUnitName("masterPersistenceUnit");
        emfBean.setPackagesToScan(
                "com.edatasite.workforce.core.domain",
                "com.finnetlimited.reportservice.core.server.domain"
        );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DIALECT, env.getProperty("tenant.datasource.master.jpa.database-platform"));
        properties.put(AvailableSettings.SHOW_SQL, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.show_sql"));
        properties.put(AvailableSettings.FORMAT_SQL, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.format_sql"));
        properties.put(AvailableSettings.HBM2DDL_AUTO, env.getProperty("tenant.datasource.master.jpa.hibernate.ddl-auto"));
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(this.beanFactory));
        properties.put(AvailableSettings.CONNECTION_PROVIDER, connectionProvider(dataSource));
        properties.put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, true);

        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.cache.use_second_level_cache"));
//        properties.put(AvailableSettings.USE_QUERY_CACHE, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.cache.use_query_cache"));
        properties.put(AvailableSettings.USE_MINIMAL_PUTS, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.cache.use_minimal_puts"));
        properties.put(AvailableSettings.CACHE_REGION_FACTORY, env.getProperty("tenant.datasource.master.jpa.properties.hibernate.cache.region.factory_class"));

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

    @Bean("jpaTemplate")
    public WfmJpaTemplate jpaTemplate(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        final WfmJpaTemplate jpaTemplate = new WfmJpaTemplate();
        jpaTemplate.setEntityManagerFactory(emf);
        return jpaTemplate;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Primary
    @Bean("transactionManager")
    public WfmJpaTransactionManager masterTransactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        WfmJpaTransactionManager masterTransactionManager = new WfmJpaTransactionManager();
        masterTransactionManager.setEntityManagerFactory(emf);
        return masterTransactionManager;
    }

    @Bean("sessionFactory")
    public HibernateJpaSessionFactoryBean sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        HibernateJpaSessionFactoryBean sessionFactory = new HibernateJpaSessionFactoryBean();
        sessionFactory.setEntityManagerFactory(emf);
        return sessionFactory;
    }

    @Bean("connectionProvider")
    public ConnectionProvider connectionProvider(DataSource dataSource) {
        KpiHikariCPConnectionProvider connectionProvider = new KpiHikariCPConnectionProvider();
        connectionProvider.setDataSource(dataSource);
        return connectionProvider;
    }
}
