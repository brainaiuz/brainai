package com.finnetlimited.reportservice.core.server.connection;

import com.finnetlimited.reportservice.core.client.enumtype.ConnectionDriverType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: ${Dilsh0d}
 * Date: 31-Mar-2010
 * Time: 18:05:42
 */

public final class DBConnection {

    private Log log = LogFactory.getLog(getClass());

    private Statement statement;
    private Connection connection;
    private ConnectionDriverType driverType;

    public DBConnection(ConnectionDriverType driverType) {
        this.driverType = driverType;
    }

    public Statement getStatement(String jdbcUrl, String user, String password) {
        try {
            Class.forName(driverType.getDriverClassName());
            connection = DriverManager.getConnection(jdbcUrl, user, password);
            //connection.setReadOnly(true);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            log.error(driverType.getDriverClassName() + "Class Not Found Exception:", e);
        } catch (SQLException e) {
            log.error("Sql Exception:", e);
        }
        return statement;
    }

    public void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
