package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class SalesInvoiceRequestListTO extends RequestListData {
    private String search_text;

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }
}
