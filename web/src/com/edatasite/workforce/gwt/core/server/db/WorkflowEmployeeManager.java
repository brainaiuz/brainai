package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 4/26/16.
 */
public interface WorkflowEmployeeManager extends Manager<EdsWorkflowEmployee> {
    List<EdsWorkflowEmployee> list(ListingFilterParameter fp);

    void deleteByIDs(String iDs);
}
