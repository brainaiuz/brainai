package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsWorkflowMessage;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 7/11/2017.
 */
public interface WorkflowMessageManager extends Manager<EdsWorkflowMessage> {
    Integer getTotalCount(ListingFilterParameter filterParameter, boolean isWorkflowMessages);

    List<EdsSuperMessage> getList(ListingFilterParameter filterParameter, boolean isWorkflowMessages);
}
