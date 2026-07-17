package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class CogsAccountTO extends ResponseData {
    private Integer cogs_account_id;
    private String cogs_account_name;

    public CogsAccountTO(Integer cogs_account_id, String cogs_account_name) {
        this.cogs_account_id = cogs_account_id;
        this.cogs_account_name = cogs_account_name;
    }

    public CogsAccountTO() {
    }

    public Integer getCogs_account_id() {
        return cogs_account_id;
    }

    public void setCogs_account_id(Integer cogs_account_id) {
        this.cogs_account_id = cogs_account_id;
    }

    public String getCogs_account_name() {
        return cogs_account_name;
    }

    public void setCogs_account_name(String cogs_account_name) {
        this.cogs_account_name = cogs_account_name;
    }
}
