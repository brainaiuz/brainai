package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 01.05.12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class CrmCampaignEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsCampaign> TYPE = new WfmType<>(EventTypes.crmCampaignEventListener);
    public static final String EVENT_CAMPAIGN_NAME_CHANGED = "CAMPAIGN_NAME_CHANGED";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_CAMPAIGN_NAME_CHANGED.equals(event.getEventType())) {
            onCampaignNameChanged(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsCampaign campaign = campaignManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCampaignAddUpdate(campaign, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsCampaign campaign = campaignManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCampaignDeleteUpdate(campaign, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsCampaign campaign = campaignManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCampaignEditUpdate(campaign, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onCampaignNameChanged(EdsBusinessEvent event) {
        EdsCampaign campaign = campaignManager.get(event.getEntityID());
        if (campaign != null) {
            List<EdsCrmContact> contacts = crmContactManager.getContactsByCampaign(campaign.getObjectID());
            List<EdsOpportunity> opportunities = opportunityManager.getOpportunitiesByCampaign(campaign.getObjectID());
            if (contacts != null && !contacts.isEmpty()) {
                try {
                    contactSolrComponent.indexes(contacts);
                    event.setStatus(EventStatus.COMPLETED.name());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
            if (opportunities != null && !opportunities.isEmpty()) {
                try {
                    opportunitySolrComponent.indexes(opportunities);
                    event.setStatus(EventStatus.COMPLETED.name());
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
        }
    }


}