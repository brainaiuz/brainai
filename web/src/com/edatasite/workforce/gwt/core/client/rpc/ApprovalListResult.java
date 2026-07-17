package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by dilsh0d on 11.05.16.
 */
public class ApprovalListResult extends ListResult<ApproverItem> {

    private boolean hierarchicalApproval = false;

    public ApprovalListResult(){}

    public ApprovalListResult(ArrayList<ApproverItem> list) {
        super(list, list != null ? list.size() : 0, null);
    }

    public boolean isHierarchicalApproval() {
        return hierarchicalApproval;
    }

    public void setHierarchicalApproval(boolean hierarchicalApproval) {
        this.hierarchicalApproval = hierarchicalApproval;
    }
}
