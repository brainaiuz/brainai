package com.workforcetrack.mobile.rpc.client;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 21.06.11
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
public class MClientContactList {
    private MClientContactListItem[] result;
    private int totalCount;

    public MClientContactList() {}

    public MClientContactList(MClientContactListItem[] result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
    }

    public MClientContactListItem[] getResult() {
        return result;
    }

    public void setResult(MClientContactListItem[] result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
