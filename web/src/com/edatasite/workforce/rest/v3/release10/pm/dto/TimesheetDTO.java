package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Akhror
 * Date: 26.07.2021
 */
public class TimesheetDTO implements Serializable {
    @NotNull(message = "Task is required")
    private ItemDto task;
    private ItemDto employee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;
    @NotNull(message = "Minutes is required")
    private Integer minutes;
    private String comment;
    private String status;

    public TimesheetDTO() {
    }

    public TimesheetDTO(ItemDto task, ItemDto employee, Date date, int minutes, String comment) {
        this.task = task;
        this.employee = employee;
        this.date = date;
        this.minutes = minutes;
        this.comment = comment;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimesheetDTO)) return false;

        TimesheetDTO that = (TimesheetDTO) o;

        if (getMinutes() != that.getMinutes()) return false;
        if (getTask() != null ? !getTask().equals(that.getTask()) : that.getTask() != null) return false;
        if (getEmployee() != null ? !getEmployee().equals(that.getEmployee()) : that.getEmployee() != null)
            return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        if (getComment() != null ? !getComment().equals(that.getComment()) : that.getComment() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getTask() != null ? getTask().hashCode() : 0;
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + getMinutes();
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        return result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TimesheetDTO{" +
                "task=" + task +
                ", employee=" + employee +
                ", date=" + date +
                ", minutes=" + minutes +
                ", comment='" + comment + '\'' +
                '}';
    }
}
