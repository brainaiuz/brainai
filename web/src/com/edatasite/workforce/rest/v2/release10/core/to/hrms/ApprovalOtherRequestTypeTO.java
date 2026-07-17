package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 16/01/2018.
 */
public class ApprovalOtherRequestTypeTO extends ResponseData {
    private String type;

    public ApprovalOtherRequestTypeTO() {
    }

    public ApprovalOtherRequestTypeTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
