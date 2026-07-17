package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class CashAdvanceAddTO extends ResponseData {
    private String create_type;
    private CashAdvanceAddRequestTO request;

    public CashAdvanceAddTO() {
    }

    public String getCreate_type() {
        return create_type;
    }

    public void setCreate_type(String create_type) {
        this.create_type = create_type;
    }

    public CashAdvanceAddRequestTO getRequest() {
        return request;
    }

    public void setRequest(CashAdvanceAddRequestTO request) {
        this.request = request;
    }
}
