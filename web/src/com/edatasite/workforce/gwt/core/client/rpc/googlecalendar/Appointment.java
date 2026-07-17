package com.edatasite.workforce.gwt.core.client.rpc.googlecalendar;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Appointment extends Relational implements Comparable<Appointment>, IsSerializable, ListingCustomFields {
    public static final int EVENT = 1;
    public static final int CALL_LOG = 2;
    public static final int INTERVIEW = 3;
    public static final int VOLUNTEER_SHIFT = 4;
    public static final int SMS = 5;
    public static final int CALL_AND_SMS = 6;
    public static final int WHATSAPP = 7;

    public static final String ID = "Id";
    public static final String TYPE_EVENT = "Event";
    public static final String TYPE_CALL_LOG = "Call";
    public static final String TYPE_INTERVIEW = "Interview";
    public static final String TYPE_SMS = "Sms";
    public static final String SUBJECT = "SUBJECT";
    public static final String CALL_TYPE = "Call Type";
    public static final String START_DATE = "Start Date";
    public static final String END_DATE = "END Date";
    public static final String VENUE = "Venue";
    public static final String DESCRIPTION = "Description";
    public static final String ASSIGNEE = "Assignee";
    public static final String EVENT_TYPE = "Event type";
    public static final String TODAY = "TODAY";
    public static final String UPDATED_DATE = "updated_date";
    public static final String UPDATER = "updater";
    public static final String CREATED_DATE = "created_date";
    public static final String CREATER = "creator";
    public static final String DURATION = "duration";
    public static final String LEAD_RELATION = "LEAD_RELATION";
    public static final String CONTACT_RELATION = "CONTACT_RELATION";
    public static final String CRM_ACCOUNT_RELATION = "CRM_ACCOUNT_RELATION";
    public static final String CANDIDATE_RELATION = "CANDIDATE_RELATION";
    public static final String EMPLOYEE_RELATION = "EMPLOYEE_RELATION";
    public static final String PHONE = "PHONE";


    public static int DEFAULT_HEIGHT = 21;
    private static final String STYLE_PREFIX = "gwt-appointment-";
    public static final String BLUE = STYLE_PREFIX + "blue";
    public static final String AQUA = STYLE_PREFIX + "aqua";
    public static final String BLUE_SHORT = "appointment--short " + STYLE_PREFIX + "blue";
    public static final String RED = STYLE_PREFIX + "red";
    public static final String PINK = STYLE_PREFIX + "pink";
    public static final String PURPLE = STYLE_PREFIX + "purple";
    public static final String DARK_PURPLE = STYLE_PREFIX + "darkpurple";
    public static final String GREEN = STYLE_PREFIX + "green";
    public static final String YELLOW = STYLE_PREFIX + "yellow";
    public static final String ORANGE = STYLE_PREFIX + "orange";

    public static final String ADD_NEW_EVENT = "add_new_event";
    public static final int FROM_CRM = 1;
    public static final int FROM_HRMS = 2;
    public static final int FROM_BOTH = 3;

    private Integer objectID;
    private Integer ownerID;
    private String ownerName;
    private Integer recurrenceId;
    private Date fireTime;
    private String googleID;
    private String officeID;
    private String subject;
    private String description;
    private Date startDate;
    private Date endDate;
    private Date startDateClone;
    private Date endDateClone;
    private String location;
    private String address1;
    private String address2;
    private String city;
    private Integer countryId;
    private String postCode;
    private String organizationName;
    private String organizationDescription;
    private String createdBy;
    private Date createdDate;
    private String style = BLUE;
    private String action;
    private ArrayList<SelectItem> guests;
    private HashMap<String, Object> customFieldsMap;
    private boolean sendEmailNotification = true;
    private boolean includeAttachments = true;
    private FileItem[] attachments;
    private Integer attachmentFolderID;
    private ArrayList<PositionsSelectItem> sharedEmployees;
    private String sharedEmployeesString; //ex: First Emp, Second Emp, Third Emp,... etc
    private Integer logoId;
    //private ArrayList<BookingReservationItem> bookingReservationItemList = new ArrayList<>();
    /*task summary link*/
    private String linkURL;
    private Integer projectID;
    private Integer priorityID;
    private String projectName;
    private String priorityName;
    private String taskCreator;
    private Integer instancesCount;
    private Integer newGuestsCount;
    private String numberData;
    private IdTime[] projectEmployees;
    private Integer locationId;
    /**
     * All boolean values accepts false by default.
     */
    private boolean multiDay = false;
    private boolean allDay = true;
    private boolean clone = false;
    private boolean allDayClone = true;
    private boolean visible = true;
    private boolean hasGoogleAccount = false;
    private boolean editable = true;
    private boolean recurring = false;
    private boolean inboundCall = false;
    private boolean outboundCall = false;
    private boolean missedCall = false;
    private boolean currentCall;
    private boolean complatedCall;
    private boolean scheduleCall;
    private long callDuration;
    private String twilioCallSID;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private RelationItem contactRelation;
    private RelationItem leadRelation;
    private RelationItem crmAccountRelation;
    private RelationItem candidateRelation;
    private RelationItem employeeRelation;
    private RecurrenceJobItem recurrenceJobItem;
    private ArrayList<Attendee> attendees = new ArrayList<>();
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private int activityType = EVENT;
    private boolean isCopy;
    /**
     * Booking event
     */
    private boolean isBooking = false;
    private boolean isTaskCompleted = false;
    private Integer maxAttendents;

    private boolean isPublic = false;
    private Integer totalTicketCount;
    private Boolean isPublished = false;
    private Integer sold;
    private Integer remaining;
    private String websiteNumber;
    private String domain;
    /**
     * these params of public event for create website each public event
     */
    private Integer templateID;

    private TicketItem[] tickets;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;

    private boolean isWorkflowItem;
    private Integer workflowID;
    private String workflowModule;
    private String workflowStartDate;
    private String workflowStartDateAttributes;
    private Integer workflowDueDate;
    private String workflowDueDateGranularity;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private Integer createdFrom = FROM_CRM;
    private Boolean isPrivate;
    private Boolean isOwner;
    private Integer shiftStatus;
    private String shiftStatusName;
    private Integer activityId;
    private boolean isTask = false;
    private String border = "#006600";
    private boolean noTask = false;
    private boolean registerWorkFlowEventPerDate = true;
    private boolean registerNestedWorkflowEvents = true;
    private String asteriskid;
    private String phoneNumber;
    private String invitationResponse;
    private String templateSubject;
    private String templateValue;
    private SelectItem template;
    private Integer zoomObjectId;
    private String zoomLink;

    public Appointment() {

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

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public Appointment(Date startDate) {
        this.startDate = startDate;
        this.endDate = DateUtil.addMinutes(startDate, 30);
    }

    public Date getStartDateClone() {
        return startDateClone;
    }

    public void setStartDateClone(Date startDateClone) {
        this.startDateClone = startDateClone;
    }

    public Date getEndDateClone() {
        return endDateClone;
    }

    public void setEndDateClone(Date endDateClone) {
        this.endDateClone = endDateClone;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }

    public boolean isAllDayClone() {
        return allDayClone;
    }

    public void setAllDayClone(boolean allDayClone) {
        this.allDayClone = allDayClone;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer id) {
        this.objectID = id;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

    public boolean isMultiDay() {
        return multiDay;
    }

    public void setMultiDay(boolean isMultiDay) {
        this.multiDay = isMultiDay;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean hasGoogleAccount() {
        return hasGoogleAccount;
    }

    public void setHasGoogleAccount(boolean hasGoogleAccount) {
        this.hasGoogleAccount = hasGoogleAccount;
    }

    public ArrayList<CalendarEventReminder> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<CalendarEventReminder> reminder) {
        this.reminder = reminder;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public IdTime[] getProjectEmployees() {
        return projectEmployees;
    }

    public void setProjectEmployees(IdTime[] projectEmployees) {
        this.projectEmployees = projectEmployees;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getInstancesCount() {
        return instancesCount;
    }

    public void setInstancesCount(Integer instancesCount) {
        this.instancesCount = instancesCount;
    }

    public Integer getNewGuestsCount() {
        return newGuestsCount;
    }

    public void setNewGuestsCount(Integer newGuestsCount) {
        this.newGuestsCount = newGuestsCount;
    }

    public ArrayList<SelectItem> getGuests() {
        if (guests == null) {
            guests = new ArrayList<>();
        }
        return guests;
    }

    public void setGuests(ArrayList<SelectItem> guests) {
        this.guests = guests;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public boolean isIncludeAttachments() {
        return includeAttachments;
    }

    public void setIncludeAttachments(boolean includeAttachments) {
        this.includeAttachments = includeAttachments;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer getAttachmentFolderID() {
        return attachmentFolderID;
    }

    public void setAttachmentFolderID(Integer attachmentFolderID) {
        this.attachmentFolderID = attachmentFolderID;
    }

    public ArrayList<PositionsSelectItem> getSharedEmployees() {
        return sharedEmployees;
    }

    public void setSharedEmployees(ArrayList<PositionsSelectItem> sharedEmployees) {
        this.sharedEmployees = sharedEmployees;
    }

    public String getSharedEmployeesString() {
        return sharedEmployeesString;
    }

    public void setSharedEmployeesString(String sharedEmployeesString) {
        this.sharedEmployeesString = sharedEmployeesString;
    }

    public Appointment clone() {
        Appointment clone = new Appointment();
        clone.setAllDay(this.allDay);
        clone.setAttendees(this.attendees);
        clone.setCreatedBy(this.createdBy);
        clone.setCreatedDate(this.createdDate);
        clone.setDescription(this.description);
        clone.setEndDate(this.endDate);
        clone.setLocation(this.location);
        clone.setMultiDay(this.multiDay);
        clone.setStartDate(this.startDate);
        clone.setStyle(this.style);
        clone.setSubject(this.subject);
        clone.setCreatedFrom(this.createdFrom);
        clone.setRecurrenceJobItem(this.recurrenceJobItem);
        clone.setProjectID(this.projectID);
        clone.setPriorityID(this.priorityID);
        clone.setInstancesCount(this.instancesCount);
        clone.setGuests(this.guests);
        clone.setSendEmailNotification(this.sendEmailNotification);
        clone.setIncludeAttachments(this.includeAttachments);
        clone.setAttachments(this.attachments);
        clone.setAttachmentFolderID(this.attachmentFolderID);
        clone.setSharedEmployees(this.sharedEmployees);
        clone.setSharedEmployeesString(this.sharedEmployeesString);
        clone.setProjectEmployees(this.projectEmployees);
        clone.setBooking(this.isBooking);
        clone.setTaskCompleted(this.isTaskCompleted);
        clone.setMaxAttendents(this.maxAttendents);
        clone.setRelations(this.getRelations());
        clone.setPublic(this.isPublic());
        clone.setIsPrivate(this.getIsPrivate());
        clone.setIsOwner(this.getIsOwner());
        clone.setTemplateID(this.getTemplateID());
        clone.setTickets(this.getTickets());
        //clone.setBookingReservationItemList(this.getBookingReservationItemList());
        clone.setShiftStatus(this.getShiftStatus());
        clone.setShiftStatusName(this.getShiftStatusName());
        clone.setRegisterNestedWorkflowEvents(this.isRegisterNestedWorkflowEvents());
        clone.setSendEmailNotification(this.isSendEmailNotification());
        return clone;
    }

    public int compareTo(Appointment appointment) {
        int compare = this.getStartDate().compareTo(appointment.getStartDate());

        if (compare == 0) {
            compare = appointment.getEndDate().compareTo(this.getEndDate());
        }

        return compare;
    }

    public boolean isMultiDayAppointment() {
        if (endDate != null && startDate != null) {
            return DateUtil.isMoreThanOneDay(startDate, endDate);
        }

        throw new IllegalStateException("Calculating isMultiDayAppointment with no start/end dates set");
    }

    /*public boolean isAllDayAppointment() {
        return DateUtil.isOneDay(start, end);
    }*/

    /*public boolean isAllDayAppointment() {
        long difference = !allDay ? (endDate.getTime() - startDate.getTime()) : (endDate.getTime() - startDate.getTime() + 1000);
        long diff = (!allDay) ? MILLIS_IN_A_DAY : (MILLIS_IN_A_DAY - 1000);
        boolean multiDay = difference / MILLIS_IN_A_DAY > 0 && difference % MILLIS_IN_A_DAY == 0;
        return difference == diff || difference == MILLIS_IN_A_DAY || multiDay;
    }

    public void makeAllDayAppointment() {
        startDate = DateUtil.resetTime((Date) startDate.clone());
        if (endDate == null) {
            endDate = DateUtil.getDayLastTime((Date) startDate.clone());
        } else {
            endDate = DateUtil.getDayLastTime((Date) endDate.clone());
        }
    }*/

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(String taskCreator) {
        this.taskCreator = taskCreator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public boolean isBooking() {
        return isBooking;
    }

    public void setBooking(boolean booking) {
        isBooking = booking;
    }

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }

    public void setTaskCompleted(boolean taskCompleted) {
        isTaskCompleted = taskCompleted;
    }

    public Integer getMaxAttendents() {
        return maxAttendents;
    }

    public void setMaxAttendents(Integer maxAttendents) {
        this.maxAttendents = maxAttendents;
    }

    public boolean isCallLog() {
        return activityType == CALL_LOG;
    }

    public boolean isInterview() {
        return activityType == INTERVIEW;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType != null ? activityType : this.activityType;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public TicketItem[] getTickets() {
        return tickets;
    }

    public ArrayList<TicketItem> getTicketsAsList() {
        ArrayList<TicketItem> ticketList = new ArrayList<>();

        if (tickets != null && tickets.length > 0) {
            for (TicketItem item : tickets) {
                TicketItem ticket = new TicketItem();
                ticket.setName(item.getName());
                ticket.setObjectID(item.getObjectID());
                ticket.setQty(item.getQty());
                ticket.setPrice(item.getPrice());

                ticketList.add(ticket);
            }
        }
        return ticketList;
    }

    public void setTickets(TicketItem[] tickets) {
        this.tickets = tickets;
    }

//    public ArrayList<BookingReservationItem> getBookingReservationItemList() {
//        if (bookingReservationItemList == null) {
//            bookingReservationItemList = new ArrayList<>();
//        }
//        return bookingReservationItemList;
//    }

//    public void setBookingReservationItemList(ArrayList<BookingReservationItem> bookingReservationItemList) {
//        this.bookingReservationItemList = bookingReservationItemList;
//    }

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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    public Integer getLogoId() {
        return logoId;
    }

    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
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

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getWebsiteNumber() {
        return websiteNumber;
    }

    public void setWebsiteNumber(String websiteNumber) {
        this.websiteNumber = websiteNumber;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_EVENT;
    }

    @Override
    public String getRelationName() {
        return getSubject();
    }

    public boolean isInboundCall() {
        return inboundCall;
    }

    public void setInboundCall(boolean inboundCall) {
        this.inboundCall = inboundCall;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public boolean isWorkflowItem() {
        return isWorkflowItem;
    }

    public void setWorkflowItem(boolean isWorkflowItem) {
        this.isWorkflowItem = isWorkflowItem;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
        setWorkflowItem(workflowID != null);
    }

    public String getWorkflowModule() {
        return workflowModule;
    }

    public void setWorkflowModule(String workflowModule) {
        this.workflowModule = workflowModule;
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

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public Integer getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Integer createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Boolean getIsPrivate() {
        return isPrivate != null ? isPrivate : false;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsOwner() {
        return isOwner != null ? isOwner : false;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    public Integer getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public String getShiftStatusName() {
        return shiftStatusName;
    }

    public void setShiftStatusName(String shiftStatusName) {
        this.shiftStatusName = shiftStatusName;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setIsTask(boolean isTask) {
        this.isTask = isTask;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorder() {
        return border;
    }

    public void setNoTask(boolean noTask) {
        this.noTask = noTask;
    }

    public boolean isNoTask() {
        return noTask;
    }

    public boolean isRegisterWorkFlowEventPerDate() {
        return registerWorkFlowEventPerDate;
    }

    public void setRegisterWorkFlowEventPerDate(boolean registerWorkFlowEventPerDate) {
        this.registerWorkFlowEventPerDate = registerWorkFlowEventPerDate;
    }

    public boolean isRegisterNestedWorkflowEvents() {
        return registerNestedWorkflowEvents;
    }

    public void setRegisterNestedWorkflowEvents(boolean registerNestedWorkflowEvents) {
        this.registerNestedWorkflowEvents = registerNestedWorkflowEvents;
    }

    public String getAsteriskid() {
        return asteriskid;
    }

    public void setAsteriskid(String asteriskid) {
        this.asteriskid = asteriskid;
    }

    public boolean isCurrentCall() {
        return this.currentCall;
    }

    public void setCurrentCall(final boolean currentCall) {
        this.currentCall = currentCall;
    }

    public boolean isComplatedCall() {
        return this.complatedCall;
    }

    public void setComplatedCall(final boolean complatedCall) {
        this.complatedCall = complatedCall;
    }

    public boolean isScheduleCall() {
        return this.scheduleCall;
    }

    public void setScheduleCall(final boolean scheduleCall) {
        this.scheduleCall = scheduleCall;
    }

    public boolean isOutboundCall() {
        return this.outboundCall;
    }

    public void setOutboundCall(final boolean outboundCall) {
        this.outboundCall = outboundCall;
    }

    public boolean isMissedCall() {
        return this.missedCall;
    }

    public void setMissedCall(final boolean missedCall) {
        this.missedCall = missedCall;
    }

    public boolean isCopy() {
        return this.isCopy;
    }

    public void setCopy(final boolean copy) {
        this.isCopy = copy;
    }

    public RelationItem getContactRelation() {
        return this.contactRelation;
    }

    public void setContactRelation(final RelationItem contactRelation) {
        this.contactRelation = contactRelation;
    }

    public RelationItem getLeadRelation() {
        return this.leadRelation;
    }

    public void setLeadRelation(final RelationItem leadRelation) {
        this.leadRelation = leadRelation;
    }

    public RelationItem getCrmAccountRelation() {
        return this.crmAccountRelation;
    }

    public void setCrmAccountRelation(final RelationItem crmAccountRelation) {
        this.crmAccountRelation = crmAccountRelation;
    }

    public RelationItem getEmployeeRelation() {
        return employeeRelation;
    }

    public void setEmployeeRelation(RelationItem employeeRelation) {
        this.employeeRelation = employeeRelation;
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

    public RelationItem getCandidateRelation() {
        return candidateRelation;
    }

    public void setCandidateRelation(RelationItem candidateRelation) {
        this.candidateRelation = candidateRelation;
    }

    public RelationItem getExistingRelation() {
        return getLeadRelation() != null && getLeadRelation().getToID() != null ? getLeadRelation() : getCrmAccountRelation() != null && getCrmAccountRelation().getToID() != null ? getCrmAccountRelation() : getCandidateRelation() != null && getCandidateRelation().getToID() != null ? getCandidateRelation() : getEmployeeRelation() != null && getEmployeeRelation().getToID() != null ? getEmployeeRelation() : getContactRelation();
    }

    public SelectItem getTemplate() {
        return template;
    }

    public void setTemplate(SelectItem template) {
        this.template = template;
    }

    public String getTemplateValue() {
        return templateValue;
    }

    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }

    public String getTemplateSubject() {
        return templateSubject;
    }

    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }

    public Integer getZoomObjectId() {
        return zoomObjectId;
    }

    public void setZoomObjectId(Integer zoomObjectId) {
        this.zoomObjectId = zoomObjectId;
    }

    public String getZoomLink() {
        return zoomLink;
    }

    public void setZoomLink(String zoomLink) {
        this.zoomLink = zoomLink;
    }
}
