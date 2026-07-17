package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowInvoice;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 10/6/16.
 */
public interface WorkflowInvoiceManager extends Manager<EdsWorkflowInvoice> {
    List<EdsWorkflowInvoice> list(ListingFilterParameter fp);
}
