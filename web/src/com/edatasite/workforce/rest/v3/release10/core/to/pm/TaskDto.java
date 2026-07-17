package com.edatasite.workforce.rest.v3.release10.core.to.pm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.ReminderDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class TaskDto {

    private Integer id;

    //task details
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private ItemDto status;
    private String number;
    private String description;
    @NotNull(message = "Project is required")
    private ItemDto project;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Start Date is required")
    private Date startDate = new Date();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "End Date is required")
    private Date endDate = new Date();
    private boolean allDay = false;
    private IdCode priority;
    private boolean billable;
    private Integer spentTime;

    //assignees
    private List<AssigneeDto> assignees;

    //dependencies
    private IdCode workStream;
    private List<ItemDto> predecessors;
    private List<ItemDto> successors;
    private List<ReminderDto> dueDateReminder;

    //attachments
    private List<AttachmentTO> attachments;

    //notes
    private List<NoteDto> notes;

    @Valid
    private List<? extends CustomFieldRequest> customFields;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private String updatedBy;
    private String customer;
    private String assignedTo;

    public TaskDto() {
    }

    public TaskDto(Integer id, String name, ItemDto status, String number, String description, ItemDto project, Date startDate, Date endDate, boolean allDay, IdCode priority, boolean billable, List<AssigneeDto> assignees, IdCode workStream, List<ItemDto> predecessors, List<ItemDto> successors, List<ReminderDto> dueDateReminder, List<AttachmentTO> attachments, List<NoteDto> notes, List<? extends CustomFieldRequest> customFields, Date createdAt, String createdBy, Date updatedAt, String updatedBy) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.number = number;
        this.description = description;
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.priority = priority;
        this.billable = billable;
        this.assignees = assignees;
        this.workStream = workStream;
        this.predecessors = predecessors;
        this.successors = successors;
        this.dueDateReminder = dueDateReminder;
        this.attachments = attachments;
        this.notes = notes;
        this.customFields = customFields;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemDto getStatus() {
        return status;
    }

    public void setStatus(ItemDto status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public IdCode getPriority() {
        return priority;
    }

    public void setPriority(IdCode priority) {
        this.priority = priority;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public List<AssigneeDto> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<AssigneeDto> assignees) {
        this.assignees = assignees;
    }

    public IdCode getWorkStream() {
        return workStream;
    }

    public void setWorkStream(IdCode workStream) {
        this.workStream = workStream;
    }

    public List<ItemDto> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<ItemDto> predecessors) {
        this.predecessors = predecessors;
    }

    public List<ItemDto> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<ItemDto> successors) {
        this.successors = successors;
    }

    public List<ReminderDto> getDueDateReminder() {
        return dueDateReminder;
    }

    public void setDueDateReminder(List<ReminderDto> dueDateReminder) {
        this.dueDateReminder = dueDateReminder;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(Integer spentTime) {
        this.spentTime = spentTime;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDto)) return false;

        TaskDto taskDto = (TaskDto) o;

        if (isAllDay() != taskDto.isAllDay()) return false;
        if (isBillable() != taskDto.isBillable()) return false;
        if (getId() != null ? !getId().equals(taskDto.getId()) : taskDto.getId() != null) return false;
        if (getName() != null ? !getName().equals(taskDto.getName()) : taskDto.getName() != null) return false;
        if (getStatus() != null ? !getStatus().equals(taskDto.getStatus()) : taskDto.getStatus() != null) return false;
        if (getNumber() != null ? !getNumber().equals(taskDto.getNumber()) : taskDto.getNumber() != null) return false;
        if (getDescription() != null ? !getDescription().equals(taskDto.getDescription()) : taskDto.getDescription() != null)
            return false;
        if (getProject() != null ? !getProject().equals(taskDto.getProject()) : taskDto.getProject() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(taskDto.getStartDate()) : taskDto.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(taskDto.getEndDate()) : taskDto.getEndDate() != null)
            return false;
        if (getPriority() != null ? !getPriority().equals(taskDto.getPriority()) : taskDto.getPriority() != null)
            return false;
        if (getAssignees() != null ? !getAssignees().equals(taskDto.getAssignees()) : taskDto.getAssignees() != null)
            return false;
        if (getWorkStream() != null ? !getWorkStream().equals(taskDto.getWorkStream()) : taskDto.getWorkStream() != null)
            return false;
        if (getPredecessors() != null ? !getPredecessors().equals(taskDto.getPredecessors()) : taskDto.getPredecessors() != null)
            return false;
        if (getSuccessors() != null ? !getSuccessors().equals(taskDto.getSuccessors()) : taskDto.getSuccessors() != null)
            return false;
        if (getDueDateReminder() != null ? !getDueDateReminder().equals(taskDto.getDueDateReminder()) : taskDto.getDueDateReminder() != null)
            return false;
        if (getAttachments() != null ? !getAttachments().equals(taskDto.getAttachments()) : taskDto.getAttachments() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(taskDto.getNotes()) : taskDto.getNotes() != null) return false;
        if (getCustomFields() != null ? !getCustomFields().equals(taskDto.getCustomFields()) : taskDto.getCustomFields() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(taskDto.getCreatedAt()) : taskDto.getCreatedAt() != null)
            return false;
        if (getCreatedBy() != null ? !getCreatedBy().equals(taskDto.getCreatedBy()) : taskDto.getCreatedBy() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(taskDto.getUpdatedAt()) : taskDto.getUpdatedAt() != null)
            return false;
        if (getUpdatedBy() != null ? !getUpdatedBy().equals(taskDto.getUpdatedBy()) : taskDto.getUpdatedBy() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (isAllDay() ? 1 : 0);
        result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
        result = 31 * result + (isBillable() ? 1 : 0);
        result = 31 * result + (getAssignees() != null ? getAssignees().hashCode() : 0);
        result = 31 * result + (getWorkStream() != null ? getWorkStream().hashCode() : 0);
        result = 31 * result + (getPredecessors() != null ? getPredecessors().hashCode() : 0);
        result = 31 * result + (getSuccessors() != null ? getSuccessors().hashCode() : 0);
        result = 31 * result + (getDueDateReminder() != null ? getDueDateReminder().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getCreatedBy() != null ? getCreatedBy().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedBy() != null ? getUpdatedBy().hashCode() : 0);
        result = 31 * result + (getSpentTime() != null ? getSpentTime().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", project=" + project +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", allDay=" + allDay +
                ", priority=" + priority +
                ", billable=" + billable +
                ", assignees=" + assignees +
                ", workStream=" + workStream +
                ", predecessors=" + predecessors +
                ", successors=" + successors +
                ", dueDateReminder=" + dueDateReminder +
                ", attachments=" + attachments +
                ", notes=" + notes +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", spentTime='" + spentTime + '\'' +
                '}';
    }
}
