package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 19/01/2018.
 */
public class ApprovalCashAdvanceTO extends ResponseData {
    private Integer id;
    private Object status;
    private String requester;
    private String approver;
    private CurrencyValueTO requested_amount;

    public ApprovalCashAdvanceTO() {
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

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public CurrencyValueTO getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(CurrencyValueTO requested_amount) {
        this.requested_amount = requested_amount;
    }
}
