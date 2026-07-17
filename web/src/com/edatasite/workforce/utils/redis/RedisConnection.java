package com.edatasite.workforce.utils.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;

/**
 * @author Normurod
 */
public class RedisConnection {

    private static JedisPool jedisPool;

    /**
     * Returns a pool of Jedis connections. Create a new pool of Jedis
     * connections if jedisPool is null.
     *
     * @return JedisPool
     */
    protected static JedisPool getJedisPool() {

        if (null == jedisPool) {
            createJedisPool();

        }
        return jedisPool;
    }

    /**
     * Create a new pool of Jedis connections.
     */
    private static void createJedisPool() {

        //pool configuration
        Integer timeout = getIntegerValue(System.getProperty("redis.pool.timeout"));
        Integer conMaxTotal = getIntegerValue(System.getProperty("redis.pool.maxTotal"));
        Integer conMinIdle = getIntegerValue(System.getProperty("redis.pool.minIdle"));
        Integer conMaxIdle = getIntegerValue(System.getProperty("redis.pool.maxIdle"));
        Integer minEvictableIdleDuration = getIntegerValue(System.getProperty("redis.pool.minEvictableIdleDuration"));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(conMaxTotal != null ? conMaxTotal : 8);
        poolConfig.setMinIdle(conMinIdle != null ? conMinIdle : 0);
        poolConfig.setMaxIdle(conMaxIdle != null ? conMaxIdle : 8);
        Optional.ofNullable(minEvictableIdleDuration).ifPresent(poolConfig::setMinEvictableIdleTimeMillis);
        //connection configuration
        String host = System.getProperty("redis.host");
        Integer port = getIntegerValue(System.getProperty("redis.port"));
        String password = System.getProperty("redis.password");

        jedisPool = new JedisPool(poolConfig, host, port != null ? port : 6379, timeout != null ? timeout : 2000, StringUtils.isNotBlank(password) ? password : (String) null);
    }

    private static Integer getIntegerValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
