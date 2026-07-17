package com.edatasite.workforce.gwt.core.server.utils;

import jakarta.mail.Session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 08-Jul-2011
 * Time: 20:27:17
 */
public class EmailSessionCache {
    public static synchronized boolean contains(String sessionKey) {
        return sessionKey != null && getInstance().containsKey(sessionKey);
    }

    public static synchronized Session get(String sessionKey) {
        if(sessionKey != null){
            return (Session) getInstance().get(sessionKey);
        }
        return null;
    }

    public static synchronized boolean containsKey(String sessionKey) {
        return contains(sessionKey);
    }

    public static Set<String> keySet() {
        return getInstance().keySet();
    }

    public static synchronized void remove(String sessionKey) {
        if(sessionKey != null){
            getInstance().remove(sessionKey);
        }
    }

    public static synchronized void put(String sessionKey, Session session) {
        if(sessionKey != null){
            getInstance().put(sessionKey, session);
        }
    }

    private static ConcurrentHashMap<String, Session> sessions;

    public static synchronized ConcurrentHashMap<String, Session> getInstance() {
        if (sessions == null) {
            sessions = new ConcurrentHashMap<>();
        }
        return sessions;
    }
}
