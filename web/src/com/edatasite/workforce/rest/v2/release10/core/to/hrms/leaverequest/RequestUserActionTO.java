package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 20/11/2017.
 */
public class RequestUserActionTO extends ResponseData {
    private boolean reject;
    private boolean approve;
    private boolean approve_for_all;

    public RequestUserActionTO() {
    }

    public RequestUserActionTO(boolean reject, boolean approve, boolean approve_for_all) {
        this.reject = reject;
        this.approve = approve;
        this.approve_for_all = approve_for_all;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public boolean isApprove_for_all() {
        return approve_for_all;
    }

    public void setApprove_for_all(boolean approve_for_all) {
        this.approve_for_all = approve_for_all;
    }
}
