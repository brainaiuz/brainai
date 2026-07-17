package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 27/02/2018.
 */

public class StatusHistoryTO extends ResponseData {

    private FilteredStatusItemTO status;
    private String applied_action;
    private ContactTO modifier;
    private String date;
    //This field is used for sorting by date
    private Date updateDate;

    public StatusHistoryTO() {
    }

    public FilteredStatusItemTO getStatus() {
        return status;
    }

    public void setStatus(FilteredStatusItemTO status) {
        this.status = status;
    }

    public String getApplied_action() {
        return applied_action;
    }

    public void setApplied_action(String applied_action) {
        this.applied_action = applied_action;
    }

    public ContactTO getModifier() {
        return modifier;
    }

    public void setModifier(ContactTO modifier) {
        this.modifier = modifier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
