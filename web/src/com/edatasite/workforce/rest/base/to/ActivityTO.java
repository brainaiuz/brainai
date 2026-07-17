package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.rest.base.enums.CallTypeEnum;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov on 3/30/15.
 */
public class ActivityTO implements IsSerializable {

    Integer id;
    String subject;
    Long startDate;
    Long endDate;
    Long callDate;
    String description;
    String activityType;
    CallTypeEnum callType;
    SelectItemTO user;
    SelectItemTO contact;
    SelectItemTO lead;
    SelectItemTO opportunity;
    String location;
    String callNumber;
    Integer duration;


    public ActivityTO() {
    }

    public ActivityTO(EventItem item, boolean briefly) {
        this.id = item.getObjectID();
        this.subject = item.getSubject();
        this.startDate = WrapUtils.dateToLong(item.getStartDate());
        this.endDate = WrapUtils.dateToLong(item.getEndDate());
        if (!briefly) {
            this.description = item.getDescription();
            this.location = item.getLocation();
        }
    }

    public EventItem wrap(ActivityTO activityTO) {
        EventItem item = new EventItem();
        item.setObjectID(activityTO.getId());
        item.setSubject(activityTO.getSubject());
        item.setStartDate(WrapUtils.longToDate(activityTO.getStartDate()));
        item.setEndDate(WrapUtils.longToDate(activityTO.getEndDate()));

        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public CallTypeEnum getCallType() {
        return callType;
    }

    public void setCallType(CallTypeEnum callType) {
        this.callType = callType;
    }

    public SelectItemTO getUser() {
        return user;
    }

    public void setUser(SelectItemTO user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public SelectItemTO getContact() {
        return contact;
    }

    public void setContact(SelectItemTO contact) {
        this.contact = contact;
    }

    public SelectItemTO getLead() {
        return lead;
    }

    public void setLead(SelectItemTO lead) {
        this.lead = lead;
    }

    public SelectItemTO getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(SelectItemTO opportunity) {
        this.opportunity = opportunity;
    }

    public Long getCallDate() {
        return callDate;
    }

    public void setCallDate(Long callDate) {
        this.callDate = callDate;
    }
}
