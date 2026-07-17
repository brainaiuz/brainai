package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/3/2017.
 */
public class AccountTO extends ResponseData {
    private Integer account_id;
    private String account_code;
    private String account_name;

    public AccountTO() {
    }

    public AccountTO(Integer account_id, String account_name) {
        this.account_id = account_id;
        this.account_name = account_name;
    }

    public AccountTO(Integer account_id, String account_code, String account_name) {
        this.account_id = account_id;
        this.account_code = account_code;
        this.account_name = account_name;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getAccount_code() {
        return account_code;
    }

    public void setAccount_code(String account_code) {
        this.account_code = account_code;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
