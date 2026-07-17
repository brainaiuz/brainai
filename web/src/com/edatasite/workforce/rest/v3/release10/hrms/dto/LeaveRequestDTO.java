package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveReasonStateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User : Akhror
 * Date : 10.07.2021
 */
public class LeaveRequestDTO {
    private Integer id;

    private ItemDto employee;
    private ItemDto reason;
    @Schema(description = "Date Format (format: yyyy-MM-dd HH:mm:ss)")
    @NotNull(message = "startDate is required")
    private Date startDate;
    @Schema(description = "Date Format (format: yyyy-MM-dd HH:mm:ss)")
    @NotNull(message = "endDate is required")
    private Date endDate;
    @Pattern(regexp = "ST_PAID|NON_PAID", message = "type can be either ST_PAID or NON_PAID")
    private String type;
    @Pattern(regexp = "MONEY|DAY", message = "takeLeaveBy can be either MONEY or DAY")
    private String takeLeaveBy;
    private String description;
    @NotNull(message = "approver is required")
    private List<ItemDto> approver;
    private ApproverListStatusTO status;

    private List<AttachmentTO> attachments;
    private ArrayList<LeaveReasonStateTO> state_records;
    private RequestUserActionTO userAction;
    private boolean isAutoApprove;
    private Integer currentApproverId;
    private Boolean isCurrentApprover;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateUTC;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateUTC;
    private String leaveDays;


    @Valid
    private List<? extends CustomFieldRequest> customFields;

    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(Integer id, ItemDto employee, ItemDto reason, Date startDate, Date endDate, String type, String takeLeaveBy, String description, List<ItemDto> approver, List<AttachmentTO> attachments, boolean isAutoApprove, List<? extends CustomFieldRequest> customFields) {
        this.id = id;
        this.employee = employee;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.takeLeaveBy = takeLeaveBy;
        this.description = description;
        this.approver = approver;
        this.attachments = attachments;
        this.isAutoApprove = isAutoApprove;
        this.customFields = customFields;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public ItemDto getReason() {
        return reason;
    }

    public void setReason(ItemDto reason) {
        this.reason = reason;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTakeLeaveBy() {
        return takeLeaveBy;
    }

    public void setTakeLeaveBy(String takeLeaveBy) {
        this.takeLeaveBy = takeLeaveBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemDto> getApprover() {
        return approver;
    }

    public void setApprover(List<ItemDto> approver) {
        this.approver = approver;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public boolean isAutoApprove() {
        return isAutoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        isAutoApprove = autoApprove;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<LeaveReasonStateTO> getState_records() {
        return state_records;
    }

    public void setState_records(ArrayList<LeaveReasonStateTO> state_records) {
        this.state_records = state_records;
    }

    public ApproverListStatusTO getStatus() {
        return status;
    }

    public void setStatus(ApproverListStatusTO status) {
        this.status = status;
    }

    public RequestUserActionTO getUserAction() {
        return userAction;
    }

    public void setUserAction(RequestUserActionTO userAction) {
        this.userAction = userAction;
    }

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public Boolean getCurrentApprover() {
        return isCurrentApprover;
    }

    public void setCurrentApprover(Boolean currentApprover) {
        isCurrentApprover = currentApprover;
    }

    public Date getStartDateUTC() {
        return startDateUTC;
    }

    public void setStartDateUTC(Date startDateUTC) {
        this.startDateUTC = startDateUTC;
    }

    public Date getEndDateUTC() {
        return endDateUTC;
    }

    public void setEndDateUTC(Date endDateUTC) {
        this.endDateUTC = endDateUTC;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LeaveRequestDTO))
            return false;

        LeaveRequestDTO that = (LeaveRequestDTO) o;

        if (isAutoApprove() != that.isAutoApprove())
            return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null)
            return false;
        if (getEmployee() != null ? !getEmployee().equals(that.getEmployee()) : that.getEmployee() != null)
            return false;
        if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null)
            return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null)
            return false;
        if (getTakeLeaveBy() != null ? !getTakeLeaveBy().equals(that.getTakeLeaveBy()) : that.getTakeLeaveBy() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getApprover() != null ? !getApprover().equals(that.getApprover()) : that.getApprover() != null)
            return false;
        if (getAttachments() != null ? !getAttachments().equals(that.getAttachments()) : that.getAttachments() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(that.getCustomFields()) : that.getCustomFields() != null)
            return false;
        if (getState_records() != null ? !getState_records().equals(that.getState_records()) : that.getState_records() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null)
            return false;
        if (getUserAction() != null ? !getUserAction().equals(that.getUserAction()) : that.getUserAction() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getTakeLeaveBy() != null ? getTakeLeaveBy().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getApprover() != null ? getApprover().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (isAutoApprove() ? 1 : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getState_records() != null ? getState_records().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getUserAction() != null ? getUserAction().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LeaveRequestDTO{" +
                "id=" + id +
                ", employee=" + employee +
                ", reason=" + reason +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type='" + type + '\'' +
                ", takeLeaveBy='" + takeLeaveBy + '\'' +
                ", description='" + description + '\'' +
                ", approver=" + approver +
                ", attachments=" + attachments +
                ", isAutoApprove=" + isAutoApprove +
                ", status=" + status +
                ", state_records=" + state_records +
                ", userAction=" + userAction +
                ", customFields=" + customFields +
                '}';
    }
}
