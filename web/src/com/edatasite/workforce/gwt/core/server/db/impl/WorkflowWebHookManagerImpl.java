package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowWebHookManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User : Akhror
 * Date : 11.01.2022
 */
@Repository("workflowWebHookManager")
public class WorkflowWebHookManagerImpl extends BaseManager<EdsWorkflowWebHook> implements WorkflowWebHookManager {
    public WorkflowWebHookManagerImpl() {
        super(EdsWorkflowWebHook.class);
    }

    @Override
    public List<EdsWorkflowWebHook> list(ListingFilterParameter fp) {
        String query = "from EdsWorkflowWebHook w where ";
        if (fp.getWorkflowID() != null) {
            query += "w.workflow.objectID = " + fp.getWorkflowID();
        } else if (fp.isItemTable()) {
            query += "w.itemTableForm = '" + fp.getForm() + "'";
            if (fp.getRelationName() != null) {
                query += " AND w.itemTableUuid = '" + fp.getRelationName() + "'";
            }
        } else {
            query += "w.form.formID = '" + fp.getForm() + "' and w.itemTableForm is null";
        }
        return find(query + " ORDER BY w.id ASC");
    }
}
