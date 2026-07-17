package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class GetApproversTO extends ResponseData {

    private String request_type;
    private Integer approver_index;

    public GetApproversTO() {
    }

    public GetApproversTO(String request_type, Integer approver_index) {
        this.request_type = request_type;
        this.approver_index = approver_index;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Integer getApprover_index() {
        return approver_index;
    }

    public void setApprover_index(Integer approver_index) {
        this.approver_index = approver_index;
    }
}
