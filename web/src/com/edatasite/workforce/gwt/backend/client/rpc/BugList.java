package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 2:40:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugList implements IsSerializable {
    private int totalCount;
    private BugListItem[] bugListItems;

    public BugList() {
    }

    public BugList(BugListItem[] bugListItems, int totalCount) {
        this.bugListItems = bugListItems;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public BugListItem[] getBugListItems() {
        return bugListItems;
    }

    public void setBugListItems(BugListItem[] bugListItems) {
        this.bugListItems = bugListItems;
    }

    public ListData getListData() {
        return new ListData(bugListItems, totalCount);
    }
}
