package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowEmployeeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Azazello on 4/26/16.
 */
@Repository("workflowEmployeeManager")
public class WorkflowEmployeeManagerImpl extends BaseManager<EdsWorkflowEmployee> implements WorkflowEmployeeManager {
    public WorkflowEmployeeManagerImpl() {
        super(EdsWorkflowEmployee.class);
    }

    @Override
    public List<EdsWorkflowEmployee> list(ListingFilterParameter fp) {
        return findInterval("SELECT we FROM EdsWorkflowEmployee we WHERE we.workflowID=" + fp.getWorkflowID(), fp.getStart(), fp.getLimit());
    }

    @Override
    public void deleteByIDs(String iDs) {
        updateNative("DELETE FROM " + getCompanyId() + ".workflowEmployee WHERE id IN (" + iDs + ")");
    }
}
