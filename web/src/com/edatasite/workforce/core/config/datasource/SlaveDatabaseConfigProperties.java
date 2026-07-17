package com.edatasite.workforce.core.config.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlaveDatabaseConfigProperties {
    /**
     * Maximum number of milliseconds that a client will wait for a connection
     * from the pool. If this time is exceeded without a connection becoming
     * available, a SQLException will be thrown from
     * javax.sql.DataSource.getConnection().
     */
    @Value("${tenant.datasource.slave.hikari.connectionTimeout}")
    private long connectionTimeout;

    /**
     * The property controls the maximum size that the pool is allowed to reach,
     * including both idle and in-use connections. Basically this value will
     * determine the maximum number of actual connections to the database
     * backend.
     * <p>
     * When the pool reaches this size, and no idle connections are available,
     * calls to getConnection() will block for up to connectionTimeout
     * milliseconds before timing out.
     */
    @Value("${tenant.datasource.slave.hikari.maxPoolSize}")
    private int maxPoolSize;

    /**
     * This property controls the maximum amount of time (in milliseconds) that
     * a connection is allowed to sit idle in the pool. Whether a connection is
     * retired as idle or not is subject to a maximum variation of +30 seconds,
     * and average variation of +15 seconds. A connection will never be retired
     * as idle before this timeout. A value of 0 means that idle connections are
     * never removed from the pool.
     */
    @Value("${tenant.datasource.slave.hikari.idleTimeout}")
    private long idleTimeout;

    /**
     * The property controls the minimum number of idle connections that
     * HikariCP tries to maintain in the pool, including both idle and in-use
     * connections. If the idle connections dip below this value, HikariCP will
     * make the best effort to restore them quickly and efficiently.
     */
    @Value("${tenant.datasource.slave.hikari.minIdle}")
    private int minIdle;

    /**
     * This property controls the amount of time that a connection can be out of the pool before
     * a message is logged indicating a possible connection leak. A value of 0 means leak detection is disabled.
     * Lowest acceptable value for enabling leak detection is 2000 (2 seconds). Default: 0
     */
    @Value("${tenant.datasource.slave.hikari.leakDetectionThreshold}")
    private int leakDetectionThreshold;

    /**
     * The name for the master database connection pool
     */
    @Value("${tenant.datasource.slave.hikari.poolName}")
    private String poolName;

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }

    public void setLeakDetectionThreshold(int leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
}
