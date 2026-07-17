package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.RelationDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CaseDto {

    private Integer id;

    //case description
    @NotNull(message = "Subject is required")
    @NotBlank(message = "Subject cannot be blank")
    private String subject;
    private CaseReporterDto reportedBy;
    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private String number;

    //case information
    private IdCode status;
    private IdCode priority;
    private IdCode type;
    private ItemDto assignee;
    private ItemDto resolver;
    private IdCode origin;
    private IdCode reason;

    //notes
    private List<NoteDto> notes;

    //attachments
    private List<AttachmentTO> attachments;

    //relations
    private List<RelationDto> relations;

    @Valid
    private List<? extends CustomFieldRequest> customFields;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;

    public CaseDto() {
    }

    public CaseDto(Integer id, String subject, CaseReporterDto reportedBy, String description, String number, IdCode status, IdCode priority, IdCode type, ItemDto assignee, ItemDto resolver, IdCode origin, IdCode reason, List<NoteDto> notes, List<AttachmentTO> attachments, List<RelationDto> relations, List<? extends CustomFieldRequest> customFields, Date createdAt, Date updatedAt) {
        this.id = id;
        this.subject = subject;
        this.reportedBy = reportedBy;
        this.description = description;
        this.number = number;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.assignee = assignee;
        this.resolver = resolver;
        this.origin = origin;
        this.reason = reason;
        this.notes = notes;
        this.attachments = attachments;
        this.relations = relations;
        this.customFields = customFields;
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

    public CaseReporterDto getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(CaseReporterDto reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IdCode getStatus() {
        return status;
    }

    public void setStatus(IdCode status) {
        this.status = status;
    }

    public IdCode getPriority() {
        return priority;
    }

    public void setPriority(IdCode priority) {
        this.priority = priority;
    }

    public IdCode getType() {
        return type;
    }

    public void setType(IdCode type) {
        this.type = type;
    }

    public ItemDto getAssignee() {
        return assignee;
    }

    public void setAssignee(ItemDto assignee) {
        this.assignee = assignee;
    }

    public ItemDto getResolver() {
        return resolver;
    }

    public void setResolver(ItemDto resolver) {
        this.resolver = resolver;
    }

    public IdCode getOrigin() {
        return origin;
    }

    public void setOrigin(IdCode origin) {
        this.origin = origin;
    }

    public IdCode getReason() {
        return reason;
    }

    public void setReason(IdCode reason) {
        this.reason = reason;
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

    public List<RelationDto> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationDto> relations) {
        this.relations = relations;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseDto)) return false;

        CaseDto caseDto = (CaseDto) o;

        if (getId() != null ? !getId().equals(caseDto.getId()) : caseDto.getId() != null) return false;
        if (getSubject() != null ? !getSubject().equals(caseDto.getSubject()) : caseDto.getSubject() != null)
            return false;
        if (getReportedBy() != null ? !getReportedBy().equals(caseDto.getReportedBy()) : caseDto.getReportedBy() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(caseDto.getDescription()) : caseDto.getDescription() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(caseDto.getNumber()) : caseDto.getNumber() != null) return false;
        if (getStatus() != null ? !getStatus().equals(caseDto.getStatus()) : caseDto.getStatus() != null) return false;
        if (getPriority() != null ? !getPriority().equals(caseDto.getPriority()) : caseDto.getPriority() != null)
            return false;
        if (getType() != null ? !getType().equals(caseDto.getType()) : caseDto.getType() != null) return false;
        if (getAssignee() != null ? !getAssignee().equals(caseDto.getAssignee()) : caseDto.getAssignee() != null)
            return false;
        if (getResolver() != null ? !getResolver().equals(caseDto.getResolver()) : caseDto.getResolver() != null)
            return false;
        if (getOrigin() != null ? !getOrigin().equals(caseDto.getOrigin()) : caseDto.getOrigin() != null) return false;
        if (getReason() != null ? !getReason().equals(caseDto.getReason()) : caseDto.getReason() != null) return false;
        if (getNotes() != null ? !getNotes().equals(caseDto.getNotes()) : caseDto.getNotes() != null) return false;
        if (getAttachments() != null ? !getAttachments().equals(caseDto.getAttachments()) : caseDto.getAttachments() != null)
            return false;
        if (getRelations() != null ? !getRelations().equals(caseDto.getRelations()) : caseDto.getRelations() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(caseDto.getCustomFields()) : caseDto.getCustomFields() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(caseDto.getCreatedAt()) : caseDto.getCreatedAt() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(caseDto.getUpdatedAt()) : caseDto.getUpdatedAt() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getReportedBy() != null ? getReportedBy().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getAssignee() != null ? getAssignee().hashCode() : 0);
        result = 31 * result + (getResolver() != null ? getResolver().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getRelations() != null ? getRelations().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CaseDto{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", reportedBy=" + reportedBy +
                ", description='" + description + '\'' +
                ", number='" + number + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", type=" + type +
                ", assignee=" + assignee +
                ", resolver=" + resolver +
                ", origin=" + origin +
                ", reason=" + reason +
                ", notes=" + notes +
                ", attachments=" + attachments +
                ", relations=" + relations +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
