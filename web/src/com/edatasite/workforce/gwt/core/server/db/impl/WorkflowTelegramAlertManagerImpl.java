package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowTelegramAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WorkflowTelegramAlertManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("workflowTelegramAlertManager")
public class WorkflowTelegramAlertManagerImpl extends BaseManager<EdsWorkflowTelegramAlert> implements WorkflowTelegramAlertManager {
    public WorkflowTelegramAlertManagerImpl() {
        super(EdsWorkflowTelegramAlert.class);
    }

    @Override
    public List<EdsWorkflowTelegramAlert> list(ListingFilterParameter filterParametr) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select alert from EdsWorkflowTelegramAlert alert where ").append(ServerUtils.checkForDeleted("deleted"));
        if (filterParametr != null && filterParametr.getWorkflowID() != null) {
            buffer.append(" and alert.workflow.objectID = ").append(filterParametr.getWorkflowID());
        }
        return find(buffer.toString());
    }
}
