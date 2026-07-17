package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:56:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingMethodsList implements IsSerializable {

    private ShippingMethod[] results;
    private int totalCount;

    public ShippingMethodsList() {

    }

    public ShippingMethodsList(ShippingMethod[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public ShippingMethod[] getResults() {
        return results;
    }

    public void setResults(ShippingMethod[] results) {
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
