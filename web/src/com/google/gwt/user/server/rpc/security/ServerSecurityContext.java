package com.google.gwt.user.server.rpc.security;

import com.edatasite.shared.log.KpiLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 26, 2007
 * Time: 1:42:05 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ServerSecurityContext {

    private static ThreadLocal<ServerSecurityContext> instance = new ThreadLocal<>();

    private static Class serverSecurityContextClass = DefaultServerSecurityContextImpl.class;

    static {
        Properties props = new Properties();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("gwt_security.properties");
        if (stream != null) {
            try {
                props.load(stream);
                String className = props.getProperty("security.context.class");
                serverSecurityContextClass = Class.forName(className);
            } catch (IOException | ClassNotFoundException iox) {
                ; // Ignore IOException
            }
        }
    }

    public static void clear() {
        instance.remove();
    }

    public static ServerSecurityContext getInstance() {
        if (instance.get() == null) {
            createServerSecurityContext();
        }
        return instance.get();
    }

    public static boolean isGTL() {
        return getInstance().getCompanyId().equals("90826");
    }

    public static void setServerSecurityContext(ServerSecurityContext context) {
        instance.set(context);
    }

    private static void createServerSecurityContext() {
        try {
            ServerSecurityContext context = (ServerSecurityContext) serverSecurityContextClass.newInstance();
            instance.set(context);
            context.setStartDate(new Date().getTime());
        } catch (Throwable t) {
            throw new RuntimeException("Error creating ServerSecurityContext instance", t);
        }

    }

    public abstract String getDatabase();

    public abstract void setDatabase(String database);

    public abstract void setSessionId(String sessionId);

    public abstract String getSessionId();

    public abstract void setSuperUser(Boolean sessionId);

    public abstract Boolean isSuperUser();

    public abstract Integer getStaticUserID();

    public abstract void setCompanyId(String companyId);

    public abstract void setCompanyId(Integer companyId);

    public abstract void setStaticUserID(Integer staticUserID);

    public abstract void removeCompanyId();

    public abstract String getCompanyId();

    public abstract Object getUser();

    public abstract boolean isLoggedIn();

    public abstract void setDummySessionId(String session);

    public abstract void setStartDate(long startDate);

    public abstract long getStartDate();

    public abstract void setUniqueID(String uniqueID);

    public abstract String getUniqueID();

    public abstract Locale getUserLocale();

    public abstract void setUserLocale(Locale userLocale);

    public void expireServerSession() {

    };

    public abstract KpiLog getKpiLog();

	public abstract String getVersion();

	public abstract void setVersion(String version);

    public abstract String getSource();

    public abstract void setSource(String source);

    public abstract Integer getUserId();
}
