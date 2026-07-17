package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 2:46:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrandList implements IsSerializable {
    private BrandItem[] items;
    private int totalCount;

    public BrandItem[] getItems() {
        return items;
    }

    public void setItems(BrandItem[] items) {
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
