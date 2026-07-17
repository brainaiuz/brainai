package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CertificateDto {
    private Integer id;
    @NotNull(message = "certificateNumber is required")
    @NotBlank(message = "certificateNumber cannot be empty")
    private String certificateNumber;
    @NotNull(message = "employee is required")
    @NotBlank(message = "employee cannot be empty")
    private ItemDto employee;
    @NotNull(message = "type is required")
    @NotBlank(message = "type cannot be empty")
    private IdName type;
    private String content;
    private List<AttachmentTO> attachments;
    private List<ItemDto> approvers;
    @Valid
    private List<? extends CustomFieldRequest> customFields;
    private CertificateDynamicFieldsDto dynamicFields;

    public CertificateDto() {
    }

    public CertificateDto(Integer id, String certificateNumber, ItemDto employee, IdName type, String content, List<AttachmentTO> attachments, List<ItemDto> approvers, List<? extends CustomFieldRequest> customFields, CertificateDynamicFieldsDto dynamicFields) {
        this.id = id;
        this.certificateNumber = certificateNumber;
        this.employee = employee;
        this.type = type;
        this.content = content;
        this.attachments = attachments;
        this.approvers = approvers;
        this.customFields = customFields;
        this.dynamicFields = dynamicFields;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public IdName getType() {
        return type;
    }

    public void setType(IdName type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public List<ItemDto> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ItemDto> approvers) {
        this.approvers = approvers;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public CertificateDynamicFieldsDto getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(CertificateDynamicFieldsDto dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CertificateDto)) return false;

        CertificateDto that = (CertificateDto) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getCertificateNumber() != null ? !getCertificateNumber().equals(that.getCertificateNumber()) : that.getCertificateNumber() != null)
            return false;
        if (getEmployee() != null ? !getEmployee().equals(that.getEmployee()) : that.getEmployee() != null)
            return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getContent() != null ? !getContent().equals(that.getContent()) : that.getContent() != null) return false;
        if (getAttachments() != null ? !getAttachments().equals(that.getAttachments()) : that.getAttachments() != null)
            return false;
        if (getApprovers() != null ? !getApprovers().equals(that.getApprovers()) : that.getApprovers() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(that.getCustomFields()) : that.getCustomFields() != null)
            return false;
        if (getDynamicFields() != null ? !getDynamicFields().equals(that.getDynamicFields()) : that.getDynamicFields() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCertificateNumber() != null ? getCertificateNumber().hashCode() : 0);
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getApprovers() != null ? getApprovers().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getDynamicFields() != null ? getDynamicFields().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CertificateDto{" +
                "id=" + id +
                ", certificateNumber='" + certificateNumber + '\'' +
                ", employee=" + employee +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", attachments=" + attachments +
                ", approvers=" + approvers +
                ", customFields=" + customFields +
                ", dynamicFields=" + dynamicFields +
                '}';
    }
}
