package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Aug 11, 2010
 * Time: 3:38:20 PM
 */
@Transactional
public class CrmContactCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCrmContact> TYPE = new WfmType<>(EventTypes.crmContactCustomEventListener);
    public static WfmType<EdsOpportunity> TYPE_OPPORTUNITY = new WfmType<>("opportunityCustomEventListenerString");
    public static WfmType<EdsCrmAccount> TYPE_ACCOUNT = new WfmType<>("crmAccountCustomEventListenerString");
    public static String EVENT_ADD_CRM_CONTACT_TO_SOLR = "ADD_CRM_CONTACT_TO_SOLR";
    public static String EVENT_DELETE_CRM_CONTACT_FROM_SOLR = "DELETE_CRM_CONTACT_FROM_SOLR";
    public static String EVENT_CONTACT_CATEGORY_UPDATED = "CONTACT_CATEGORY_UPDATED";
    public static final String EVENT_DELETE_CRM_CONTACT_FROM_SOLR_BATCH = "DELETE_CRM_CONTACT_FROM_SOLR_BATCH";
    public static final String EVENT_CRM_ACCOUNT_NAME_CHANGED = "CRM_ACCOUNT_NAME_CHANGED";
    public static final String EVENT_CRM_ACCOUNT_ADDRESS_CHANGED = "CRM_ACCOUNT_ADDRESS_CHANGED";
    public static final String EVENT_REINDEX_CONTACT_RELATIONS = "REINDEX_CONTACT_RELATIONS";

    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_ADD_CRM_CONTACT_TO_SOLR.equals(event.getEventType())) {
            onAdd(event);
        } else if (EVENT_DELETE_CRM_CONTACT_FROM_SOLR.equals(event.getEventType())) {
            onDelete(event);
        } else if (EVENT_CONTACT_CATEGORY_UPDATED.equals(event.getEventType())) {
            onCategoryUpdated(event);
        } else if (EVENT_DELETE_CRM_CONTACT_FROM_SOLR_BATCH.equals(event.getEventType())) {
            onMassDelete(event);
        } else if (EVENT_CRM_ACCOUNT_NAME_CHANGED.equals(event.getEventType()) || EVENT_CRM_ACCOUNT_ADDRESS_CHANGED.equals(event.getEventType())) {
            onCrmAccountChanged(event);
        } else if (EVENT_REINDEX_CONTACT_RELATIONS.equals(event.getEventType())) {
            onReindexContactRelations(event);
        }
    }

    private void onCrmAccountChanged(EdsBusinessEvent event) {
        EdsCrmAccount account = crmAccountManager.get(event.getEntityID());
        if (account != null) {
            if (EVENT_CRM_ACCOUNT_ADDRESS_CHANGED.equals(event.getEventType())) {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LINK_ACCOUNT_ADDRESSES)) {
                    updateContactsAddresses(account);
                }
            }
            List<EdsCrmContact> contacts = crmContactManager.getContactsByCrmAccount(account.getObjectID());
            List<EdsCrmAccount> accounts = crmAccountManager.getAllSubAccounts(account, true);
            if (contacts != null && !contacts.isEmpty()) {
                try {
                    contactSolrComponent.indexes(contacts);
                    event.setStatus(EventStatus.COMPLETED.name());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
            if (accounts != null && !accounts.isEmpty()) {
                try {
                    crmAccountSolrComponent.indexes(accounts);
                    event.setStatus(EventStatus.COMPLETED.name());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
        }
    }

    private void updateContactsAddresses(EdsCrmAccount account) {
        addressManager.updateLinkedAddresses(account);
    }

    private void onCategoryUpdated(EdsBusinessEvent event) {
        EdsContactCategory category_ = contactCategoryManager.get(event.getEntityID());
        if (category_ != null) {
            List<EdsContactCategory> categoryList = EdsContactCategory.asList(true, category_);
            int limit = 500;
            int start = 0;
            for (EdsContactCategory category : categoryList) {
                List<EdsCrmContact> contacts = new ArrayList<>();
                do {
                    contacts = crmContactManager.getContactsByCategoryIDs(new ArrayList<>(category.getObjectID()), start, limit);
                    try {
                        contactSolrComponent.indexes(contacts);
                        event.setStatus(EventStatus.COMPLETED.name());
                    } catch (InterruptedException e) {
                        event.setStatus(EventStatus.FAILED.name());
                    }
                    start = contacts.size() > 0 ? contacts.get(contacts.size() - 1).getObjectID() : start;
                } while (contacts.size() > 0);
            }
        }
    }

    private void onReindexContactRelations(EdsBusinessEvent event) {
        HashMap<String, ArrayList<Integer>> fromIDs = new HashMap<>();
        List<EdsRelation> relations = relationManager.getAllRelations(RelationItem.TYPE_CONTACT, event.getEntityID());
        if (relations != null && !relations.isEmpty()) {
            for (EdsRelation relation : relations) {
                if (!fromIDs.containsKey(relation.getFromType())) {
                    fromIDs.put(relation.getFromType(), new ArrayList<>());
                }
                fromIDs.get(relation.getFromType()).add(relation.getFromID());
            }
            event.setStatus(EventStatus.COMPLETED.name());
        }
        try {
            relationManager.updateSolr(fromIDs);
        } catch (InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void onDelete(EdsBusinessEvent event) {
        try {
            solrManager.removeCompanyCrmContactBuIds(event.getEntityID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onMassDelete(EdsBusinessEvent event) {
        String ids = event.getCustomStringField();
        List<Integer> listOfIDs = ServerUtils.getStringAsList(ids, ",");
        try {
            solrManager.removeCompanyCrmContactBuIds(listOfIDs.toArray(new Integer[]{}));
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
        WebSocketServerObject pushMessage = new WebSocketServerObject();
        pushMessage.setEventType(WfmUiEventType.ON_LEADS_DELETE);
        try {
            Integer userId = ((EdsUser)SecurityContext.getInstance().getUser()).getObjectID();
            pushMessage.setUserId(userId);
            rabbitMQService.sendWebPushNotification(pushMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsCrmContact contact = crmContactManager.get(event.getEntityID());
        try {
            contactSolrComponent.index(contact);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }
}
