package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class ChangeOpportunityStatusRequestTO extends ResponseData {
    private Integer status_id;
    private Integer item_id;
    private Integer reason_id; //in case if lost we must set reason (new logic added)
    private String note;

    public ChangeOpportunityStatusRequestTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getReason_id() {
        return reason_id;
    }

    public void setReason_id(Integer reason_id) {
        this.reason_id = reason_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
