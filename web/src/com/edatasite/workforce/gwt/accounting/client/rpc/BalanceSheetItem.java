package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 28.02.2009
 * Time: 12:50:00
 * To change this template use File | Settings | File Templates.
 */
public class BalanceSheetItem implements IsSerializable {
    private String name;
    private BalanceSheetInnerItem[] items;
    private BalanceSheetInnerItem total;

    public BalanceSheetItem() {
    }

    public BalanceSheetItem(String name, BalanceSheetInnerItem[] items, BalanceSheetInnerItem total) {
        this.name = name;
        this.items = items;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BalanceSheetInnerItem[] getItems() {
        return items;
    }

    public void setItems(BalanceSheetInnerItem[] items) {
        this.items = items;
    }

    public BalanceSheetInnerItem getTotal() {
        return total;
    }

    public void setTotal(BalanceSheetInnerItem total) {
        this.total = total;
    }
}
