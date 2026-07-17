package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class ApproverTO {
    private Integer appproveStatusId;
    private Integer rejectStatusId;
    private Integer approverOrder;
    private Integer clonedFrom;
    private SelectItem exactEmployee;

    public Integer getAppproveStatusId() {
        return appproveStatusId;
    }

    public void setAppproveStatusId(Integer appproveStatusId) {
        this.appproveStatusId = appproveStatusId;
    }

    public Integer getRejectStatusId() {
        return rejectStatusId;
    }

    public void setRejectStatusId(Integer rejectStatusId) {
        this.rejectStatusId = rejectStatusId;
    }

    public Integer getApproverOrder() {
        return approverOrder;
    }

    public void setApproverOrder(Integer approverOrder) {
        this.approverOrder = approverOrder;
    }

    public Integer getClonedFrom() {
        return clonedFrom;
    }

    public void setClonedFrom(Integer clonedFrom) {
        this.clonedFrom = clonedFrom;
    }

    public SelectItem getExactEmployee() {
        return exactEmployee;
    }

    public void setExactEmployee(SelectItem exactEmployee) {
        this.exactEmployee = exactEmployee;
    }
}
