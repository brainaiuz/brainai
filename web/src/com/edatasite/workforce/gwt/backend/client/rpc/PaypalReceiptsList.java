package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alisher
 * Date: 05.05.2010
 * Time: 12:53:24
 * To change this template use File | Settings | File Templates.
 */
public class PaypalReceiptsList implements IsSerializable {
    private int totalCount;
    private PaypalReceiptsListItem[] paypalReceiptsListItems;

    public PaypalReceiptsList() {
    }


    public PaypalReceiptsList(PaypalReceiptsListItem[] paypalReceiptsItems, int totalCount) {
        this.paypalReceiptsListItems = paypalReceiptsItems;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PaypalReceiptsListItem[] getPaypalReceiptsItems() {
        return paypalReceiptsListItems;
    }

    public void setPaypalReceiptsItems(PaypalReceiptsListItem[] paypalReceiptsItems) {
        this.paypalReceiptsListItems = paypalReceiptsItems;
    }

    public ListData getListData() {
        return new ListData(paypalReceiptsListItems, totalCount);
    }
}
