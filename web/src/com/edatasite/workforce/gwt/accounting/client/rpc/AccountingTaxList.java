package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 24.06.2010
 * Time: 20:18:22
 * To change this template use File | Settings | File Templates.
 */
public class AccountingTaxList implements IsSerializable {
    private TaxListItem[] items;
    private int totalCount;

    public AccountingTaxList() {
    }

    public AccountingTaxList(TaxListItem[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public TaxListItem[] getItems() {
        return items;
    }

    public void setItems(TaxListItem[] items) {
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
