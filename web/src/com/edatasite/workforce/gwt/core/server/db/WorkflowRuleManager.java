package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface WorkflowRuleManager extends Manager<EdsWorkflowRule> {

    List<EdsWorkflowRule> list(ListingFilterParameter fp);

    Integer listCount(ListingFilterParameter fp);

    List<EdsWorkflowRule> getByModuleAndActions(String module, WorkflowExecutionCriteriaEnum... actions);

    List<Object[]> getListForActivities(ListingFilterParameter fp);

    Integer getActivitiesListCount();

    List<Integer> getRecurrenceAlertIdsByContactId(Integer contactID);
}
