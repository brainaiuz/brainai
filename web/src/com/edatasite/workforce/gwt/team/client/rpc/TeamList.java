package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TeamList implements IsSerializable {

    private TeamListItem[] results;
    private int totalCount;
    private String params;

    public TeamList() {
    }

    public TeamList(TeamListItem[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public TeamListItem[] getResults() {
        return results;
    }

    public void setResults(TeamListItem[] results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
