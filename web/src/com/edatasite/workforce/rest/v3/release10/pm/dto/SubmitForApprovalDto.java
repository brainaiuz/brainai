package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.util.Date;
import java.util.List;

public class SubmitForApprovalDto {
    private ItemDto project;
    private ItemDto employee;
    private Boolean selectAll;
    private List<Integer> approvedTimesheetIds;
    private String action;
    private Date startDate;
    private Date endDate;

    public SubmitForApprovalDto() {
    }

    public Boolean getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<Integer> getApprovedTimesheetIds() {
        return approvedTimesheetIds;
    }

    public void setApprovedTimesheetIds(List<Integer> approvedTimesheetIds) {
        this.approvedTimesheetIds = approvedTimesheetIds;
    }

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
}
