package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Aug 11, 2010
 * Time: 2:28:55 PM
 */
@Transactional
public class LeadCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsCrmContact> TYPE = new WfmType<>(EventTypes.leadCustomEventListener);
    public static String EVENT_LEAD_ADD_TO_SOLR = "LEAD_ADD_TO_SOLR";
    public static String EVENT_LEAD_DELETE_FROM_SOLR = "LEAD_DELETE_FROM_SOLR";
    public static String EVENT_LEAD_DELETE_ALL_FROM_SOLR_BY_IDS = "LEAD_DELETE_ALL_FROM_SOLR_BY_IDS";
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_LEAD_ADD_TO_SOLR.equals(event.getEventType())) {
            onAdd(event);
        } else if (EVENT_LEAD_DELETE_FROM_SOLR.equals(event.getEventType())) {
            onDelete(event);
        } else if (EVENT_LEAD_DELETE_ALL_FROM_SOLR_BY_IDS.equals(event.getEventType())) {
            deleteByIds(event);
        }
    }

    private void deleteByIds(EdsBusinessEvent event) {
        try {
            List<Integer> ids = new ArrayList<>();
            if (event.getCustomStringField() != null && !"".equals(event.getCustomStringField())) {
                for (String id_ : event.getCustomStringField().split(",")) {
                    try {
                        ids.add(Integer.parseInt(id_));
                    } catch (NumberFormatException e) {

                    }
                }
            }
            if (ids.size() > 0) {
                solrManager.removeCompanyLeadByIds(ids.toArray(new Integer[]{}));
            }
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsCrmContact lead = crmContactManager.get(event.getEntityID());
        try {
            contactSolrComponent.index(lead);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onDelete(EdsBusinessEvent event) {
        try {
            solrManager.removeCompanyLeadByIds(event.getEntityID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }
}
