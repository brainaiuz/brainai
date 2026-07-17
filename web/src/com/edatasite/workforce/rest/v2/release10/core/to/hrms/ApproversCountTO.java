package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class ApproversCountTO extends ResponseData {
    private Integer approvers_count;

    public ApproversCountTO() {
    }

    public ApproversCountTO(Integer approvers_count) {
        this.approvers_count = approvers_count;
    }

    public Integer getApprovers_count() {
        return approvers_count;
    }

    public void setApprovers_count(Integer approvers_count) {
        this.approvers_count = approvers_count;
    }
}
