package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowPush;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 10/15/15.
 */
public interface WorkflowPushManager extends Manager<EdsWorkflowPush> {
    void deletePushs(ArrayList<Integer> ids);

    List<EdsWorkflowPush> list(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);
}
