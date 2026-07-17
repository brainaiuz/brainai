package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 1, 2010
 * Time: 5:37:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientInvoiceList implements IsSerializable {

    private ClientInvoice[] results;
    private int totalCount;

    public ClientInvoiceList() {
    }

    public ClientInvoiceList(ClientInvoice[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public ClientInvoice[] getResults() {

        return results;
    }

    public void setResults(ClientInvoice[] results) {
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
