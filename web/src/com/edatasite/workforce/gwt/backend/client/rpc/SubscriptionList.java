package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionList implements IsSerializable {

    private SubscriptionListItem[] results;
    private int totalCount;

    public SubscriptionList() {

    }

    public SubscriptionList(SubscriptionListItem[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public SubscriptionListItem[] getResults() {
        return results;
    }

    public void setResults(SubscriptionListItem[] results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }

}
