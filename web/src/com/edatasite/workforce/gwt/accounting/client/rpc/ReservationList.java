package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 4:28:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationList implements IsSerializable {
    private ReservationItem[] items;
    private int totalCount;

    public ReservationItem[] getItems() {
        return items;
    }

    public void setItems(ReservationItem[] items) {
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

