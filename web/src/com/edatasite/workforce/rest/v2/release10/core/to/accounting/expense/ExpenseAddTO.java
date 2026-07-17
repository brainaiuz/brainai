package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov on 14/12/2017.
 */

public class ExpenseAddTO extends ResponseData {

    private String create_type;
    private ExpenseAddRequestTO request;


    public ExpenseAddTO() {

    }

    public String getCreate_type() {
        return create_type;
    }

    public void setCreate_type(String create_type) {
        this.create_type = create_type;
    }

    public ExpenseAddRequestTO getRequest() {
        return request;
    }

    public void setRequest(ExpenseAddRequestTO request) {
        this.request = request;
    }
}
