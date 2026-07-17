package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;

/**
 * Created by Anvar Akramov on 11/16/17.
 */
public class CustomerLookupRequestData extends RequestListSearchData {

    private String account_type; //eg. CUSTOMER or SUPPLIER

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
