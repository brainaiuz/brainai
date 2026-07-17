package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.06.2009
 * Time: 22:55:13
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogList implements IsSerializable {
    private int totalCount;
    private AccessLogListItem[] accessLogListItems;

    public AccessLogList() {
    }

    ;

    public AccessLogList(int totalCount, AccessLogListItem[] accessLogListItems) {
        this.totalCount = totalCount;
        this.accessLogListItems = accessLogListItems;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public AccessLogListItem[] getAccessLogListItems() {
        return accessLogListItems;
    }

    public void setAccessLogListItems(AccessLogListItem[] accessLogListItems) {
        this.accessLogListItems = accessLogListItems;
    }

    public ListData getListData() {
        return new ListData(accessLogListItems, totalCount);
    }
}
