package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.core.env.AbstractEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: 12.08.2010
 * Time: 16:26:45
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class WfmGetConnection {

    private static String dbUser = null;
    private static String dbPassword = null;


    static {
        //try retrieve data from file]
        Properties props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (StringUtil.isEmpty(profile)) {
            System.out.println("--------------------------");
            System.out.println("Please set spring.profiles.active!!!");
            System.out.println("--------------------------");
        }
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        if (stream != null) {
            try {
                props.load(stream);
                dbUser = props.getProperty("tenant.datasource.username");
                dbPassword = props.getProperty("tenant.datasource.password");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets a database connection.
     * IMPORTANT: Caller MUST close the connection when done using try-with-resources:
     * try (Connection conn = WfmGetConnection.getConnection(url)) { ... }
     *
     * @param url database URL
     * @return Connection object or null if connection fails
     * @deprecated Use Spring-managed DataSource or JPA EntityManager instead
     */
    public static Connection getConnection(String url) {
        Connection conc = null;
        try {
            Class.forName("org.postgresql.Driver");
            conc = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + url + " - " + e.getMessage());
            e.printStackTrace();
        }
        return conc;
    }

}
