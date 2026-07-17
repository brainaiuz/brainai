package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.DateRangeTO;

/**
 * Created by Farrukh Abdurakhmonov on 19/01/2018.
 */
public class ApprovalLeaveRequestTO extends ResponseData {
    private Integer id;
    private String title;
    private String requester;
    private String description;
    private DateRangeTO date_range;
    private Integer days_paid;
    private Integer days_not_paid;
    private ApproverListStatusTO status;

    public ApprovalLeaveRequestTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateRangeTO getDate_range() {
        return date_range;
    }

    public void setDate_range(DateRangeTO date_range) {
        this.date_range = date_range;
    }

    public Integer getDays_paid() {
        return days_paid;
    }

    public void setDays_paid(Integer days_paid) {
        this.days_paid = days_paid;
    }

    public Integer getDays_not_paid() {
        return days_not_paid;
    }

    public void setDays_not_paid(Integer days_not_paid) {
        this.days_not_paid = days_not_paid;
    }

    public ApproverListStatusTO getStatus() {
        return status;
    }

    public void setStatus(ApproverListStatusTO status) {
        this.status = status;
    }
}
