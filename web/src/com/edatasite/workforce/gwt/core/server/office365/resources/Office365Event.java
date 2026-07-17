package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Event extends Office365BaseResource {
    ArrayList<Office365Attendee> attendees;
    Office365ItemBody body;
    String bodyPreview;
    ArrayList<String> categories;
    String changeKey;
    String createdDateTime;
    Office365DateTimeTimeZone end;
    Boolean hasAttachments;
    String iCalUId;
    String id;
    Importance importance;
    Boolean isAllDay;
    Boolean isCancelled;
    Boolean isOrganizer;
    Boolean isReminderOn;
    String lastModifiedDateTime;
    Office365Location location;
    Office365Recipient organizer;
    String originalEndTimeZone;
    Date originalStart;
    String originalStartTimeZone;
    Office365PatternedRecurrence recurrence;
    Integer reminderMinutesBeforeStart;
    Boolean responseRequested;
    Office365ResponseStatus responseStatus;
    String sensitivity;
    String seriesMasterId;
    Status showAs;
    Office365DateTimeTimeZone start;
    String subject;
    Type type;
    String webLink;

    /**
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/resources/event
     */
    public Office365Event() {
    }

    public enum Type {
        singleInstance, occurrence, exception, seriesMaster
    }

    public enum Status {
        free, tentative, busy, oof, workingElsewhere, unknown
    }

    public enum Sensitivity {
        normal, personal, Private, confidential
    }

    public enum Importance {
        low, normal, high
    }

    public ArrayList<Office365Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<Office365Attendee> attendees) {
        this.attendees = attendees;
    }

    public Office365ItemBody getBody() {
        return body;
    }

    public void setBody(Office365ItemBody body) {
        this.body = body;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Office365DateTimeTimeZone getEnd() {
        return end;
    }

    public void setEnd(Office365DateTimeTimeZone end) {
        this.end = end;
    }

    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public String getiCalUId() {
        return iCalUId;
    }

    public void setiCalUId(String iCalUId) {
        this.iCalUId = iCalUId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public Boolean getIsAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(Boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getIsOrganizer() {
        return isOrganizer;
    }

    public void setIsOrganizer(Boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }

    public Boolean getIsReminderOn() {
        return isReminderOn;
    }

    public void setIsReminderOn(Boolean isReminderOn) {
        this.isReminderOn = isReminderOn;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public Office365Location getLocation() {
        return location;
    }

    public void setLocation(Office365Location location) {
        this.location = location;
    }

    public Office365Recipient getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Office365Recipient organizer) {
        this.organizer = organizer;
    }

    public String getOriginalEndTimeZone() {
        return originalEndTimeZone;
    }

    public void setOriginalEndTimeZone(String originalEndTimeZone) {
        this.originalEndTimeZone = originalEndTimeZone;
    }

    public Date getOriginalStart() {
        return originalStart;
    }

    public void setOriginalStart(Date originalStart) {
        this.originalStart = originalStart;
    }

    public String getOriginalStartTimeZone() {
        return originalStartTimeZone;
    }

    public void setOriginalStartTimeZone(String originalStartTimeZone) {
        this.originalStartTimeZone = originalStartTimeZone;
    }

    public Office365PatternedRecurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Office365PatternedRecurrence recurrence) {
        this.recurrence = recurrence;
    }

    public Integer getReminderMinutesBeforeStart() {
        return reminderMinutesBeforeStart;
    }

    public void setReminderMinutesBeforeStart(Integer reminderMinutesBeforeStart) {
        this.reminderMinutesBeforeStart = reminderMinutesBeforeStart;
    }

    public Boolean getResponseRequested() {
        return responseRequested;
    }

    public void setResponseRequested(Boolean responseRequested) {
        this.responseRequested = responseRequested;
    }

    public Office365ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Office365ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getSeriesMasterId() {
        return seriesMasterId;
    }

    public void setSeriesMasterId(String seriesMasterId) {
        this.seriesMasterId = seriesMasterId;
    }

    public Status getShowAs() {
        return showAs;
    }

    public void setShowAs(Status showAs) {
        this.showAs = showAs;
    }

    public Office365DateTimeTimeZone getStart() {
        return start;
    }

    public void setStart(Office365DateTimeTimeZone start) {
        this.start = start;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
