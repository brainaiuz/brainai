package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class CashAdvanceListItemTO extends ResponseData {

    private Integer id;
    //private CustomStatusTO status;//the rest
    //private ApproverListStatusTO status;//partially approved
    private Object status;//partially approved or the rest
    private String approver;
    private CurrencyValueTO remaining_amount;
    private CurrencyValueTO requested_amount;

    public CashAdvanceListItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public CurrencyValueTO getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(CurrencyValueTO remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public CurrencyValueTO getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(CurrencyValueTO requested_amount) {
        this.requested_amount = requested_amount;
    }
}
