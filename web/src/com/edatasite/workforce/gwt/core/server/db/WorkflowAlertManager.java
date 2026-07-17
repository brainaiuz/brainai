package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface WorkflowAlertManager extends Manager<EdsWorkflowAlert> {
    List<EdsWorkflowAlert> list(ListingFilterParameter filterParametr);

    void removeCFFromAlerts(String form_id, String field_id);

    boolean hasAlertsByRoleID(Integer roleID);

    void updateFromEmail(EdsEmailSetting setting);
}
