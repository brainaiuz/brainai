package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowInvoice;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowInvoiceManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Azazello on 10/6/16.
 */
@Repository("workflowInvoiceManager")
public class WorkflowInvoiceManagerImpl extends BaseManager<EdsWorkflowInvoice> implements WorkflowInvoiceManager {
    public WorkflowInvoiceManagerImpl() {
        super(EdsWorkflowInvoice.class);
    }

    @Override
    public List<EdsWorkflowInvoice> list(ListingFilterParameter fp) {
        return findInterval("SELECT wi FROM EdsWorkflowInvoice wi WHERE wi.workflowID=" + fp.getWorkflowID(), fp.getStart(), fp.getLimit());
    }

}
