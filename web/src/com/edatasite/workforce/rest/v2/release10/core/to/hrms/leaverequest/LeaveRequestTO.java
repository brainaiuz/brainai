package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class LeaveRequestTO extends ResponseData {
    private Integer id;
    private String title;
    private String description;
    private DateRangeTO date_range;
    private Double days_paid;
    private Double days_not_paid;
    private ApproverListStatusTO status;
    private UserTO user;
    private ArrayList<ApproverItemMini> approversList;

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

    public Double getDays_paid() {
        return days_paid;
    }

    public void setDays_paid(Double days_paid) {
        this.days_paid = days_paid;
    }

    public Double getDays_not_paid() {
        return days_not_paid;
    }

    public void setDays_not_paid(Double days_not_paid) {
        this.days_not_paid = days_not_paid;
    }

    public ApproverListStatusTO getStatus() {
        return status;
    }

    public void setStatus(ApproverListStatusTO status) {
        this.status = status;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public ArrayList<ApproverItemMini> getApproversList() {
        return approversList;
    }

    public void setApproversList(ArrayList<ApproverItemMini> approversList) {
        this.approversList = approversList;
    }
}
