package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 27.11.2008
 * Time: 16:31:48
 * To change this template use File | Settings | File Templates.
 */
public class HistoryList implements IsSerializable {

    private HistoryListItem[] result;
    private int totalCount;

    public HistoryList() {

    }

    public HistoryListItem[] getResult() {
        return result;
    }

    public void setResult(HistoryListItem[] result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public HistoryList(HistoryListItem[] result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(result, totalCount);
    }

}
