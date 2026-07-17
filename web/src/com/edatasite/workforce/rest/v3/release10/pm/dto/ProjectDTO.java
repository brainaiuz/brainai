package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.ReminderDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * User : Akhror
 * Date : 07.07.2021
 */
public class ProjectDTO {
    private Integer id;

    //project details
    private String number;
    @NotNull(message = "name is required")
    @NotBlank(message = "name cannot be empty")
    private String name;
    private ItemDto customer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "startDate is required")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "dueDate is required")
    private Date dueDate;

    private IdName location;
    private ItemDto status;
    private String description;
    private List<ReminderDto> dueDateReminder;
    private boolean cloneProject;
    private CloneProjectDto copyExisting;
    private boolean billable;

    //involved employees
    private List<ProjectEmployeeDTO> employees;
    private ItemDto manager;
    private List<ItemDto> backupManagers;

    //notes
    private List<NoteDto> notes;

    //attachments
    private List<AttachmentTO> attachments;

    @Valid
    private List<? extends CustomFieldRequest> customFields;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private IdName createdBy;
    private IdName updatedBy;
    private List<CheckInLocationItem> checkInLocations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemDto getCustomer() {
        return customer;
    }

    public void setCustomer(ItemDto customer) {
        this.customer = customer;
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

    public IdName getLocation() {
        return location;
    }

    public void setLocation(IdName location) {
        this.location = location;
    }

    public ItemDto getStatus() {
        return status;
    }

    public void setStatus(ItemDto status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ReminderDto> getDueDateReminder() {
        return dueDateReminder;
    }

    public void setDueDateReminder(List<ReminderDto> dueDateReminder) {
        this.dueDateReminder = dueDateReminder;
    }

    public boolean isCloneProject() {
        return cloneProject;
    }

    public void setCloneProject(boolean cloneProject) {
        this.cloneProject = cloneProject;
    }

    public CloneProjectDto getCopyExisting() {
        return copyExisting;
    }

    public void setCopyExisting(CloneProjectDto copyExisting) {
        this.copyExisting = copyExisting;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public List<ProjectEmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ProjectEmployeeDTO> employees) {
        this.employees = employees;
    }

    public ItemDto getManager() {
        return manager;
    }

    public void setManager(ItemDto manager) {
        this.manager = manager;
    }

    public List<ItemDto> getBackupManagers() {
        return backupManagers;
    }

    public void setBackupManagers(List<ItemDto> backupManagers) {
        this.backupManagers = backupManagers;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public IdName getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IdName createdBy) {
        this.createdBy = createdBy;
    }

    public IdName getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(IdName updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<CheckInLocationItem> getCheckInLocations() {
        return checkInLocations;
    }

    public void setCheckInLocations(List<CheckInLocationItem> checkInLocations) {
        this.checkInLocations = checkInLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectDTO that)) return false;

        return isCloneProject() == that.isCloneProject()
                && isBillable() == that.isBillable()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getNumber(), that.getNumber())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getCustomer(), that.getCustomer())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getDueDate(), that.getDueDate())
                && Objects.equals(getLocation(), that.getLocation())
                && Objects.equals(getStatus(), that.getStatus())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getDueDateReminder(), that.getDueDateReminder())
                && Objects.equals(getCopyExisting(), that.getCopyExisting())
                && Objects.equals(getEmployees(), that.getEmployees())
                && Objects.equals(getManager(), that.getManager())
                && Objects.equals(getBackupManagers(), that.getBackupManagers())
                && Objects.equals(getNotes(), that.getNotes())
                && Objects.equals(getAttachments(), that.getAttachments())
                && Objects.equals(getCustomFields(), that.getCustomFields())
                && Objects.equals(getCreatedAt(), that.getCreatedAt())
                && Objects.equals(getUpdatedAt(), that.getUpdatedAt())
                && Objects.equals(getCreatedBy(), that.getCreatedBy())
                && Objects.equals(getUpdatedBy(), that.getUpdatedBy());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getDueDate() != null ? getDueDate().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getDueDateReminder() != null ? getDueDateReminder().hashCode() : 0);
        result = 31 * result + (isCloneProject() ? 1 : 0);
        result = 31 * result + (getCopyExisting() != null ? getCopyExisting().hashCode() : 0);
        result = 31 * result + (isBillable() ? 1 : 0);
        result = 31 * result + (getEmployees() != null ? getEmployees().hashCode() : 0);
        result = 31 * result + (getManager() != null ? getManager().hashCode() : 0);
        result = 31 * result + (getBackupManagers() != null ? getBackupManagers().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        result = 31 * result + (getCreatedBy() != null ? getCreatedBy().hashCode() : 0);
        result = 31 * result + (getUpdatedBy() != null ? getUpdatedBy().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", customer=" + customer +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", location=" + location +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", dueDateReminder=" + dueDateReminder +
                ", cloneProject=" + cloneProject +
                ", copyExisting=" + copyExisting +
                ", billable=" + billable +
                ", employees=" + employees +
                ", manager=" + manager +
                ", backupManagers=" + backupManagers +
                ", notes=" + notes +
                ", attachments=" + attachments +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
