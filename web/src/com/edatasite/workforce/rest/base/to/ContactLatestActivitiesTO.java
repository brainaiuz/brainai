package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: abror
 * Date: 7/8/15 3:57 PM
 */
public class ContactLatestActivitiesTO implements IsSerializable {
    Integer id;
    String activityType;
    String subject;
    Long startDate;
    Long endDate;
    SelectItemTO status;
    SelectItemTO priority;

    public ContactLatestActivitiesTO() {
    }

    public ContactLatestActivitiesTO(ActivityItem item) {
        if (item != null) {
            this.id = item.getEventObjectId();
            this.activityType = item.getActivityType();
            this.subject = item.getSubject();
            this.startDate = WrapUtils.dateToLong(item.getStartDate());
            this.endDate = WrapUtils.dateToLong(item.getDueDate());
            this.status = new SelectItemTO(item.getStatus());
            this.priority = new SelectItemTO(item.getPriority());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public SelectItemTO getPriority() {
        return priority;
    }

    public void setPriority(SelectItemTO priority) {
        this.priority = priority;
    }
}
