package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 16:57:11
 * To change this template use File | Settings | File Templates.
 */
public class WFTPlaginList implements IsSerializable {

    private int totalCount;
    private WFTPlaginListItem[] plaginListItems;

    public WFTPlaginList() {
    }

    public WFTPlaginList(int totalCount, WFTPlaginListItem[] plaginListItems) {
        this.totalCount = totalCount;
        this.plaginListItems = plaginListItems;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public WFTPlaginListItem[] getPlaginListItems() {
        return plaginListItems;
    }

    public void setPlaginListItems(WFTPlaginListItem[] plaginListItems) {
        this.plaginListItems = plaginListItems;
    }

    public ListData getListData() {
        return new ListData(plaginListItems, totalCount);
    }
}
