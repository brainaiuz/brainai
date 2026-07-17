package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */
public class TimesheetAddTO extends ResponseData {
    private Integer employee_id;
    private Integer task_id;
    private Integer hours;
    private Integer minutes;
    private Float completed;
    private String comment;

    public TimesheetAddTO() {
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Float getCompleted() {
        return completed;
    }

    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
