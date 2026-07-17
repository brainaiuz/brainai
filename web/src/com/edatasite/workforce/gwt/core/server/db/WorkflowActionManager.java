package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by shohruh on 23-Mar-17.
 */
public interface WorkflowActionManager extends Manager<EdsWorkflowAction> {
    List<EdsWorkflowAction> list(ListingFilterParameter fp);
}
