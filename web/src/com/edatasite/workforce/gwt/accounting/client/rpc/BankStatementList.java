package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 11:34:18
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementList implements IsSerializable {
    private BankStatementListItem[] items;
    private int totalCount;

    public BankStatementList() {
    }

    public BankStatementList(BankStatementListItem[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public BankStatementListItem[] getItems() {
        return items;
    }

    public void setItems(BankStatementListItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(items, totalCount);
    }
}
