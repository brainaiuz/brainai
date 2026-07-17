package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@SolrDocument(collection = "eventCore")
public class EventSolrDoc extends RelationBaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("eventId")
    @Indexed(name = "eventId", type = "pint", required = true)
    private Integer eventId;

    @Field("subject")
    private String subject;

    @Field("callType")
    private String callType;

    @Field("inbound")
    private Boolean inbound;

    @Field("missed")
    private Boolean missed;

    @Field("ownerId")
    private Integer ownerId;

    @Field("ownerIdName")
    @Indexed(name = "ownerIdName", type = "string", stored = false)
    private String ownerIdName;

    @Field("ownerName")
    private String ownerName;

    @Field("updaterId")
    private Integer updaterId;

    @Field("updaterIdName")
    @Indexed(name = "updaterIdName", type = "string", stored = false)
    private String updaterIdName;

    @Field("updaterName")
    private String updaterName;

    @Field("googleId")
    private String googleId;

    @Field("sharedUserId")
    @Indexed(name = "sharedUserId", type = "pints", stored = false)
    private List<Integer> sharedUserId = new ArrayList<>();

    @Field("contactId")
    @Indexed(name = "contactId",type = "pints",stored = false)
    private List<Integer> contactId = new ArrayList<>();

    @Field("sharedUserIdName")
    @Indexed(name = "sharedUserIdName", type = "strings", stored = false)
    private List<String> sharedUserIdName = new ArrayList<>();

    @Field("sharedUserName")
    @Indexed(name = "sharedUserName", type = "strings")
    private List<String> sharedUserName = new ArrayList<>();

    @Field("creationDate")
    private Date creationDate;

    @Field("startDate")
    private Date startDate;

    @Field("endDate")
    private Date endDate;

    @Field("lastUpdateDate")
    private Date lastUpdateDate;

    @Field("recurrenceId")
    private Integer recurrenceId;

    @Field("allDay")
    private Boolean allDay;

    @Field("multiDay")
    private Boolean multiDay;

    @Field("callLog")
    private Boolean callLog;

    @Field("fromRecorder")
    private Boolean fromRecorder;

    @Field("duration")
    private Long duration;

    @Field("activityTypeId")
    private Integer activityTypeId;

    @Field("activityTypeIdName")
    private String activityTypeIdName;

    @Field("booking")
    private Boolean booking;

    @Field("description")
    private String description;

    @Field("edsLocationId")
    private Integer edsLocationId;

    @Field("location")
    private String location;

    @Field("createdFromId")
    private Integer createdFromId;

    @Field("contactRelatedId")
    private Integer contactRelatedId;

    @Field("contactRelatedIdName")
    @Indexed(name = "contactRelatedIdName", type = "string", stored = false)
    private String contactRelatedIdName;

    @Field("contactRelatedName")
    private String contactRelatedName;

    @Field("leadRelatedId")
    private Integer leadRelatedId;

    @Field("leadRelatedIdName")
    @Indexed(name = "leadRelatedIdName", type = "string", stored = false)
    private String leadRelatedIdName;

    @Field("leadRelatedName")
    private String leadRelatedName;

    @Field("crmAccountRelatedId")
    private Integer crmAccountRelatedId;

    @Field("crmAccountRelatedIdName")
    @Indexed(name = "crmAccountRelatedIdName", type = "string", stored = false)
    private String crmAccountRelatedIdName;

    @Field("crmAccountRelatedName")
    private String crmAccountRelatedName;

    @Field("candidateRelatedId")
    private Integer candidateRelatedId;

    @Field("candidateRelatedIdName")
    @Indexed(name = "candidateRelatedIdName", type = "string", stored = false)
    private String candidateRelatedIdName;

    @Field("candidateRelatedName")
    private String candidateRelatedName;

    @Field("employeeRelatedId")
    private Integer employeeRelatedId;

    @Field("employeeRelatedIdName")
    @Indexed(name = "employeeRelatedIdName", type = "string", stored = false)
    private String employeeRelatedIdName;

    @Field("employeeRelatedName")
    private String employeeRelatedName;

    @Field("taskRelatedId")
    private Integer taskRelatedId;

    @Field("taskRelatedName")
    private String taskRelatedName;

    @Field("taskRelatedIdName")
    @Indexed(name = "taskRelatedIdName", type = "string", stored = false)
    private String taskRelatedIdName;

    @Field("projectRelatedId")
    private Integer projectRelatedId;

    @Field("projectRelatedName")
    private String projectRelatedName;

    @Field("projectRelatedIdName")
    @Indexed(name = "projectRelatedIdName", type = "string", stored = false)
    private String projectRelatedIdName;

    @Field("asteriskId")
    private String asteriskId;

    @Field("twilioId")
    private String twilioId;

    @Field("phoneNumber")
    private String phoneNumber;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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
        return inbound != null && inbound;
    }

    public void setInbound(Boolean inbound) {
        this.inbound = inbound;
    }

    public Boolean getMissed() {
        return missed != null && missed;
    }

    public void setMissed(Boolean missed) {
        this.missed = missed;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerIdName() {
        return ownerIdName;
    }

    public void setOwnerIdName(String ownerIdName) {
        this.ownerIdName = ownerIdName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterIdName() {
        return updaterIdName;
    }

    public void setUpdaterIdName(String updaterIdName) {
        this.updaterIdName = updaterIdName;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public List<Integer> getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(List<Integer> sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public List<String> getSharedUserIdName() {
        return sharedUserIdName;
    }

    public void setSharedUserIdName(List<String> sharedUserIdName) {
        this.sharedUserIdName = sharedUserIdName;
    }

    public List<String> getSharedUserName() {
        return sharedUserName;
    }

    public void setSharedUserName(List<String> sharedUserName) {
        this.sharedUserName = sharedUserName;
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
        return allDay != null && allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getMultiDay() {
        return multiDay != null && multiDay;
    }

    public void setMultiDay(Boolean multiDay) {
        this.multiDay = multiDay;
    }

    public Boolean getCallLog() {
        return callLog != null && callLog;
    }

    public void setCallLog(Boolean callLog) {
        this.callLog = callLog;
    }

    public Boolean getFromRecorder() {
        return fromRecorder != null && fromRecorder;
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

    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getActivityTypeIdName() {
        return activityTypeIdName;
    }

    public void setActivityTypeIdName(String activityTypeIdName) {
        this.activityTypeIdName = activityTypeIdName;
    }

    public Boolean getBooking() {
        return booking != null && booking;
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

    public Integer getEdsLocationId() {
        return edsLocationId;
    }

    public void setEdsLocationId(Integer edsLocationId) {
        this.edsLocationId = edsLocationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCreatedFromId() {
        return createdFromId;
    }

    public void setCreatedFromId(Integer createdFromId) {
        this.createdFromId = createdFromId;
    }

    public Integer getContactRelatedId() {
        return contactRelatedId;
    }

    public void setContactRelatedId(Integer contactRelatedId) {
        this.contactRelatedId = contactRelatedId;
    }

    public String getContactRelatedIdName() {
        return contactRelatedIdName;
    }

    public void setContactRelatedIdName(String contactRelatedIdName) {
        this.contactRelatedIdName = contactRelatedIdName;
    }

    public String getContactRelatedName() {
        return contactRelatedName;
    }

    public void setContactRelatedName(String contactRelatedName) {
        this.contactRelatedName = contactRelatedName;
    }

    public Integer getLeadRelatedId() {
        return leadRelatedId;
    }

    public void setLeadRelatedId(Integer leadRelatedId) {
        this.leadRelatedId = leadRelatedId;
    }

    public String getLeadRelatedIdName() {
        return leadRelatedIdName;
    }

    public void setLeadRelatedIdName(String leadRelatedIdName) {
        this.leadRelatedIdName = leadRelatedIdName;
    }

    public String getLeadRelatedName() {
        return leadRelatedName;
    }

    public void setLeadRelatedName(String leadRelatedName) {
        this.leadRelatedName = leadRelatedName;
    }

    public Integer getCrmAccountRelatedId() {
        return crmAccountRelatedId;
    }

    public void setCrmAccountRelatedId(Integer crmAccountRelatedId) {
        this.crmAccountRelatedId = crmAccountRelatedId;
    }

    public String getCrmAccountRelatedIdName() {
        return crmAccountRelatedIdName;
    }

    public void setCrmAccountRelatedIdName(String crmAccountRelatedIdName) {
        this.crmAccountRelatedIdName = crmAccountRelatedIdName;
    }

    public String getCrmAccountRelatedName() {
        return crmAccountRelatedName;
    }

    public void setCrmAccountRelatedName(String crmAccountRelatedName) {
        this.crmAccountRelatedName = crmAccountRelatedName;
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

    public Integer getCandidateRelatedId() {
        return candidateRelatedId;
    }

    public void setCandidateRelatedId(Integer candidateRelatedId) {
        this.candidateRelatedId = candidateRelatedId;
    }

    public String getCandidateRelatedIdName() {
        return candidateRelatedIdName;
    }

    public void setCandidateRelatedIdName(String candidateRelatedIdName) {
        this.candidateRelatedIdName = candidateRelatedIdName;
    }

    public String getCandidateRelatedName() {
        return candidateRelatedName;
    }

    public void setCandidateRelatedName(String candidateRelatedName) {
        this.candidateRelatedName = candidateRelatedName;
    }

    public Integer getEmployeeRelatedId() {
        return employeeRelatedId;
    }

    public void setEmployeeRelatedId(Integer employeeRelatedId) {
        this.employeeRelatedId = employeeRelatedId;
    }

    public String getEmployeeRelatedIdName() {
        return employeeRelatedIdName;
    }

    public void setEmployeeRelatedIdName(String employeeRelatedIdName) {
        this.employeeRelatedIdName = employeeRelatedIdName;
    }

    public String getEmployeeRelatedName() {
        return employeeRelatedName;
    }

    public void setEmployeeRelatedName(String employeeRelatedName) {
        this.employeeRelatedName = employeeRelatedName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getTaskRelatedId() {
        return taskRelatedId;
    }

    public void setTaskRelatedId(Integer taskRelatedId) {
        this.taskRelatedId = taskRelatedId;
    }

    public String getTaskRelatedName() {
        return taskRelatedName;
    }

    public void setTaskRelatedName(String taskRelatedName) {
        this.taskRelatedName = taskRelatedName;
    }

    public String getTaskRelatedIdName() {
        return taskRelatedIdName;
    }

    public void setTaskRelatedIdName(String taskRelatedIdName) {
        this.taskRelatedIdName = taskRelatedIdName;
    }

    public Integer getProjectRelatedId() {
        return projectRelatedId;
    }

    public void setProjectRelatedId(Integer projectRelatedId) {
        this.projectRelatedId = projectRelatedId;
    }

    public String getProjectRelatedName() {
        return projectRelatedName;
    }

    public void setProjectRelatedName(String projectRelatedName) {
        this.projectRelatedName = projectRelatedName;
    }

    public String getProjectRelatedIdName() {
        return projectRelatedIdName;
    }

    public void setProjectRelatedIdName(String projectRelatedIdName) {
        this.projectRelatedIdName = projectRelatedIdName;
    }

    public List<Integer> getContactId() {
        return contactId;
    }

    public void setContactId(List<Integer> contactId) {
        this.contactId = contactId;
    }
}
