package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 5:03:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountList implements IsSerializable {

    private int totalCount;
    private DiscountItem[] items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public DiscountItem[] getItems() {
        return items;
    }

    public void setItems(DiscountItem[] items) {
        this.items = items;
    }

    public ListData getListData() {
        return new ListData(items, totalCount);
    }
}
