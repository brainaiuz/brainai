package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/1/11
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxListData implements IsSerializable {

    private ListResult<TaxListItem> taxList;
    private boolean isVatReturnEnabled;

    public TaxListData() {

    }

    public ListResult<TaxListItem> getTaxList() {
        return taxList;
    }

    public void setTaxList(ListResult<TaxListItem> taxList) {
        this.taxList = taxList;
    }

    public boolean isVatReturnEnabled() {
        return isVatReturnEnabled;
    }

    public void setVatReturnEnabled(boolean vatReturnEnabled) {
        isVatReturnEnabled = vatReturnEnabled;
    }
}
