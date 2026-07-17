package com.edatasite.workforce.utils.redis;

import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSocketObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Normurod
 */
public class RedisClient {

    private static Logger log = LoggerFactory.getLogger(RedisClient.class);

    /**
     * Sets a new key-value pair.
     *
     * @param key   The name of the key
     * @param value The string value of the key
     */
    public static void setKey(String key, String value) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.set(key, value);
        }
    }

    /**
     * Sets a new key-value pair. Value might be any object.
     *
     * @param key   The name of the key
     * @param value The string value of the key
     * @param type  The type of the value
     */
    public static void setKey(String key, Object value, Class<?> type) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.set(key, new Gson().toJson(value, type));
        }
    }

    /**
     * Sets a new key-value pair with a time to live.
     *
     * @param key   The name of the key
     * @param value The string value of the key
     * @param type  The type of the value
     * @param ttl   The time to live of the key in seconds
     */
    public static void setKey(String key, Object value, Class<?> type, int ttl) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.setex(key, ttl, new Gson().toJson(value, type));
        }
    }

    /**
     * Sets a new key-value pair with a time to live.
     *
     * @param key   The name of the key
     * @param value The string value of the key
     * @param ttl   The time to live of the key in seconds
     */
    public static void setKey(String key, String value, int ttl) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.setex(key, ttl, value);
        }
    }

    /**
     * Returns a value of a key. Returns null if key does not exists.
     *
     * @param key The name of the key
     * @return The value of the key
     */
    public static String getKey(String key) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Returns a value of the key as a object. Returns null if key does not exists.
     *
     * @param key
     * @param type
     * @param <T>
     * @return The value of the key
     */
    public static <T> T getKey(String key, Class<T> type) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            return new Gson().fromJson(jedis.get(key), type);
        }
    }

    /**
     * Returns a value of the key as a object. Returns null if key does not exists.
     *
     * <Example>
     * Type type -> new TypeToken<Map<Object, Object>>() {}.getType() {Object might be any type(Integer, String, BigDecimal or Custom type)}
     * <p>
     * Type type -> new TypeToken<GenericClass<T>>() {}.getType() {T might be any sub class of the GenericClass}
     * </Example>
     *
     * @param key
     * @param type
     * @param <T>
     * @return The value of the key
     */
    public static <T> T getKey(String key, Type type) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            return new Gson().fromJson(jedis.get(key), type);
        }
    }

    /**
     * @param key The name of the key
     * @return The remaining time to live of the key in seconds. -2 if the key
     * does not exist.-1 if the key exists but has no associated expire.
     */
    public static long getTtl(String key) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            return jedis.ttl(key);
        }
    }

    /**
     * Remove keys and their values from the redis server.
     *
     * @param key
     * @return
     */
    public static long removeKey(String... key) {
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            return jedis.del(key);
        }
    }

    /**
     * Subscribe to a topic.
     *
     * @param jedisPubSub The subscriber
     * @param chanel      The string value of topic
     */
    public static void subscribe(JedisPubSub jedisPubSub, String chanel) {
        if (chanel != null && jedisPubSub != null) {
            try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
                jedis.subscribe(jedisPubSub, chanel);
            } catch (Exception e) {
                log.info("Subscribing failed. {}", e);
            }
        }
    }

    /**
     * Publish to a topic.
     *
     * @param chanel The name of topic to publish
     * @param object The message to be send
     */
    public static void publish(String chanel, RedisSocketObject object) {
        if (StringUtils.isNotBlank(chanel) && object != null) {
            try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
                jedis.publish(chanel, new Gson().toJson(object));
            }
        }
    }

    /**
     * Publish to a topic.
     *
     * @param object The message to be send
     */
    public static void publish(RedisSocketObject object) {
        publish(Constants.WEBSOCKETS_CHANEL, object);
    }


    public static void setKeyForR(String key, Object value, Class<?> type, int ttl) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.setex(key, ttl, objectMapper.writeValueAsString(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getKeyForR(String key, Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            if (jedis.get(key) != null) {
                return objectMapper.readValue(jedis.get(key), type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
