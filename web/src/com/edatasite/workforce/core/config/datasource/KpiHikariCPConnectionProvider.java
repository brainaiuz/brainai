package com.edatasite.workforce.core.config.datasource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Stoppable;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;

public class KpiHikariCPConnectionProvider implements ConnectionProvider, Stoppable {

    @Serial
    private static final long serialVersionUID = -9131625057941275711L;

    private static final Logger LOGGER = Logger.getLogger(KpiHikariCPConnectionProvider.class);

    /**
     * HikariCP data source.
     */
    private DataSource dataSource = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // *************************************************************************
    // ConnectionProvider
    // *************************************************************************

    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource != null) {
            Connection conn = dataSource.getConnection();
            // Ensure auto-commit is properly handled for multi-tenant
            try {
                if (conn != null && !conn.getAutoCommit()) {
                    // For multi-tenant, we want explicit transaction control
                    conn.setAutoCommit(false);
                }
            } catch (SQLException e) {
                LOGGER.warn("Could not set auto-commit on connection: " + e.getMessage());
                // Don't throw here, let the connection be used
            }
            return conn;
        }
        throw new SQLException("DataSource is not initialized");
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                // Reset connection state before returning to pool
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.debug("Could not reset connection state: " + e.getMessage());
            } finally {
                conn.close();
            }
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false; // HikariCP manages connections efficiently
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isUnwrappableAs(Class unwrapType) {
        return ConnectionProvider.class.equals(unwrapType)
                || KpiHikariCPConnectionProvider.class.isAssignableFrom(unwrapType)
                || DataSource.class.isAssignableFrom(unwrapType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> unwrapType) {
        if (ConnectionProvider.class.equals(unwrapType) ||
                KpiHikariCPConnectionProvider.class.isAssignableFrom(unwrapType)) {
            return (T) this;
        } else if (DataSource.class.isAssignableFrom(unwrapType)) {
            try {
                return (T) dataSource.unwrap(unwrapType);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnknownUnwrapTypeException(unwrapType);
        }
    }

    // *************************************************************************
    // Stoppable
    // *************************************************************************

    @Override
    public void stop() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                LOGGER.error("Error closing datasource: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (Exception e) {
                LOGGER.error("Error closing datasource: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
