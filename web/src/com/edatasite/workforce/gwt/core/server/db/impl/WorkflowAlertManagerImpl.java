package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAlert;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WorkflowAlertManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("workflowAlertManager")
public class WorkflowAlertManagerImpl extends BaseManager<EdsWorkflowAlert> implements WorkflowAlertManager {

    public WorkflowAlertManagerImpl() {
        super(EdsWorkflowAlert.class);
    }

    @Override
    public List<EdsWorkflowAlert> list(ListingFilterParameter filterParametr) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select alert from EdsWorkflowAlert alert where ").append(ServerUtils.checkForDeleted("deleted"));
        if (filterParametr != null && filterParametr.getWorkflowID() != null) {
            buffer.append(" and alert.workflow.objectID = ").append(filterParametr.getWorkflowID());
        }
        return find(buffer.toString());
    }

    @Override
    public void removeCFFromAlerts(String form_id, String field_id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(getCompanyId()).append(".workflow_alerts set content = replace(content,'${" + field_id + "}',''),subject = replace(subject, '${" + field_id + "}','') ");
        sql.append("WHERE workflow in (SELECT id FROM ").append(getCompanyId()).append(".workflowrule WHERE deleted is not true AND module = '" + "_WORKFLOW_MODULE_" + form_id.replace("_FORM", "") + "')");
        updateNative(sql.toString());
    }

    @Override
    public boolean hasAlertsByRoleID(Integer roleID) {
        Long count = (Long) findSingle("select count(alert.objectID) from EdsWorkflowAlert alert where " + ServerUtils.checkForDeleted("alert.deleted") + " and alert.workflow.objectID = " + roleID);
        return count != null && count > 0;
    }

    @Override
    public void updateFromEmail(EdsEmailSetting setting) {
        update("update EdsWorkflowAlert alert set alert.emailSetting = ? where alert.emailSetting.active <> true", setting);
    }
}
