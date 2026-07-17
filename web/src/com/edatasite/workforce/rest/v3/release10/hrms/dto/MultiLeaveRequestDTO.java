package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MultiLeaveRequestDTO {
    @NotNull(message = "formId is required")
    @NotBlank(message = "formId cannot be empty")
    private String formId;
    @NotNull(message = "entityId is required")
    private Integer entityId;
    @NotNull(message = "lrs are required")
    private List<LeaveRequestDTO> lrs;

    public MultiLeaveRequestDTO() {
    }

    public MultiLeaveRequestDTO(String formId, Integer entityId, List<LeaveRequestDTO> lrs) {
        this.formId = formId;
        this.entityId = entityId;
        this.lrs = lrs;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public List<LeaveRequestDTO> getLrs() {
        return lrs;
    }

    public void setLrs(List<LeaveRequestDTO> lrs) {
        this.lrs = lrs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiLeaveRequestDTO)) return false;

        MultiLeaveRequestDTO that = (MultiLeaveRequestDTO) o;

        if (formId != null ? !formId.equals(that.formId) : that.formId != null) return false;
        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
        if (lrs != null ? !lrs.equals(that.lrs) : that.lrs != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = formId != null ? formId.hashCode() : 0;
        result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
        result = 31 * result + (lrs != null ? lrs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MultiLeaveRequestDTO{" +
                "formId='" + formId + '\'' +
                ", entityId=" + entityId +
                ", lrs=" + lrs +
                '}';
    }
}
