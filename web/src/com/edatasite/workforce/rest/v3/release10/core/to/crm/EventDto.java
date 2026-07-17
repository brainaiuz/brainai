package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.RelationDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class EventDto {
    //Event
    private Integer id;
    @NotNull(message = "subject is required")
    @NotBlank(message = "subject cannot be blank")
    private String subject;
    private List<String> guests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date endDate;
    private List<IdCode> shares;
    private List<ReminderDto> reminders;
    private String description;

    //related to
    private List<RelationDto> relations;

    //advanced
    private boolean recurrence = false;
    private RecurrenceTypeDto recurrenceType;
    private RecurrenceEndDateDto recurrenceEndDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date recurrenceStartDate;

    @Valid
    private List<? extends CustomFieldRequest> customFields;

    private boolean missed;
    private boolean inbound;
    private boolean outgoing;

    private long duration;
    private Integer eventType;

    private boolean current;
    private boolean completed;
    private boolean scheduled;
    private boolean clone;
    private boolean allDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;

    public EventDto() {
    }

    public EventDto(Integer id, String subject, List<String> guests, Date startDate, Date endDate, List<IdCode> shares, List<ReminderDto> reminders, String description, List<RelationDto> relations, boolean recurrence, RecurrenceTypeDto recurrenceType, RecurrenceEndDateDto recurrenceEndDate, Date recurrenceStartDate, List<? extends CustomFieldRequest> customFields, boolean missed, boolean inbound, boolean outgoing, long duration, Integer eventType, boolean current, boolean completed, boolean scheduled, boolean clone, boolean allDay, Date createdAt, Date updatedAt) {
        this.id = id;
        this.subject = subject;
        this.guests = guests;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shares = shares;
        this.reminders = reminders;
        this.description = description;
        this.relations = relations;
        this.recurrence = recurrence;
        this.recurrenceType = recurrenceType;
        this.recurrenceEndDate = recurrenceEndDate;
        this.recurrenceStartDate = recurrenceStartDate;
        this.customFields = customFields;
        this.missed = missed;
        this.inbound = inbound;
        this.outgoing = outgoing;
        this.duration = duration;
        this.eventType = eventType;
        this.current = current;
        this.completed = completed;
        this.scheduled = scheduled;
        this.clone = clone;
        this.allDay = allDay;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
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

    public List<IdCode> getShares() {
        return shares;
    }

    public void setShares(List<IdCode> shares) {
        this.shares = shares;
    }

    public List<ReminderDto> getReminders() {
        return reminders;
    }

    public void setReminders(List<ReminderDto> reminders) {
        this.reminders = reminders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RelationDto> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationDto> relations) {
        this.relations = relations;
    }

    public boolean isRecurrence() {
        return recurrence;
    }

    public void setRecurrence(boolean recurrence) {
        this.recurrence = recurrence;
    }

    public RecurrenceTypeDto getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceTypeDto recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public RecurrenceEndDateDto getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(RecurrenceEndDateDto recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public Date getRecurrenceStartDate() {
        return recurrenceStartDate;
    }

    public void setRecurrenceStartDate(Date recurrenceStartDate) {
        this.recurrenceStartDate = recurrenceStartDate;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public boolean isMissed() {
        return missed;
    }

    public void setMissed(boolean missed) {
        this.missed = missed;
    }

    public boolean isInbound() {
        return inbound;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDto)) return false;

        EventDto eventDto = (EventDto) o;

        if (isRecurrence() != eventDto.isRecurrence()) return false;
        if (isMissed() != eventDto.isMissed()) return false;
        if (isInbound() != eventDto.isInbound()) return false;
        if (isOutgoing() != eventDto.isOutgoing()) return false;
        if (getDuration() != eventDto.getDuration()) return false;
        if (isCurrent() != eventDto.isCurrent()) return false;
        if (isCompleted() != eventDto.isCompleted()) return false;
        if (isScheduled() != eventDto.isScheduled()) return false;
        if (isClone() != eventDto.isClone()) return false;
        if (isAllDay() != eventDto.isAllDay()) return false;
        if (getId() != null ? !getId().equals(eventDto.getId()) : eventDto.getId() != null) return false;
        if (getSubject() != null ? !getSubject().equals(eventDto.getSubject()) : eventDto.getSubject() != null)
            return false;
        if (getGuests() != null ? !getGuests().equals(eventDto.getGuests()) : eventDto.getGuests() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(eventDto.getStartDate()) : eventDto.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(eventDto.getEndDate()) : eventDto.getEndDate() != null)
            return false;
        if (getShares() != null ? !getShares().equals(eventDto.getShares()) : eventDto.getShares() != null)
            return false;
        if (getReminders() != null ? !getReminders().equals(eventDto.getReminders()) : eventDto.getReminders() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(eventDto.getDescription()) : eventDto.getDescription() != null)
            return false;
        if (getRelations() != null ? !getRelations().equals(eventDto.getRelations()) : eventDto.getRelations() != null)
            return false;
        if (getRecurrenceType() != null ? !getRecurrenceType().equals(eventDto.getRecurrenceType()) : eventDto.getRecurrenceType() != null)
            return false;
        if (getRecurrenceEndDate() != null ? !getRecurrenceEndDate().equals(eventDto.getRecurrenceEndDate()) : eventDto.getRecurrenceEndDate() != null)
            return false;
        if (getRecurrenceStartDate() != null ? !getRecurrenceStartDate().equals(eventDto.getRecurrenceStartDate()) : eventDto.getRecurrenceStartDate() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(eventDto.getCustomFields()) : eventDto.getCustomFields() != null)
            return false;
        if (getEventType() != null ? !getEventType().equals(eventDto.getEventType()) : eventDto.getEventType() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(eventDto.getCreatedAt()) : eventDto.getCreatedAt() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(eventDto.getUpdatedAt()) : eventDto.getUpdatedAt() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getGuests() != null ? getGuests().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getShares() != null ? getShares().hashCode() : 0);
        result = 31 * result + (getReminders() != null ? getReminders().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getRelations() != null ? getRelations().hashCode() : 0);
        result = 31 * result + (isRecurrence() ? 1 : 0);
        result = 31 * result + (getRecurrenceType() != null ? getRecurrenceType().hashCode() : 0);
        result = 31 * result + (getRecurrenceEndDate() != null ? getRecurrenceEndDate().hashCode() : 0);
        result = 31 * result + (getRecurrenceStartDate() != null ? getRecurrenceStartDate().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (isMissed() ? 1 : 0);
        result = 31 * result + (isInbound() ? 1 : 0);
        result = 31 * result + (isOutgoing() ? 1 : 0);
        result = 31 * result + (int) (getDuration() ^ (getDuration() >>> 32));
        result = 31 * result + (getEventType() != null ? getEventType().hashCode() : 0);
        result = 31 * result + (isCurrent() ? 1 : 0);
        result = 31 * result + (isCompleted() ? 1 : 0);
        result = 31 * result + (isScheduled() ? 1 : 0);
        result = 31 * result + (isClone() ? 1 : 0);
        result = 31 * result + (isAllDay() ? 1 : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", guests=" + guests +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", shares=" + shares +
                ", reminders=" + reminders +
                ", description='" + description + '\'' +
                ", relations=" + relations +
                ", recurrence=" + recurrence +
                ", recurrenceType=" + recurrenceType +
                ", recurrenceEndDate=" + recurrenceEndDate +
                ", recurrenceStartDate=" + recurrenceStartDate +
                ", customFields=" + customFields +
                ", missed=" + missed +
                ", inbound=" + inbound +
                ", outgoing=" + outgoing +
                ", duration=" + duration +
                ", eventType=" + eventType +
                ", current=" + current +
                ", completed=" + completed +
                ", scheduled=" + scheduled +
                ", clone=" + clone +
                ", allDay=" + allDay +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
