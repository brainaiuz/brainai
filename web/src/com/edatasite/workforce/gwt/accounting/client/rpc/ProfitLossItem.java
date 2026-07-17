package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 03.03.2009
 * Time: 12:10:40
 * To change this template use File | Settings | File Templates.
 */
public class ProfitLossItem implements IsSerializable {
    private ProfitLossInnerItem[] items;
    private ProfitLossInnerItem total;

    public ProfitLossInnerItem[] getItems() {
        return items;
    }

    public void setItems(ProfitLossInnerItem[] items) {
        this.items = items;
    }

    public ProfitLossInnerItem getTotal() {
        return total;
    }

    public void setTotal(ProfitLossInnerItem total) {
        this.total = total;
    }
}
