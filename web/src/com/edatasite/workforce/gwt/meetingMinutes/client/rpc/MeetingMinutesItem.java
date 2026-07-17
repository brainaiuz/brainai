package com.edatasite.workforce.gwt.meetingMinutes.client.rpc;


import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MeetingAttendeesItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Markedable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: developer
 * Date: 4/19/12
 * Time: 3:17 PM
 */
public class MeetingMinutesItem implements IsSerializable, Markedable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String DESCRIPTION = "description";
    public static final String PURPOSE = "purpose";
    public static final String LOCATION = "location";
    public static final String TYPE = "type";
    public static final String MEETING_ID = "meetingId";
    public static final String CALLED_BY = "calledBy";
    public static final String DATE = "date";
    public static final String END_DATE = "enddate";
    public static final String PREPARED_BY = "preparedBy";
    public static final String TIME = "time";

    private Integer objectID;
    private SelectItem emailTemplate;
    private String name;
    private String layoutHTML;
    private String meetingNumber;
    private Integer intNumber;
    private String location;
    private String purpose;
    private String nonCompanyAttendees;
    private SelectItem type;
    private ProjectItem projectItem;
    private Date startdate;
    private Date enddate;
    private SelectItem calledBy;
    private SelectItem preparedBy;
    private DateNonConvertable nextMeetingDate;
    private Boolean newMeetingMinutes;
    private Boolean sendNotifToAttendees;
    private FileItem[] attachments;
    private HistoryListItem[] historyListItem;

    private HashMap<String, Object> customFields;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<SelectItem> employees = new ArrayList<>();
    private SelectItem[] types = new SelectItem[]{};
    private ArrayList<AgendaTopicItem> agendaTopicItems = new ArrayList<>();
    private ArrayList<AgendaTopicItem> discussionPoint = new ArrayList<>();
    private ArrayList<MeetingAttendeesItem> meetingAbsentItem = new ArrayList<>();
    private SelectItem[] emailTemplates;

    public ArrayList<MeetingAttendeesItem> getMeetingAbsentItem() {
        return meetingAbsentItem;
    }

    public void setMeetingAbsentItem(ArrayList<MeetingAttendeesItem> meetingAbsentItem) {
        this.meetingAbsentItem = meetingAbsentItem;
    }

    public ArrayList<SelectItem> getEmployees() {
        return employees;
    }

    public SelectItem[] getEmailTemplates() {
        return emailTemplates;
    }

    public SelectItem getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(SelectItem emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public String getNonCompanyAttendees() {
        return nonCompanyAttendees;
    }

    public void setNonCompanyAttendees(String nonCompanyAttendees) {
        this.nonCompanyAttendees = nonCompanyAttendees;
    }

    public void setEmailTemplates(SelectItem[] emailTemplates) {
        this.emailTemplates = emailTemplates;
    }

    public void setEmployees(ArrayList<SelectItem> employees) {
        this.employees = employees;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public ArrayList<AgendaTopicItem> getDiscussionPoint() {
        return discussionPoint;
    }

    public void setDiscussionPoint(ArrayList<AgendaTopicItem> discussionPoint) {
        this.discussionPoint = discussionPoint;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    private NumberData numberData;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public ProjectItem getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(ProjectItem projectItem) {
        this.projectItem = projectItem;
    }

    public SelectItem getCalledBy() {
        return calledBy;
    }

    public void setCalledBy(SelectItem calledBy) {
        this.calledBy = calledBy;
    }

    public SelectItem getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(SelectItem preparedBy) {
        this.preparedBy = preparedBy;
    }

    public DateNonConvertable getNextMeetingDate() {
        return nextMeetingDate;
    }

    public void setNextMeetingDate(Date nextMeetingDate) {
        this.nextMeetingDate = new DateNonConvertable(nextMeetingDate);
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    @Override
    public Object getCustomFieldsValue(String key) {
        return customFields.get(key);
    }

    @Override
    public void setCustomFieldsValue(String key, Object value) {
        customFields.put(key, value);
    }

    @Override
    public Boolean isMarked() {
        return newMeetingMinutes;
    }

    @Override
    public void setMarked(Boolean marked) {
        newMeetingMinutes = marked;
    }

    public Boolean isSendNotifToAttendees() {
        return sendNotifToAttendees;
    }

    public void setSendNotifToAttendees(Boolean sendNotifToAttendees) {
        this.sendNotifToAttendees = sendNotifToAttendees;
    }

    public ArrayList<AgendaTopicItem> getAgendaTopicItems() {
        return agendaTopicItems;
    }

    public void setAgendaTopicItems(ArrayList<AgendaTopicItem> agendaTopicItems) {
        this.agendaTopicItems = agendaTopicItems;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public HistoryListItem[] getHistoryListItem() {
        return historyListItem;
    }

    public void setHistoryListItem(HistoryListItem[] historyListItem) {
        this.historyListItem = historyListItem;
    }

}
