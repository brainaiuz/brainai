package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class IncomeAccountTO extends ResponseData {
    private Integer income_account_id;
    private String income_account_name;

    public IncomeAccountTO() {
    }

    public IncomeAccountTO(Integer income_account_id, String income_account_name) {
        this.income_account_id = income_account_id;
        this.income_account_name = income_account_name;
    }

    public Integer getIncome_account_id() {
        return income_account_id;
    }

    public void setIncome_account_id(Integer income_account_id) {
        this.income_account_id = income_account_id;
    }

    public String getIncome_account_name() {
        return income_account_name;
    }

    public void setIncome_account_name(String income_account_name) {
        this.income_account_name = income_account_name;
    }
}
