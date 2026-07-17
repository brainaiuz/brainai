package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 01/10/2018.
 */
public class ApproverStatusTO extends ResponseData {
    private String type;

    public ApproverStatusTO() {
    }

    public ApproverStatusTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
