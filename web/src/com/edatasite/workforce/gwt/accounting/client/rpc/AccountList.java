package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Mar 31, 2009
 * Time: 5:05:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountList implements IsSerializable {
    private AccountListItem[] results;
    private int totalCount;
    private String params;

    public AccountList() {
    }

    public AccountList(AccountListItem[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public AccountListItem[] getResults() {
        return results;
    }

    public void setResults(AccountListItem[] results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }
}
