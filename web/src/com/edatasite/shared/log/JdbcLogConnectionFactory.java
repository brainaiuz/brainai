package com.edatasite.shared.log;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.core.env.AbstractEnvironment;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Sherali on 3/30/2016.
 * Project web
 */
public class JdbcLogConnectionFactory {

    public static Properties props;

    static {
        //try retrieve data from file
        props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_SPRING_PROFILES);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        if (stream != null) {
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private interface Singleton {
        JdbcLogConnectionFactory INSTANCE = new JdbcLogConnectionFactory();
    }

    private final DataSource dataSource;

    private JdbcLogConnectionFactory() {
        Properties properties = new Properties();
        properties.setProperty("user", props.getProperty("logger.datasource.username"));
        properties.setProperty("password", props.getProperty("logger.datasource.password"));
        String dbUrl = props.getProperty("logger.datasource.url");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbUrl, properties);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        poolableConnectionFactory.setValidationQuery("SELECT 1");
        poolableConnectionFactory.setDefaultAutoCommit(false);
        poolableConnectionFactory.setValidationQueryTimeout(3);
//        poolableConnectionFactory.setDefaultReadOnly(true);
        poolableConnectionFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        this.dataSource = new PoolingDataSource<>(connectionPool);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}
