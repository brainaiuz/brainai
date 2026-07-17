package com.edatasite.workforce.core.config.datasource;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "globalAuthEntityManagerFactory", transactionManagerRef = "globalAuthTransactionManager")
public class GlobalDatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(GlobalDatabaseConfiguration.class);

    private final GlobalAuthDatabaseConfigProperties properties;

    public GlobalDatabaseConfiguration(GlobalAuthDatabaseConfigProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "globalauthDataSource")
    public DataSource globalAuthDataSource() {
        log.info("Initializing GlobalAuth DataSource...");
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

    /*@Bean(name = "globalAuthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean globalAuthEntityManagerFactory() {
        log.info("GLOBAL_AUTH: Entity BaseRepository Factory");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(globalAuthDataSource());
//        em.setPersistenceUnitName("globalPersistenceUnit");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        // Set the hibernate properties
        em.setJpaProperties(hibernateProperties());
        return em;
    }*/

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.put(Environment.SHOW_SQL, false);
        properties.put(Environment.FORMAT_SQL, false);
        properties.put(Environment.HBM2DDL_AUTO, "update");
//        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
//        properties.put(Environment.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        properties.put(Environment.USE_SECOND_LEVEL_CACHE, false);
        return properties;
    }

    @Bean("globalAuthJdbcSpringTemplate")
    public JdbcTemplate globalAuthJdbcSpringTemplate(@Qualifier("globalauthDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /*@Bean(name = "globalAuthTransactionManager")
    public JpaTransactionManager globalAuthTransactionManager(@Qualifier("globalAuthEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }*/

    /*@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }*/
}
