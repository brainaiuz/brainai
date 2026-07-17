package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.DetailResult;

public class CheckPerformTransactionResult {
    public boolean allow;
    private DetailResult detail;

    public CheckPerformTransactionResult(boolean allow, DetailResult detail) {
        this.allow = allow;
        this.detail = detail;
    }

    public CheckPerformTransactionResult() {

    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public void setDetail(DetailResult detail) {
        this.detail = detail;
    }

    public boolean isAllow() {
        return allow;
    }

    public DetailResult getDetail() {
        return detail;
    }
}
