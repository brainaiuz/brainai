package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowCondition;
import com.edatasite.workforce.gwt.core.server.db.WorkflowConditionManager;
import org.springframework.stereotype.Repository;

@Repository("workflowConditionManager")
public class WorkflowConditionManagerImpl extends BaseManager<EdsWorkflowCondition> implements WorkflowConditionManager {

    public WorkflowConditionManagerImpl() {
        super(EdsWorkflowCondition.class);
    }

    @Override
    public void removeCFConditions(String form_id, String field_id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getCompanyId()).append(".workflow_condition WHERE column_id = '" + field_id + "' ");
        sql.append("AND workflow in (SELECT id FROM ").append(getCompanyId()).append(".workflowrule WHERE deleted is not true AND module = '" + "_WORKFLOW_MODULE_" + form_id.replace("_FORM", "") + "')");
        updateNative(sql.toString());
    }
}
