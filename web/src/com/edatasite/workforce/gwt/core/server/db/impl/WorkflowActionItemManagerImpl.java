package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowActionItem;
import com.edatasite.workforce.gwt.core.server.db.WorkflowActionItemManager;
import org.springframework.stereotype.Repository;

/**
 * Created by shohruh on 25-Mar-17.
 */
@Repository("workflowActionItemManager")
public class WorkflowActionItemManagerImpl extends BaseManager<EdsWorkflowActionItem> implements WorkflowActionItemManager {
    public WorkflowActionItemManagerImpl() {
        super(EdsWorkflowActionItem.class);
    }
}
