package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowCondition;

public interface WorkflowConditionManager extends Manager<EdsWorkflowCondition> {
    void removeCFConditions(String form_id, String field_id);
}
