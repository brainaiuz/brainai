package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 17, 2010
 * Time: 12:09:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementList implements IsSerializable {
    private UnitMeasurementItem[] items;
    private int totalCount;

    public UnitMeasurementItem[] getItems() {
        return items;
    }

    public void setItems(UnitMeasurementItem[] items) {
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
