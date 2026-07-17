package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowSMSAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 4/23/15.
 */
public interface WorkflowSMSAlertManager extends Manager<EdsWorkflowSMSAlert> {
    List<EdsWorkflowSMSAlert> list(ListingFilterParameter filterParametr);
}
