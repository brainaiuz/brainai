package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 15, 2010
 * Time: 9:09:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionManagementItemList implements IsSerializable {

    private int totalCount;
    private SubscriptionManagementItem[] listItems;

    public SubscriptionManagementItemList() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public SubscriptionManagementItem[] getListItems() {
        return listItems;
    }

    public void setListItems(SubscriptionManagementItem[] listItems) {
        this.listItems = listItems;
    }

    public ListData getListData() {
        return new ListData(listItems, totalCount);
    }
}
