package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventSolrItem implements IsSerializable {

    private Integer objectId;
    private String subject;
    private String callType;
    private Boolean inbound;
    private Boolean missed;
    private SelectItem owner;
    private SelectItem updater;
    private String googleId;
    private List<SelectItem> sharedUsers = new ArrayList<>();
    private List<Integer> contactIds = new ArrayList<>();
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private Date lastUpdateDate;
    private Integer recurrenceId;
    private Boolean allDay;
    private Boolean multiDay;
    private Boolean callLog;
    private Boolean fromRecorder;
    private Long duration;
    private SelectItem activityType;
    private Boolean booking;
    private String description;
    private Integer locationId;
    private Integer createdFromId;
    private SelectItem contactRelated;
    private SelectItem leadRelated;
    private SelectItem crmAccountRelated;
    private SelectItem candidateRelated;
    private SelectItem employeeRelated;
    private SelectItem taskRelated;
    private SelectItem projectRelated;
    private String asteriskId;
    private String twilioId;
    private String phoneNumber;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Boolean getInbound() {
        return inbound;
    }

    public void setInbound(Boolean inbound) {
        this.inbound = inbound;
    }

    public Boolean getMissed() {
        return missed;
    }

    public void setMissed(Boolean missed) {
        this.missed = missed;
    }

    public SelectItem getOwner() {
        return owner;
    }

    public void setOwner(SelectItem owner) {
        this.owner = owner;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public List<SelectItem> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(List<SelectItem> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public List<Integer> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<Integer> contactIds) {
        this.contactIds = contactIds;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getMultiDay() {
        return multiDay;
    }

    public void setMultiDay(Boolean multiDay) {
        this.multiDay = multiDay;
    }

    public Boolean getCallLog() {
        return callLog;
    }

    public void setCallLog(Boolean callLog) {
        this.callLog = callLog;
    }

    public Boolean getFromRecorder() {
        return fromRecorder;
    }

    public void setFromRecorder(Boolean fromRecorder) {
        this.fromRecorder = fromRecorder;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public SelectItem getActivityType() {
        return activityType;
    }

    public void setActivityType(SelectItem activityType) {
        this.activityType = activityType;
    }

    public Boolean getBooking() {
        return booking;
    }

    public void setBooking(Boolean booking) {
        this.booking = booking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getCreatedFromId() {
        return createdFromId;
    }

    public void setCreatedFromId(Integer createdFromId) {
        this.createdFromId = createdFromId;
    }

    public SelectItem getContactRelated() {
        return contactRelated;
    }

    public void setContactRelated(SelectItem contactRelated) {
        this.contactRelated = contactRelated;
    }

    public SelectItem getLeadRelated() {
        return leadRelated;
    }

    public void setLeadRelated(SelectItem leadRelated) {
        this.leadRelated = leadRelated;
    }

    public SelectItem getCrmAccountRelated() {
        return crmAccountRelated;
    }

    public void setCrmAccountRelated(SelectItem crmAccountRelated) {
        this.crmAccountRelated = crmAccountRelated;
    }

    public SelectItem getCandidateRelated() {
        return candidateRelated;
    }

    public void setCandidateRelated(SelectItem candidateRelated) {
        this.candidateRelated = candidateRelated;
    }

    public SelectItem getEmployeeRelated() {
        return employeeRelated;
    }

    public void setEmployeeRelated(SelectItem employeeRelated) {
        this.employeeRelated = employeeRelated;
    }

    public SelectItem getTaskRelated() {
        return taskRelated;
    }

    public void setTaskRelated(SelectItem taskRelated) {
        this.taskRelated = taskRelated;
    }

    public SelectItem getProjectRelated() {
        return projectRelated;
    }

    public void setProjectRelated(SelectItem projectRelated) {
        this.projectRelated = projectRelated;
    }

    public String getAsteriskId() {
        return asteriskId;
    }

    public void setAsteriskId(String asteriskId) {
        this.asteriskId = asteriskId;
    }

    public String getTwilioId() {
        return twilioId;
    }

    public void setTwilioId(String twilioId) {
        this.twilioId = twilioId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
