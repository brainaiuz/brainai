package com.edatasite.workforce.core.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Component
@ConfigurationProperties(prefix = "tenant.datasource.master.hikari")
public class MasterDatabaseConfigProperties {

    /**
     * Maximum number of milliseconds that a client will wait for a connection
     * from the pool. If this time is exceeded without a connection becoming
     * available, a SQLException will be thrown.
     */
    @Positive
    private long connectionTimeout = 10000;

    /**
     * The property controls the maximum size that the pool is allowed to reach.
     */
    @Min(5)
    @Max(200)
    private int maxPoolSize = 20;

    /**
     * Maximum amount of time (in milliseconds) that a connection is allowed
     * to sit idle in the pool.
     */
    private long idleTimeout = 540000;

    /**
     * Minimum number of idle connections that HikariCP tries to maintain.
     */
    @Min(0)
    @Max(50)
    private int minIdle = 5;

    /**
     * Amount of time that a connection can be out of the pool before
     * a message is logged indicating a possible connection leak.
     */
    @Min(2000)
    private int leakDetectionThreshold = 30000;

    /**
     * The name for the master database connection pool.
     */
    private String poolName = "MasterHikariCP";

    /**
     * Maximum lifetime of a connection in the pool.
     */
    private long maxLifetime = 1500000; // 30 minutes

    /**
     * Connection test query for validation.
     */
    private String connectionTestQuery = "SELECT 1";

    /**
     * AutoCommit mode - PgBouncer uchun true bo'lishi kerak
     */
    private boolean autoCommit = true;

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

    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
