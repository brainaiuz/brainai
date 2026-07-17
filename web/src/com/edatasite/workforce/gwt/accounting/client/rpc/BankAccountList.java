package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 27.02.2009
 * Time: 20:55:27
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountList implements IsSerializable {

    private BankAccount[] results;
    private int totalCount;

    public BankAccountList() {
    }

    public BankAccountList(BankAccount[] result, int totalCount) {
        this.results = result;
        this.totalCount = totalCount;
    }

    public BankAccount[] getResults() {
        return results;
    }

    public void setResults(BankAccount[] results) {
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
}
