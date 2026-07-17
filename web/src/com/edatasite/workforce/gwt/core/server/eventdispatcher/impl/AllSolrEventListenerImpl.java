package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATABASE_FREE;

public class AllSolrEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsCompany> TYPE = new WfmType<>(EventTypes.allSolrEventListener);

    private static final Logger log = LoggerFactory.getLogger(AllSolrEventListenerImpl.class);

    @Autowired
    private BackendService backendService;
    @Autowired
    private CompanyManager companyManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        Integer companyId = event.getEntityID();
        try {
            log.info("@Reindexing sample date for company solr cores => {}", companyId);
            SecurityContext.getInstance().setDatabase(DATABASE_FREE);

            SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
            solrReindexRpc.setCompanyId(companyId);
            backendService.indexAllCoresOfSelectedCompany(solrReindexRpc);
            if (event.getCustomStringField() != null && "TRUE".equals(event.getCustomStringField())) {
                log.info("@Make it test company: {}", companyId);
                companyManager.updateTestCompany(companyId);
            }
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }
}
