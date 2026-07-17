package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowUpdateField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowUpdateFieldManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("workflowUpdateFieldManager")
public class WorkflowUpdateFieldManagerImpl extends BaseManager<EdsWorkflowUpdateField> implements WorkflowUpdateFieldManager {

    public WorkflowUpdateFieldManagerImpl() {
        super(EdsWorkflowUpdateField.class);
    }

    @Override
    public List<EdsWorkflowUpdateField> list(ListingFilterParameter filterParametr) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select alert from EdsWorkflowUpdateField alert where 1=1 ");
        if (filterParametr != null && filterParametr.getWorkflowID() != null) {
            buffer.append(" and alert.workflowID = ").append(filterParametr.getWorkflowID());
        }
        return find(buffer.toString());
    }

    @Override
    public void removeCFUpdateFields(String form_id, String field_id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getCompanyId()).append(".workflow_update_field where formid = '" + form_id + "' AND fieldid = '" + field_id + "'");
        updateNative(sql.toString());
    }
}
