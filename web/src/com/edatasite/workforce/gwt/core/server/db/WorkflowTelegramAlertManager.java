package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowTelegramAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface WorkflowTelegramAlertManager extends Manager<EdsWorkflowTelegramAlert> {
    List<EdsWorkflowTelegramAlert> list(ListingFilterParameter filterParametr);
}
