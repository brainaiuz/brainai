package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * User: Abdulaziz
 * Date: Aug 21, 2010
 * Time: 2:47:58 PM
 */
@Component("solrTransactionManager")
public class SolrTransactionManagerImpl implements SolrTransactionManager {
    @Autowired
    private SolrManager solrManager;

    private static final ThreadLocal<ArrayList<SolrEvent>> list = new ThreadLocal<>();

    public <E extends EdsObject> SolrEvent registerEvent(WfmType<E> eventType, E entity, EdsCompany company) {
        if (list.get() == null) {
            list.set(new ArrayList<>());
        }
        SolrEvent event = new SolrEvent(eventType, entity, company);
        list.get().add(event);
        return event;
    }

    public void rollbackTransaction() {
        if (list.get() != null) {
            for (SolrEvent event : list.get()) {
                rollback(event);
            }
        }
    }

    public void clearList() {
        if (list.get() != null) {
            list.get().clear();
        }

    }

    private void rollback(SolrEvent event) {
        try {
            solrManager.rollbackEvent(event);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}
