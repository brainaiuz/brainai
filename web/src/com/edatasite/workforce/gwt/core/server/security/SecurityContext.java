package com.edatasite.workforce.gwt.core.server.security;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.jpa.support.parametersource.ParameterSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 2, 2008
 * Time: 9:55:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class SecurityContext extends ServerSecurityContext {

    public static final String USER_QUERY = "select us.user from EdsUserSession us where us.sessionID=:sessionId and expired = false";
    public static final String USER_QUERY_WITH_ID = "select us from EdsUser us where us.objectID=:userId";
    public static final String EXPIRE_SESSION_QUERY = "update EdsUserSession us set expired = true where sessionID=:sessionId";

    public String sessionId;

    public Boolean superUser;
    private String companyId;
    private Integer staticUserID;
    private String database;
    private long startDate;
    private String uniqueID;
    private Locale userLocale;
    private KpiLog kpiLog;
    private String version;
    private EdsUser user;
    private String source;
    private Integer userId;

    public static Integer getCompanyID() {
        Integer companyID = null;
        try {
            if (getInstance().getCompanyId() != null) {
                companyID = Integer.valueOf(getInstance().getCompanyId());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return companyID;
    }

    public static void setCompanyID(Integer companyId) {
        getInstance().setCompanyId(companyId != null ? companyId.toString() : null);
    }

    public static void removeCompanyID() {
        getInstance().setCompanyId((String) null);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        if (database != null) {
            TenantContextHolder.setCustomerType(database);
            this.database = database;
        } else {
            throw new NullPointerException("Database is set to null");
        }
    }

    public void setDummySessionId(String sessionId) {
        setSessionId(sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void setSuperUser(Boolean superUser) {
        this.superUser =superUser;
    }

    @Override
    public Boolean isSuperUser() {
        return this.superUser;
    }

    public void setSessionId(String string) {
        user = null;
        if (string == null) {
            companyId = null;
            sessionId = null;
            staticUserID = null;
            database = null;
            userLocale = null;
            return;
        }

        String[] splited = string.replace("%24", "$").split("\\$");
        if (splited.length > 1) {
            database = splited[0];
            companyId = splited[1];
            if (splited.length > 2 && splited[2] != null && !"".equals(splited[2]) && splited[2].matches(Constants.REGEX_INTEGER)) {
                staticUserID = Integer.parseInt(splited[2]);
            }
            TenantContextHolder.setCustomerType(database);

            sessionId = string.replace("%24", "$");
        }
        this.kpiLog = new KpiLog();
    }

    public boolean isLoggedIn() {
        return sessionId != null && sessionId.length() > 0;
    }

    /**
     * Lazyly obtains user of the current request. (in thread per request Tomcat model)
     *
     * @return The current EdsUser object of the current session
     */
    public Object getUser() {
        if (user != null) {
            return user;
        }
        try {
            user = (EdsUser) getUserBySession();
            if (user == null && staticUserID != null) {
                user = getUserByUserID(staticUserID);
            }
            if (user == null && sessionId != null && !sessionId.contains("(EXPIRED)")) {//Case when session is expired
                sessionId = sessionId + "(EXPIRED)";
            }
            return user;
        } catch (EntityNotFoundException exception) {
            return null;
        }
    }

    public Integer getUserId() {
        if (userId != null) return userId;
        EdsUser user = (EdsUser) this.getUser();
        userId = user != null ? user.getObjectID() : null;
        return userId;
    }

    private EdsUser getUserByUserID(final Integer staticUserID) {
        WfmJpaOperations jpaTemplate = (WfmJpaOperations) ApplicationContextProvider.applicationContext.getBean("jpaTemplate");
        EntityManager em = jpaTemplate.getHibernateEntityManager();
        EdsUser us = (EdsUser) em.createQuery(USER_QUERY_WITH_ID)
                .setParameter("userId", staticUserID)
                .setMaxResults(1)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        if (us != null) {
            return us;
        }

        try (org.hibernate.Session session = em.unwrap(Session.class)) {
            return (EdsUser) session.createQuery(USER_QUERY_WITH_ID)
                    .setParameter("userId", staticUserID, IntegerType.INSTANCE)
                    .setMaxResults(1)
                    .setCacheable(true)
                    .setCacheRegion("query.user").uniqueResult();
        } catch (HibernateException e) {//This is temp. hack; used outside any transaction when session is closed
            return (EdsUser) jpaTemplate.getSingleResultForQuery(USER_QUERY_WITH_ID, new ParameterSource() {
                @Override
                public boolean hasValue(String s) {
                    return true;
                }

                @Override
                public Object getValue(String s) {
                    return staticUserID;
                }
            });
        } finally {
            em.close();
        }
    }

    private Object getUserBySession() {
        if (!isLoggedIn()) {
            return null;
        }
        WfmJpaOperations jpaTemplate = (WfmJpaOperations) ApplicationContextProvider.applicationContext.getBean("jpaTemplate");
        EntityManager entityManager = jpaTemplate.getHibernateEntityManager();
        Object user = null;
        try {
            user = entityManager.createQuery(USER_QUERY)
                    .setParameter("sessionId", sessionId)
                    .setMaxResults(1)
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
            if (user != null) {
                return user;
            }
        } catch (NoResultException | EmptyResultDataAccessException e) {
            return null;
        } finally {
            entityManager.close();
        }

        SessionFactory sessionFactory = (SessionFactory) ApplicationContextProvider.applicationContext.getBean("sessionFactory");
        Object sessionUser;
        try (Session session = sessionFactory.isClosed() ? sessionFactory.openSession() : sessionFactory.getCurrentSession()) {
            if (session.isOpen()) {
                sessionUser = session.createQuery(USER_QUERY)
                        .setParameter("sessionId", sessionId, StringType.INSTANCE)
                        .setMaxResults(1)
                        .setCacheable(true)
                        .setCacheRegion("query.user")
                        .uniqueResult();
            } else {//This is temp. hack; used outside any transaction when session is closed
                Map<String, Object> map = new HashMap<>();
                map.put("sessionId", sessionId);
                sessionUser = jpaTemplate.getSingleResultForQuery(USER_QUERY, new ParameterSource() {
                    @Override
                    public boolean hasValue(String s) {
                        return true;
                    }

                    @Override
                    public Object getValue(String s) {
                        return sessionId;
                    }
                });
            }
        }
        return sessionUser;
    }

    public Integer getStaticUserID() {
        return staticUserID;
    }

    public void setStaticUserID(Integer staticUserID) {
        this.staticUserID = staticUserID;
        user = null;
    }

    @Override
    public void removeCompanyId() {
        setCompanyId((String) null);
    }

    @Override
    public String getCompanyId() {
        return companyId;
    }

    @Override
    public void setCompanyId(String companyId) {
        if ((this.companyId != null && companyId != null && !this.companyId.equals(companyId)) || companyId == null) {
            setStaticUserID(null);
        }
        this.companyId = companyId;
        if (companyId == null) {
            setSessionId(null);
        }
    }

    public void setCompanyId(Integer companyId) {
        setCompanyId(companyId != null ? companyId.toString() : null);
    }

    @Override
    public long getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Override
    public Locale getUserLocale() {
        return userLocale;
    }

    @Override
    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }

    @Override
    public void expireServerSession() {
        super.expireServerSession();
        if (sessionId == null) {
            return;
        }
        WfmJpaOperations jpaTemplate = (WfmJpaOperations) ApplicationContextProvider.applicationContext.getBean("jpaTemplate");
        EntityManager em = jpaTemplate.getHibernateEntityManager();
        try (org.hibernate.Session session = em.unwrap(Session.class)) {
            session.createQuery(EXPIRE_SESSION_QUERY)
                    .setParameter("sessionId", sessionId, StringType.INSTANCE)
                    .executeUpdate();
        } finally {
            em.close();
        }
    }

    public KpiLog getKpiLog() {
        if (kpiLog == null) {
            kpiLog = new KpiLog();
        }
        return kpiLog;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }
}
