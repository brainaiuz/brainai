package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsTicket;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.core.solr.document.EventSolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.EventSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 5:07:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "event")
public class EdsEvent extends EdsTraceable implements ObjectHistory {

    public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsEmployee owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    private EdsEmployee assignee;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "subject", length = 1000)
    private String subject;

    @Column(name = "venue")
    private String venue;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    // Event Guests related fields
    @JoinColumn(name = "sendEmailNotification")
    private Boolean sendEmailNotification = true;

    @JoinColumn(name = "includeAttachments")
    private Boolean includeAttachments = true;

    @JoinColumn(name = "recurringActivity")
    private Boolean recurringActivity = false;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "withNotify")
    private Boolean withNotify;

    /**
     * We have created current variable in order to define which concrete
     * event is created in both in the system and google side. During
     * auto-syncronization we will not check in their title, we will check
     * for their eventIDs that store unique values.
     */

    @Column(name = "googleid")
    private String googleID;
    @Column(name = "asteriskid", unique = true)
    private String asteriskid;

    @Column(name = "officeid")
    @Type(type = "text")
    private String officeID;

    @Column(name = "all_day", columnDefinition = "boolean default true")
    private Boolean allDay = true;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "lastModifiedDate")
    private Date lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastModifiedBy")
    private EdsUser lastModifiedBy;

    // In recurring tasks and events this will specify the recurrence frequency, but should not be used in WFT Recurrence Service.
    @Column(name = "recurrenceId")
    private Integer recurrenceID;

    // In recurring tasks and events this will specify the fire time (created time) of the current instance within the series of recurring tasks or events
    @Column(name = "fireTime")
    private Date fireTime;

    @Column(name = "isBooking")
    private Boolean isBooking;

    @Column(name = "maxAttendants")
    private Integer maxAttendants;

    @Column(name = "activityType", columnDefinition = "int2 default 1")
    private Integer activityType;

    @Column(name = "inboundCall", columnDefinition = "boolean default false")
    private boolean inboundCall = false;

    @Column(name = "outboundCall", columnDefinition = "boolean default false")
    private boolean outboundCall = false;

    @Column(name = "missedCall", columnDefinition = "boolean default false")
    private boolean missedCall = false;

    @Column(name = "callDuration", columnDefinition = "int4 default 0")
    private long callDuration = 0;

    @Column(name = "twilio_call_sid")
    private String twilioCallSID;

    private Boolean isPublic = false;

    private Boolean isPrivate = false;

    @Column(name = "organizationName")
    private String organizationName;

    @Column(name = "organizationDescription")
    private String organizationDescription;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "totalticketcount")
    private Integer totalTicketCount;

    @Column(name = "ispublished")
    private Boolean isPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "logoId")
    private Integer logoId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Where(clause = "deleted = 'false'")
    private List<EdsTicket> tickets = new ArrayList<>();

    private Integer locationID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsCrmCustomFields eventCustomFields;

    @Column(name = "fromRecorder", columnDefinition = "boolean default false")
    private boolean fromRecorder;

    @Column(name = "isworkflowitem", columnDefinition = "boolean default false")
    private boolean isWorkflowItem;

    private Integer workflowID;
    private String workflowStartDate;
    private String workflowStartDateAttributes;
    @Column(name = "workflow_due_date")
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;
    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @Column(name = "createdFrom", columnDefinition = "int2 default 1")
    private Integer createdFrom = Appointment.FROM_CRM;
    private Integer timeZoneOffset;
    //Between start and end date
    private Integer dayCount;

    @Column(name = "activityId")
    private Integer activityId;

    @Column(name = "currentCall", columnDefinition = "boolean default false")
    private boolean currentCall = false;

    @Column(name = "completedCall", columnDefinition = "boolean default false")
    private boolean completedCall = false;

    @Column(name = "scheduleCall", columnDefinition = "boolean default false")
    private boolean scheduleCall = false;

    private String phoneNumber;

    @Column(name = "invitationResponse")
    private String invitationResponse;

    public Integer getActivityType() {
        return activityType == null ? Appointment.EVENT : activityType;
    }

    public void setOwner(EdsEmployee owner) {
        if (!ServerUtils.equalsEdsObject(this.owner, owner)) {
            addChange(CustomFormConstants.CREATOR);
        }
        this.owner = owner;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    @Override
    public String getName() {
        return getSubject();
    }

    public void setSubject(String subject) {
        if (!ServerUtils.equalsString(this.subject, subject)) {
            addChange(CustomFormConstants.SUBJECT);
        }
        this.subject = subject;
    }

    public void setVenue(String venue) {
        if (!ServerUtils.equalsString(this.venue, venue)) {
            addChange(CustomFormConstants.PROJECT.LOCATION);
        }
        this.venue = venue;
    }

    public void setStartDate(Date startDate) {
        if (!ServerUtils.equalsDate(this.startDate, startDate)) {
            addChange(CustomFormConstants.START_DATE);
        }
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        if (!ServerUtils.equalsDate(this.endDate, endDate)) {
            addChange(CustomFormConstants.END_DATE);
        }
        this.endDate = endDate;
    }

    public Boolean isSendEmailNotification() {
        return sendEmailNotification != null ? sendEmailNotification : false;
    }

    public Boolean isRecurringActivity() {
        return recurringActivity == null ? Boolean.FALSE : recurringActivity;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addChange(CustomFormConstants.DESCRIPTION);
        }
        this.description = description;
    }

    @Override
    public void setUpdater(EdsUser user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCreator(EdsUser value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Boolean isAllDay() {
        return allDay != null ? allDay : true;
    }

    public boolean isMultiDayAppointment() {
        if (endDate != null && startDate != null) {
            long difference = endDate.getTime() - startDate.getTime();
            return difference > MILLIS_IN_A_DAY;
        }
        return true;
    }

    public Boolean getPublic() {
        return isPublic != null ? isPublic : false;
    }

    public Boolean getIsPrivate() {
        return isPrivate != null ? isPrivate : false;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset != null ? timeZoneOffset : 0;
    }

    public EventItem getRPC(EventItem rpc) {
        if (rpc == null) {
            rpc = new EventItem();
        }
        rpc.setWorkflowItem(isWorkflowItem());
        rpc.setWorkflowStartDate(getWorkflowStartDate());
        rpc.setWorkflowDueDate(getWorkflowDueDate());
        rpc.setObjectID(getObjectID());
        rpc.setDescription(getDescription());
        rpc.setSubject(getSubject());
        rpc.setCreatedFrom(getCreatedFrom());
        rpc.setActivityId(getActivityId());
        rpc.setInboundCall(isInboundCall());
        rpc.setMissedCall(isMissedCall());
        rpc.setOutboundCall(isOutboundCall());
        rpc.setSendEmailNotification(isSendEmailNotification());
        rpc.setRecurring(isRecurringActivity());
        rpc.setLocation(getVenue());
        rpc.setStartDate(getStartDate());
        rpc.setEndDate(getEndDate());
        rpc.setAllDay(isAllDay());
        rpc.setMultiDay(isMultiDayAppointment());
        rpc.setLastModifiedDate(getLastModifiedDate());
        rpc.setAddress1(getAddress1());
        rpc.setAddress2(getAddress2());
        rpc.setCity(getCity());
        rpc.setLogoId(getLogoId());
        rpc.setWorkflowID(getWorkflowID());
        rpc.setWorkflowDueDate(getWorkflowDueDate());
        rpc.setWorkflowDueDateGranularity(getWorkflowDueDateGranularity());
        rpc.setWorkflowStartDate(getWorkflowStartDate());
        rpc.setWorkflowItem(getWorkflowID() != null);
        rpc.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
        rpc.setWorkflowActionStartTime(getWorkflowActionStartTime());
        rpc.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
        rpc.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
        rpc.setCreatedFrom(getCreatedFrom());
        rpc.setTwilioCallSID(getTwilioCallSID());
        rpc.setCurrentCall(isCurrentCall());
        rpc.setComplatedCall(isCompletedCall());
        rpc.setScheduleCall(isScheduleCall());
        rpc.setCallDuration(getCallDuration());
        if (getTotalTicketCount() != null) {
            rpc.setTotalTicketCount(getTotalTicketCount());
        }
        if (getPublished() != null) {
            rpc.setPublished(getPublished());
        }
        if (getCountry() != null) {
            rpc.setCountryId(getCountry().getObjectID());
        }
        rpc.setPostCode(getPostcode());
        if (getLastModifiedBy() != null) {
            rpc.setLastModifiedBy(getLastModifiedBy().getName());
        }
        if (getOwner() != null) {
            rpc.setCreatedBy(getOwner().getFullName());
        }
        rpc.setCreatedDate(getCreationTime());
        rpc.setActivityType(getActivityType());
        if (getOwner() != null) {
            rpc.setOwnerID(getOwner().getObjectID());
            rpc.setOwnerName(getOwner().getFullName());
        }
        return rpc;
    }

    public static EventItem wrapSolrDocumentToRPC(EventSolrDoc doc, ListingFilterParameter filterParameter) {
        EventItem item = new EventItem();
        item.setObjectID(doc.getEventId());
        item.setSubject(doc.getSubject());
        item.setInboundCall(doc.getInbound());
        item.setMissedCall(doc.getMissed());
        item.setDescription(doc.getDescription());
        item.setCreatedDate(doc.getCreationDate());
        item.setLastModifiedDate(doc.getLastUpdateDate());
        item.setStartDate(doc.getStartDate());
        item.setEndDate(doc.getEndDate());
        item.setLocation(doc.getLocation());
        item.setOwnerID(doc.getOwnerId());
        item.setOwnerName(doc.getOwnerName());
        item.setLastModifiedBy(doc.getUpdaterName());
        item.setCallDuration(doc.getDuration());
        item.setCreatedBy(doc.getOwnerName());
        item.setAllDay(doc.getAllDay());
        item.setActivityType(doc.getActivityTypeId());
        item.setCreatedFrom(doc.getCreatedFromId());
        item.setBooking(doc.getBooking());
        item.setGoogleID(doc.getGoogleId());
        item.setAsteriskid(doc.getAsteriskId());
        item.setTwilioCallSID(doc.getTwilioId());
        item.setMultiDay(doc.getMultiDay());
        item.setContactRelation(new RelationItem(null, doc.getContactRelatedId(),
                RelationItem.TYPE_CONTACT, doc.getContactRelatedName(),
                null, RelationItem.TYPE_EVENT, null));
        item.setLeadRelation(new RelationItem(null, doc.getLeadRelatedId(),
                RelationItem.TYPE_LEAD, doc.getLeadRelatedName(),
                null, RelationItem.TYPE_EVENT, null));
        item.setCrmAccountRelation(new RelationItem(null, doc.getCrmAccountRelatedId(),
                RelationItem.TYPE_CRM_ACCOUNT, doc.getCrmAccountRelatedName(),
                null, RelationItem.TYPE_EVENT, null));
        item.setCandidateRelation(new RelationItem(null, doc.getCandidateRelatedId(),
                RelationItem.TYPE_CANDIDATE, doc.getCandidateRelatedName(),
                null, RelationItem.TYPE_EVENT, null));
        item.setEmployeeRelation(new RelationItem(null, doc.getEmployeeRelatedId(),
                RelationItem.TYPE_EMPLOYEE, doc.getEmployeeRelatedName(),
                null, RelationItem.TYPE_EVENT, null));
        item.setRelationValueMap(SolrRelationUtils.getBaseSolrDocValue(doc, EdsRelation.TYPE_EVENT));
        //shared employees
        String sharedEmployees = ServerUtils.asListToString(doc.getSharedUserName());
        if (sharedEmployees != null && !"".equals(sharedEmployees)) {
            item.setSharedEmployeesString(sharedEmployees);
        }
        if (filterParameter.getListPanelTool() != null) {
            item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, filterParameter.getListPanelTool().getColumnCodeName()));
        }

        if (doc.getEdsLocationId() != null) {
            item.setLocationId(doc.getEdsLocationId());
        }
        item.setPhoneNumber(doc.getPhoneNumber());
        return item;
    }

    public SolrInputDocument wrapToSolrDocument(Set<EdsUser> mapOfUsers, List<EdsRelation> edsRelationList) {
        SolrInputDocument doc = wrapToSolrDocument(edsRelationList);
        for (EdsUser user : mapOfUsers) {
            if (user != null) {
                doc.addField(SolrEventRepresenter.FIELD_SHARED_USER_ID, user.getObjectID());
                doc.addField(SolrEventRepresenter.FIELD_SHARED_USER_NAME, user.getFullName());
                doc.addField(SolrEventRepresenter.FIELD_SHARED_USER_ID_NAME, SolrUtils.getIdName(user));
            }
        }
        return doc;
    }

    public SolrInputDocument wrapToSolrDocument(List<EdsRelation> edsRelationList) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrEventRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID());
        doc.addField(SolrEventRepresenter.FIELD_COMPANY_ID, Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        doc.addField(SolrEventRepresenter.FIELD_EVENT_ID, getObjectID());
        doc.addField(SolrEventRepresenter.FIELD_SUBJECT, getSubject());
        doc.addField(SolrEventRepresenter.FIELD_INBOUND, isInboundCall());
        doc.addField(SolrEventRepresenter.FIELD_MISSED, isMissedCall());
        doc.addField(SolrEventRepresenter.FIELD_RECURRENCE_ID, getRecurrenceID());
        doc.addField(SolrEventRepresenter.FIELD_CREATION_DATE, getCreationTime());
        doc.addField(SolrEventRepresenter.FIELD_START_DATE, getStartDate());
        doc.addField(SolrEventRepresenter.FIELD_END_DATE, getEndDate());
        doc.addField(SolrEventRepresenter.FIELD_LAST_UPDATE_DATE, getLastUpdateTime());
        doc.addField(SolrEventRepresenter.FIELD_DESCRIPTION, getDescription());
        doc.addField(SolrEventRepresenter.FIELD_ALL_DAY, isAllDay());
        doc.addField(SolrEventRepresenter.FIELD_FROM_RECORDER, isFromRecorder());
        doc.addField(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID, getActivityType());
        doc.addField(SolrEventRepresenter.FIELD_CREATED_FROM_ID, getCreatedFrom());
        String idName = SolrUtils.getIdName(Appointment.EVENT, Appointment.TYPE_EVENT);
        if (Appointment.CALL_LOG == getActivityType()) {
            idName = SolrUtils.getIdName(Appointment.CALL_LOG, Appointment.TYPE_CALL_LOG);
        } else if (Appointment.INTERVIEW == getActivityType()) {
            idName = SolrUtils.getIdName(Appointment.INTERVIEW, Appointment.TYPE_INTERVIEW);
        } else if (Appointment.SMS == getActivityType()) {
            idName = SolrUtils.getIdName(Appointment.SMS, Appointment.TYPE_SMS);
        }
        doc.addField(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID_NAME, idName);
        doc.addField(SolrEventRepresenter.FIELD_BOOKING, isBooking);
        doc.addField(SolrEventRepresenter.FIELD_GOOGLE_ID, getGoogleID());
        doc.addField(SolrEventRepresenter.FIELD_ASTERISK_ID, getAsteriskid());
        doc.addField(SolrEventRepresenter.FIELD_TWILIO_ID, getTwilioCallSID());
        if (getOwner() != null) {
            doc.addField(SolrEventRepresenter.FIELD_OWNER_ID, getOwner().getObjectID());
            doc.addField(SolrEventRepresenter.FIELD_OWNER_NAME, getOwner().getFullName());
            doc.addField(SolrEventRepresenter.FIELD_OWNER_ID_NAME, SolrUtils.getIdName(getOwner().getObjectID(), getOwner().getFullName()));
        }
        if (getLastModifiedBy() != null) {
            doc.addField(SolrEventRepresenter.FIELD_UPDATER_ID, getLastModifiedBy().getObjectID());
            doc.addField(SolrEventRepresenter.FIELD_UPDATER_NAME, getLastModifiedBy().getFullName());
            doc.addField(SolrEventRepresenter.FIELD_UPDATER_ID_NAME, SolrUtils.getIdName(getLastModifiedBy().getObjectID(), getLastModifiedBy().getFullName()));
        }
        if (Appointment.CALL_LOG == getActivityType()) {
            String callType = "";
            if (isMissedCall()) {
                callType = "MISSED";
            } else if (isInboundCall()) {
                callType = "INBOUND";
            } else if (isOutboundCall()) {
                callType = "OUTBOUND";
            }
            doc.addField(SolrEventRepresenter.FIELD_CALL_TYPE, callType);
        }

        for (EdsRelation contactRelation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(contactRelation.getFromType()) && contactRelation.getToID() != null
                    && contactRelation.getToName() != null && contactRelation.getToType() != null
                    && EdsRelation.TYPE_CONTACT.equalsIgnoreCase(contactRelation.getToType())) {

                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_ID, contactRelation.getToID());
                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_NAME, contactRelation.getToName());
                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_ID_NAME, contactRelation.getToID() + SolrEventRepresenter.SPLIT + contactRelation.getToName());
                break;

            } else if (EdsRelation.TYPE_EVENT.equals(contactRelation.getToType()) && contactRelation.getFromID() != null
                    && contactRelation.getFromName() != null && contactRelation.getFromType() != null
                    && EdsRelation.TYPE_CONTACT.equalsIgnoreCase(contactRelation.getFromType())) {

                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_ID, contactRelation.getFromID());
                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_NAME, contactRelation.getFromName());
                doc.addField(SolrEventRepresenter.FIELD_CONTACT_RELATED_ID_NAME, contactRelation.getFromID() + SolrEventRepresenter.SPLIT + contactRelation.getFromName());
                break;
            }
        }

        for (EdsRelation leadRelation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(leadRelation.getFromType()) && leadRelation.getToID() != null
                    && leadRelation.getToName() != null && leadRelation.getToType() != null
                    && EdsRelation.TYPE_LEAD.equalsIgnoreCase(leadRelation.getToType())) {

                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_ID, leadRelation.getToID());
                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_NAME, leadRelation.getToName());
                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_ID_NAME, leadRelation.getToID() + SolrEventRepresenter.SPLIT + leadRelation.getToName());
                break;

            } else if (EdsRelation.TYPE_EVENT.equals(leadRelation.getToType()) && leadRelation.getFromID() != null
                    && leadRelation.getFromName() != null && leadRelation.getFromType() != null
                    && EdsRelation.TYPE_LEAD.equalsIgnoreCase(leadRelation.getFromType())) {

                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_ID, leadRelation.getFromID());
                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_NAME, leadRelation.getFromName());
                doc.addField(SolrEventRepresenter.FIELD_LEAD_RELATED_ID_NAME, leadRelation.getFromID() + SolrEventRepresenter.SPLIT + leadRelation.getFromName());
                break;
            }
        }

        for (EdsRelation crmAccountRelation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(crmAccountRelation.getFromType()) && crmAccountRelation.getToID() != null
                    && crmAccountRelation.getToName() != null && crmAccountRelation.getToType() != null
                    && EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(crmAccountRelation.getToType())) {

                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_ID, crmAccountRelation.getToID());
                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_NAME, crmAccountRelation.getToName());
                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_ID_NAME, crmAccountRelation.getToID() + SolrEventRepresenter.SPLIT + crmAccountRelation.getToName());
                break;

            } else if (EdsRelation.TYPE_EVENT.equals(crmAccountRelation.getToType()) && crmAccountRelation.getFromID() != null
                    && crmAccountRelation.getFromName() != null && crmAccountRelation.getFromType() != null
                    && EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(crmAccountRelation.getFromType())) {

                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_ID, crmAccountRelation.getFromID());
                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_NAME, crmAccountRelation.getFromName());
                doc.addField(SolrEventRepresenter.FIELD_CRM_ACCOUNT_RELATED_ID_NAME, crmAccountRelation.getFromID() + SolrEventRepresenter.SPLIT + crmAccountRelation.getFromName());
                break;
            }
        }

        for (EdsRelation candidateRelation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(candidateRelation.getFromType()) && candidateRelation.getToID() != null
                    && candidateRelation.getToName() != null && candidateRelation.getToType() != null
                    && EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(candidateRelation.getToType())) {

                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_ID, candidateRelation.getToID());
                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_NAME, candidateRelation.getToName());
                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_ID_NAME, candidateRelation.getToID() + SolrEventRepresenter.SPLIT + candidateRelation.getToName());
                break;

            } else if (EdsRelation.TYPE_EVENT.equals(candidateRelation.getToType()) && candidateRelation.getFromID() != null
                    && candidateRelation.getFromName() != null && candidateRelation.getFromType() != null
                    && EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(candidateRelation.getFromType())) {

                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_ID, candidateRelation.getFromID());
                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_NAME, candidateRelation.getFromName());
                doc.addField(SolrEventRepresenter.FIELD_CANDIDATE_RELATED_ID_NAME, candidateRelation.getFromID() + SolrEventRepresenter.SPLIT + candidateRelation.getFromName());
                break;
            }
        }

        for (EdsRelation employeeRelation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(employeeRelation.getFromType()) && employeeRelation.getToID() != null
                    && employeeRelation.getToName() != null && employeeRelation.getToType() != null
                    && EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(employeeRelation.getToType())) {

                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_ID, employeeRelation.getToID());
                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_NAME, employeeRelation.getToName());
                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_ID_NAME, employeeRelation.getToID() + SolrEventRepresenter.SPLIT + employeeRelation.getToName());
                break;

            } else if (EdsRelation.TYPE_EVENT.equals(employeeRelation.getToType()) && employeeRelation.getFromID() != null
                    && employeeRelation.getFromName() != null && employeeRelation.getFromType() != null
                    && EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(employeeRelation.getFromType())) {

                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_ID, employeeRelation.getFromID());
                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_NAME, employeeRelation.getFromName());
                doc.addField(SolrEventRepresenter.FIELD_EMPLOYEE_RELATED_ID_NAME, employeeRelation.getFromID() + SolrEventRepresenter.SPLIT + employeeRelation.getFromName());
                break;
            }
        }

        doc.addField(SolrEventRepresenter.FIELD_DURATION_SEC, getCallDuration());
        doc.addField(SolrEventRepresenter.FIELD_MULTI_DAY, isMultiDayAppointment());
        doc.addField(SolrEventRepresenter.FIELD_EDS_LOCATION_ID, getLocationID());
        doc.addField(SolrEventRepresenter.FIELD_PHONE_NUMBER, getPhoneNumber());
        SolrRelationUtils.addToSolrRelations(doc, edsRelationList, EdsRelation.TYPE_EVENT);
        CustomFieldsUtils.setInSolrCustomFields(doc, getEventCustomFields());
        return doc;
    }

    public EventSolrItem getSolrRPC() {
        EventSolrItem eventSolrItem = new EventSolrItem();

        eventSolrItem.setObjectId(getObjectID());
        eventSolrItem.setSubject(getSubject());
        if (Appointment.CALL_LOG == getActivityType()) {
            String callType = "";
            if (isMissedCall()) {
                callType = "MISSED";
            } else if (isInboundCall()) {
                callType = "INBOUND";
            } else if (isOutboundCall()) {
                callType = "OUTBOUND";
            }
            eventSolrItem.setCallType(callType);
        }
        eventSolrItem.setInbound(isInboundCall());
        eventSolrItem.setMissed(isMissedCall());
        if (getOwner() != null) {
            eventSolrItem.setOwner(getOwner().getAsSelectItem());
        }
        if (getLastModifiedBy() != null) {
            eventSolrItem.setUpdater(getLastModifiedBy().getAsSelectItem());
        }
        eventSolrItem.setGoogleId(getGoogleID());
        eventSolrItem.setCreationDate(getCreationTime());
        eventSolrItem.setStartDate(getStartDate());
        eventSolrItem.setEndDate(getEndDate());
        eventSolrItem.setLastUpdateDate(getLastUpdateTime());
        eventSolrItem.setRecurrenceId(getRecurrenceID());
        eventSolrItem.setAllDay(isAllDay());
        eventSolrItem.setMultiDay(isMultiDayAppointment());
        eventSolrItem.setFromRecorder(isFromRecorder());
        eventSolrItem.setDuration(getCallDuration());
        SelectItem activityType = new SelectItem(Appointment.EVENT, Appointment.TYPE_EVENT);
        if (Appointment.CALL_LOG == getActivityType()) {
            activityType = new SelectItem(Appointment.CALL_LOG, Appointment.TYPE_CALL_LOG);
        } else if (Appointment.INTERVIEW == getActivityType()) {
            activityType = new SelectItem(Appointment.INTERVIEW, Appointment.TYPE_INTERVIEW);
        } else if (Appointment.SMS == getActivityType()) {
            activityType = new SelectItem(Appointment.SMS, Appointment.TYPE_SMS);
        }
        eventSolrItem.setActivityType(activityType);
        eventSolrItem.setBooking(getBooking());
        eventSolrItem.setDescription(getDescription());
        eventSolrItem.setLocationId(getLocationID());
        eventSolrItem.setCreatedFromId(getCreatedFrom());

        return eventSolrItem;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.SUBJECT)) {
                setSubject((String) value);
            } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
                setDescription((String) value);
            } else if (fieldID.equals(CustomFormConstants.PROJECT.LOCATION)) {
                setVenue((String) value);
            } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
                setStartDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.END_DATE)) {
                setEndDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.CREATOR)) {
                setOwner((EdsEmployee) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getEventCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getEventCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.CREATOR)) {
            return getOwner();
        } else if (fieldID.equals(CustomFormConstants.SUBJECT)) {
            return getSubject();
        } else if (fieldID.equals(CustomFormConstants.PROJECT.LOCATION)) {
            return getVenue();
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription() != null ? getDescription() : "<i>You have no description for this event.</i>";
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return getStartDate();
        } else if (fieldID.equals(CustomFormConstants.END_DATE)) {
            return getEndDate();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationTime();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getLastUpdateTime();
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_ITEM)) {
            return getLastUpdateTime();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getEventCustomFields() != null ? CustomFieldsUtils.getObjectValue(getEventCustomFields(), fieldID) : "";
        } else if (fieldID.equals("call_type")) {
            return isOutboundCall() ? "outgoing" : isInboundCall() ? "incoming" : "missed";
        } else if (fieldID.equals("call_details")) {
            return isCurrentCall() ? "Current" : isScheduleCall() ? "Scheduled" : "Completed";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getOwner() {
        return owner;
    }

    public EdsEmployee getAssignee() {
        return assignee;
    }

    public void setAssignee(EdsEmployee assignee) {
        this.assignee = assignee;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getSubject() {
        return subject;
    }

    public String getVenue() {
        return venue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean getSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(Boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public Boolean getIncludeAttachments() {
        return includeAttachments;
    }

    public void setIncludeAttachments(Boolean includeAttachments) {
        this.includeAttachments = includeAttachments;
    }

    public Boolean getRecurringActivity() {
        return recurringActivity;
    }

    public void setRecurringActivity(Boolean recurringActivity) {
        this.recurringActivity = recurringActivity;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getWithNotify() {
        return withNotify;
    }

    public void setWithNotify(Boolean withNotify) {
        this.withNotify = withNotify;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getAsteriskid() {
        return asteriskid;
    }

    public void setAsteriskid(String asteriskid) {
        this.asteriskid = asteriskid;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public EdsUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EdsUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public Boolean getBooking() {
        return isBooking;
    }

    public void setBooking(Boolean booking) {
        isBooking = booking;
    }

    public Integer getMaxAttendants() {
        return maxAttendants;
    }

    public void setMaxAttendants(Integer maxAttendants) {
        this.maxAttendants = maxAttendants;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public boolean isInboundCall() {
        return inboundCall;
    }

    public void setInboundCall(boolean inboundCall) {
        this.inboundCall = inboundCall;
    }

    public boolean isOutboundCall() {
        return outboundCall;
    }

    public void setOutboundCall(boolean outboundCall) {
        this.outboundCall = outboundCall;
    }

    public boolean isMissedCall() {
        return missedCall;
    }

    public void setMissedCall(boolean missedCall) {
        this.missedCall = missedCall;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getTwilioCallSID() {
        return twilioCallSID;
    }

    public void setTwilioCallSID(String twilioCallSID) {
        this.twilioCallSID = twilioCallSID;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(Integer totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Integer getLogoId() {
        return logoId;
    }

    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    public List<EdsTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<EdsTicket> tickets) {
        this.tickets = tickets;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public EdsCrmCustomFields getEventCustomFields() {
        return eventCustomFields;
    }

    public void setEventCustomFields(EdsCrmCustomFields eventCustomFields) {
        this.eventCustomFields = eventCustomFields;
    }

    public boolean isFromRecorder() {
        return fromRecorder;
    }

    public void setFromRecorder(boolean fromRecorder) {
        this.fromRecorder = fromRecorder;
    }

    public boolean isWorkflowItem() {
        return isWorkflowItem;
    }

    public void setWorkflowItem(boolean workflowItem) {
        isWorkflowItem = workflowItem;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getWorkflowStartDate() {
        return workflowStartDate;
    }

    public void setWorkflowStartDate(String workflowStartDate) {
        this.workflowStartDate = workflowStartDate;
    }

    public String getWorkflowStartDateAttributes() {
        return workflowStartDateAttributes;
    }

    public void setWorkflowStartDateAttributes(String workflowStartDateAttributes) {
        this.workflowStartDateAttributes = workflowStartDateAttributes;
    }

    public Integer getWorkflowDueDate() {
        return workflowDueDate;
    }

    public void setWorkflowDueDate(Integer workflowDueDate) {
        this.workflowDueDate = workflowDueDate;
    }

    public String getWorkflowDueDateGranularity() {
        return workflowDueDateGranularity;
    }

    public void setWorkflowDueDateGranularity(String workflowDueDateGranularity) {
        this.workflowDueDateGranularity = workflowDueDateGranularity;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public Integer getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Integer createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public boolean isCurrentCall() {
        return currentCall;
    }

    public void setCurrentCall(boolean currentCall) {
        this.currentCall = currentCall;
    }

    public boolean isCompletedCall() {
        return completedCall;
    }

    public void setCompletedCall(boolean completedCall) {
        this.completedCall = completedCall;
    }

    public boolean isScheduleCall() {
        return scheduleCall;
    }

    public void setScheduleCall(boolean scheduleCall) {
        this.scheduleCall = scheduleCall;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInvitationResponse() {
        return invitationResponse;
    }

    public void setInvitationResponse(String invitationResponse) {
        this.invitationResponse = invitationResponse;
    }
}
