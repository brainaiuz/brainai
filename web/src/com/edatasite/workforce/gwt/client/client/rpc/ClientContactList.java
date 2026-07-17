package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientContactList implements IsSerializable {

    private ClientContactListItem[] result;
    private int totalCount;

    public ClientContactList() {
    }

    public ClientContactList(ClientContactListItem[] result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
    }

    public ClientContactListItem[] getResult() {
        return result;
    }

    public void setResult(ClientContactListItem[] result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(result, totalCount);
    }
}
