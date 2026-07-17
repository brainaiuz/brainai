package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 10, 2009
 * Time: 11:22:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityList implements IsSerializable {
    private int totalCount;
    private OpportunityListItem[] opportunityListItem;

    public OpportunityList() {
    }

    public OpportunityList(OpportunityListItem[] opportunityListItem, int totalCount) {
        this.opportunityListItem = opportunityListItem;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public OpportunityListItem[] getOpportunityListItems() {
        return opportunityListItem;
    }

    public void setOpportunityListItem(OpportunityListItem[] opportunityListItem) {
        this.opportunityListItem = opportunityListItem;
    }

    public ListData getListData() {
        return new ListData(opportunityListItem, totalCount);
    }
}