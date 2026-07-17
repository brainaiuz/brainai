package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/5/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CustomFieldCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCustomFields> TYPE = new WfmType<>(EventTypes.customFieldCustomEventListener);

    public static final String EVENT_INDEX_CRM_LEAD_TO_SOLR = "EVENT_INDEX_CRM_LEAD_TO_SOLR";
    public static final String EVENT_INDEX_CRM_CANDIDATE_TO_SOLR = "EVENT_INDEX_CRM_CANDIDATE_TO_SOLR";
    public static final String EVENT_INDEX_CRM_CONTACT_TO_SOLR = "EVENT_INDEX_CRM_CONTACT_TO_SOLR";
    public static final String EVENT_INDEX_CRM_CASE_TO_SOLR = "EVENT_INDEX_CRM_CASE_TO_SOLR";
    public static final String EVENT_INDEX_CRM_ACCOUNT_TO_SOLR = "EVENT_INDEX_CRM_ACCOUNT_TO_SOLR";
    public static final String EVENT_INDEX_OPPORTUNITY_TO_SOLR = "EVENT_INDEX_OPPORTUNITY_TO_SOLR";
    public static final String EVENT_INDEX_TASK_TO_SOLR = "EVENT_INDEX_TASK_TO_SOLR";
    public static final String EVENT_INDEX_PROJECT_TO_SOLR = "EVENT_INDEX_PROJECT_TO_SOLR";
    public static final String EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR = "EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR";

    @Autowired
    private BackendServiceLocal backendServiceLocal;

    @Autowired
    private UserManager userManager;

    @Override
	@Transactional(propagation = Propagation.SUPPORTS)
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());

        SolrReindexRpc reindexRpc = new SolrReindexRpc();
        reindexRpc.setCompanyId(user.getCompany().getObjectID());

        if (EVENT_INDEX_CRM_LEAD_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexLeads(reindexRpc);
        } else if (EVENT_INDEX_CRM_CANDIDATE_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCandidates(reindexRpc);
        } else if (EVENT_INDEX_CRM_CONTACT_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCompanyContactToSolr(reindexRpc);
        } else if (EVENT_INDEX_CRM_CASE_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCompanyCrmCase(reindexRpc);
        } else if (EVENT_INDEX_CRM_ACCOUNT_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCompanyAccountToSolr(reindexRpc);
        } else if (EVENT_INDEX_OPPORTUNITY_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCompanyOpportunity(reindexRpc);
        } else if (EVENT_INDEX_TASK_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.reindexCompanyTasks(reindexRpc);
        } else if (EVENT_INDEX_PROJECT_TO_SOLR.equals(event.getEventType())) {
            backendServiceLocal.indexCompanyProjects(reindexRpc);
        } else if (EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR.equals(event.getEventType())) {
            reindexRpc.setFormID(event.getCustomStringField());
            backendServiceLocal.indexCustomFormItems(reindexRpc);
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
