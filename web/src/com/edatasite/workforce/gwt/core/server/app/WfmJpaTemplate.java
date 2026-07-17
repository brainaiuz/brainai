package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.hibernate.Session;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.springframework.integration.jpa.core.DefaultJpaOperations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 29.10.2008
 * Time: 13:37:00
 */
public class WfmJpaTemplate extends DefaultJpaOperations implements WfmJpaOperations {

    private EntityManagerFactory entityManagerFactory;
    public final static int SOLR_FACET_LIMIT = 250;

    private static Map<String, HttpSolrClient> defaultCores = new HashMap<>();

    private static Map<String, HttpSolrClient> freeCores = new HashMap<>();

    private static Map<String, HttpSolrClient> paidCores = new HashMap<>();

    private static Map<String, HttpSolrClient> paid1Cores = new HashMap<>();


    public final static int flushLimit = 50;

    private static String getSolrUrl() {
        String solrUrl;

        if (EdsContextParams.getSolrUrl() != null) {
            solrUrl = EdsContextParams.getSolrUrl();
        } else {
            throw new RuntimeException("Solr host not found");
        }
        System.out.println("Solr URL:" + solrUrl);
        return solrUrl;
    }

    public static SolrClient getSolrServerForCore(String coreName) {
        String database = ServerSecurityContext.getInstance().getDatabase();
        String solrUrl = null;

        if (Constants.DATABASE_FREE.equals(database)) {
            solrUrl = SpringPropertiesUtil.getProperty("cluster.solr.free.url");
            return getRequestedCore(solrUrl, coreName, freeCores);
        } else if (Constants.DATABASE_PAID.equals(database)) {
            solrUrl = SpringPropertiesUtil.getProperty("cluster.solr.paid.url");
            return getRequestedCore(solrUrl, coreName, paidCores);
        } else if (Constants.DATABASE_PAID1.equals(database)) {
            solrUrl = SpringPropertiesUtil.getProperty("cluster.solr.paid1.url");
            return getRequestedCore(solrUrl, coreName, paid1Cores);
        }

        //Get solr url from database if not found in properties;
        return getRequestedCore(getSolrUrl(), coreName, defaultCores);
    }

    public static String getDataBaseURL() {
        String database = ServerSecurityContext.getInstance().getDatabase();
        if (Constants.DATABASE_FREE.equals(database)) {
            return SpringPropertiesUtil.getProperty("tenant.datasource.master.FREE.url");
        } else if (Constants.DATABASE_PAID.equals(database)) {
            return SpringPropertiesUtil.getProperty("tenant.datasource.master.PAID.url");
        } else if (Constants.DATABASE_PAID1.equals(database)) {
            return SpringPropertiesUtil.getProperty("tenant.datasource.master.PAID1.url");
        } else if (Constants.DATABASE_GLOBAL_AUTH.equals(database)) {
            return SpringPropertiesUtil.getProperty("globalauth.datasource.url");
        } /*else if (Constants.DATABASE_AWS_PAID.equals(database)) {
            return SpringPropertiesUtil.getProperty("aws.paidb.url");
        } else if (Constants.DATABASE_AWS_FREE.equals(database)) {
            return SpringPropertiesUtil.getProperty("aws.freedb.url");
        }*/

        return null;
    }

    public static String[] getTomcatUrls() {
        String tomcatUrls = SpringPropertiesUtil.getProperty("tomcat.urls");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, tomcatUrls.split(";"));
        return list.toArray(new String[0]);
    }

    private static HttpSolrClient getRequestedCore(String solrUrl, String coreName, final Map<String, HttpSolrClient> solrCore) {
        HttpSolrClient requestedCore;
            HttpSolrClient.Builder builder = new HttpSolrClient.Builder();
            builder.withBaseSolrUrl(solrUrl + coreName);
            requestedCore = builder.build();
            requestedCore.setSoTimeout(1200000);  // socket read timeout
            requestedCore.setConnectionTimeout(120000);
            solrCore.put(coreName, requestedCore);
        return requestedCore;
    }

    @Override
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        super.setEntityManagerFactory(entityManagerFactory);
        this.entityManagerFactory = entityManagerFactory;
    }

    public Session getHibernateSession() {
        return getEntityManager().unwrap(Session.class);
    }

    public Session createHibernateSession() {
        return entityManagerFactory.createEntityManager().unwrap(Session.class);
    }

    public EntityManager getHibernateEntityManager() {
        return getEntityManager();
    }

    public EntityManager createHibernateEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
