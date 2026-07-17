package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Sher
 * Date: 10.08.2010
 * Time: 14:13:39
 */
@Transactional
public class LeadEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsCrmContact> TYPE = new WfmType<>(EventTypes.leadEventListener);
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        Integer leadId = event.getEntityID();
        EdsCrmContact lead = crmContactManager.get(leadId);
        try {
            contactSolrComponent.index(lead);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}