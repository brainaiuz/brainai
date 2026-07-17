package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientProjectList implements IsSerializable {

    private ClientProjectListItem[] result;
    private int totalCount;

    public ClientProjectList() {

    }

    public ClientProjectList(ClientProjectListItem[] result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
    }

    public ClientProjectListItem[] getResult() {
        return result;
    }

    public void setResult(ClientProjectListItem[] result) {
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
