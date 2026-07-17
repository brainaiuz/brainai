package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 16.09.2009
 * Time: 13:31:54
 * To change this template use File | Settings | File Templates.
 */
public class ErrorPageList implements IsSerializable {
    private int totalCount;
    private ErrorPageListItem[] errorPageListItems;

    public ErrorPageList() {
    }

    public ErrorPageList(int totalCount, ErrorPageListItem[] errorPageListItems) {
        this.totalCount = totalCount;
        this.errorPageListItems = errorPageListItems;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ErrorPageListItem[] getErrorPageListItems() {
        return errorPageListItems;
    }

    public void setErrorPageListItems(ErrorPageListItem[] errorPageListItems) {
        this.errorPageListItems = errorPageListItems;
    }

    public ListData getListData() {
        return new ListData(errorPageListItems, totalCount);
    }
}
