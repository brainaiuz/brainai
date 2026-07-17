package com.edatasite.workforce.gwt.profile.client.rpc.locking;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TransactionLockingListData implements IsSerializable {
    private ListResult<TransactionLockingListItem> list;

    public ListResult<TransactionLockingListItem> getList() {
        return list;
    }

    public void setList(ListResult<TransactionLockingListItem> list) {
        this.list = list;
    }
}
