package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User : Akhror
 * Date : 11.01.2022
 */
public interface WorkflowWebHookManager extends Manager<EdsWorkflowWebHook> {

    List<EdsWorkflowWebHook> list(ListingFilterParameter fp);
}
