package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowSMSAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WorkflowSMSAlertManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Azazello on 4/23/15.
 */
@Repository("workflowSMSAlertManager")
public class WorkflowSMSAlertManagerImpl extends BaseManager<EdsWorkflowSMSAlert> implements WorkflowSMSAlertManager {
    public WorkflowSMSAlertManagerImpl() {
        super(EdsWorkflowSMSAlert.class);
    }

    @Override
    public List<EdsWorkflowSMSAlert> list(ListingFilterParameter filterParametr) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select alert from EdsWorkflowSMSAlert alert where ").append(ServerUtils.checkForDeleted("deleted"));
        if (filterParametr != null && filterParametr.getWorkflowID() != null) {
            buffer.append(" and alert.workflow.objectID = ").append(filterParametr.getWorkflowID());
        }
        if (filterParametr != null && filterParametr.getProviderID() != null) {
            buffer.append(" and alert.provider.id = ").append(filterParametr.getProviderID());
        }
        return find(buffer.toString());
    }
}
