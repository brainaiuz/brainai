package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowUpdateField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface WorkflowUpdateFieldManager extends Manager<EdsWorkflowUpdateField> {
    List<EdsWorkflowUpdateField> list(ListingFilterParameter filterParametr);

    void removeCFUpdateFields(String form_id, String field_id);
}
