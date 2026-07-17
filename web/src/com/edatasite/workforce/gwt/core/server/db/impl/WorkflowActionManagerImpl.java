package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowActionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shohruh on 23-Mar-17.
 */
@Repository("workflowActionManager")
public class WorkflowActionManagerImpl extends BaseManager<EdsWorkflowAction> implements WorkflowActionManager {
    public WorkflowActionManagerImpl() {
        super(EdsWorkflowAction.class);
    }

    @Override
    public List<EdsWorkflowAction> list(ListingFilterParameter fp) {
        return find("from EdsWorkflowAction where workflowId = ?", fp.getWorkflowID());
    }
}
