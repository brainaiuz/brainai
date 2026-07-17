package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.util.Date;

/**
 * User: Akhror
 * Date: 29.07.2021
 */
public class TimesheetFilterDTO {
    private ItemDto task;
    private ItemDto employee;
    private Date startDate;
    private Date endDate;
    private ItemDto project;

    public TimesheetFilterDTO() {
    }

    public TimesheetFilterDTO(ItemDto task, ItemDto employee, Date startDate, Date endDate) {
        this.task = task;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ItemDto getTask() {
        return task;
    }

    public void setTask(ItemDto task) {
        this.task = task;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
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

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimesheetFilterDTO)) return false;

        TimesheetFilterDTO that = (TimesheetFilterDTO) o;

        if (getTask() != null ? !getTask().equals(that.getTask()) : that.getTask() != null) return false;
        if (getEmployee() != null ? !getEmployee().equals(that.getEmployee()) : that.getEmployee() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null) return false;
        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getTask() != null ? getTask().hashCode() : 0;
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimesheetFilterDTO{" +
                "task=" + task +
                ", employee=" + employee +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", project=" + project +
                '}';
    }
}
