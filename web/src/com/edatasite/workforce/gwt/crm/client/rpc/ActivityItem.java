/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:29:44                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 10:56:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActivityItem implements IsSerializable, Key {
    public static final String ACTIVITY_TYPE = "Activity Type";
    public static final String SUBJECT = "SUBJECT";
    public static final String START_DATE = "Start Date";
    public static final String CREATION_DATE = "Creation Date";
    public static final String DUE_DATE = "Due Date";
    public static final String STATUS = "Status";
    public static final String PRIORITY = "Priority";
    public static final String ESTIMATE = "Estimate";
    public static final String ACTUAL = "Actual";
    public static final String TASKS = "Tasks";
    public static final String EVENTS = "Events";
    public static final String EMAILS = "Emails";

    private Integer entityId;
    private Integer massMailObjectId;
    private Integer taskObjectId;
    private Integer eventObjectId;
    private String emailObjectId;
    private String activityType;
    private String subject;
    private Date startDate;
    private Date dueDate;
    private DateNonConvertable dueDate2;
    private String status;
    private Integer statusID;
    private String priority;
    private String priorityCode;
    private Integer priorityID;
    private String leadName;
    private String accountName;
    private String contactName;
    private String opportunityName;
    private Date creationDate;
    private String description;
    private String assignee;
    private Float percent;

    private String sStartDate;
    private String sDueDate;

    private Integer estimateTime;
    private long actualTime;
    private boolean timerStarted;
    private boolean callLog = false;
    private boolean isSms = false;
    private boolean interview = false;
    private Integer timerOwnerId;

    //for Case Emails only
    private String to;
    private String replyTo;
    private String cc;
    private String bcc;
    private String from;
    private Integer fromUserID;
    private String content;
    private Integer timeSpent;
    private Integer salesID;
    private String invitationResponse;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getSStartDate() {
        return sStartDate;
    }

    public void setSStartDate(String sStartDate) {
        this.sStartDate = sStartDate;
    }

    public String getSDueDate() {
        return sDueDate;
    }

    public void setSDueDate(String sDueDate) {
        this.sDueDate = sDueDate;
    }

    public Integer getTaskObjectId() {
        return taskObjectId;
    }

    public void setTaskObjectId(Integer taskObjectId) {
        this.taskObjectId = taskObjectId;
    }

    public Integer getEventObjectId() {
        return eventObjectId;
    }

    public void setEventObjectId(Integer eventObjectId) {
        this.eventObjectId = eventObjectId;
    }

    public String getEmailObjectId() {
        return emailObjectId;
    }

    public void setEmailObjectId(String emailObjectId) {
        this.emailObjectId = emailObjectId;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public DateNonConvertable getDueDate2() {
        return dueDate2;
    }

    public void setDueDate2(DateNonConvertable dueDate2) {
        this.dueDate2 = dueDate2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee == null || assignee.equals("") ? "" : assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public long getActualTime() {
        return actualTime;
    }

    public void setActualTime(long actualTime) {
        this.actualTime = actualTime;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(boolean timerStarted) {
        this.timerStarted = timerStarted;
    }

    public boolean isInterview() {
        return interview;
    }

    public void setInterview(boolean interview) {
        this.interview = interview;
    }

    public boolean isCallLog() {
        return callLog;
    }

    public void setCallLog(boolean callLog) {
        this.callLog = callLog;
    }

    public boolean isSms() {
        return isSms;
    }

    public void setSms(boolean sms) {
        isSms = sms;
    }

    public Integer getTimerOwnerId() {
        return timerOwnerId;
    }

    public void setTimerOwnerId(Integer timerOwnerId) {
        this.timerOwnerId = timerOwnerId;
    }

    @Override
    public String getKey() {
        return getObjectId() + "_" + getActivityType();
    }

    private Integer getObjectId() {
        if (activityType != null) {
            if (activityType.equals(CrmConstants.CRM_EVENT)) {
                return getEventObjectId();
            }

            if (activityType.equals(CrmConstants.TASK)) {
                return getTaskObjectId();
            }

            /*if (activityType.equals(CrmConstants.EMAIL)) {
                return getEmailObjectId();
            }*/
        }
        return null;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getMassMailObjectId() {
        return massMailObjectId;
    }

    public void setMassMailObjectId(Integer massMailObjectId) {
        this.massMailObjectId = massMailObjectId;
    }

    public void setSalesID(Integer salesID) {
        this.salesID = salesID;
    }

    public Integer getSalesID() {
        return salesID;
    }

    public String getInvitationResponse() {
        return invitationResponse;
    }

    public void setInvitationResponse(String invitationResponse) {
        this.invitationResponse = invitationResponse;
    }
}
