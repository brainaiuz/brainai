package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 24.08.2010
 * Time: 11:28:25
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnReportListData implements IsSerializable {
    private VatReturnTransferObject[] items;
    private int totalCount;

    public VatReturnReportListData() {
    }

    public VatReturnReportListData(VatReturnTransferObject[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public VatReturnTransferObject[] getItems() {
        return items;
    }

    public void setItems(VatReturnTransferObject[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
