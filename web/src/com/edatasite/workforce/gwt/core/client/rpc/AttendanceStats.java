package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Farhod Otaboev
 * Date: 04.06.12
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceStats implements IsSerializable {
    private boolean holiday;
    private boolean dayOff;
    private Date taskStart;
    private Date taskEnd;
    private Integer timeslotMinutes;
    private Integer timesheetMinutes;
    private Integer leaveMinutes;

    public AttendanceStats() {

    }

    public boolean isHoliday() {
        return this.holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Date getTaskStart() {
        return this.taskStart;
    }

    public void setTaskStart(Date taskStart) {
        this.taskStart = taskStart;
    }

    public Date getTaskEnd() {
        return this.taskEnd;
    }

    public void setTaskEnd(Date taskEnd) {
        this.taskEnd = taskEnd;
    }

    public Integer getTimeslotMinutes() {
        return this.timeslotMinutes;
    }

    public void setTimeslotMinutes(Integer timeslotMinutes) {
        this.timeslotMinutes = timeslotMinutes;
    }

    public Integer getTimesheetMinutes() {
        return this.timesheetMinutes;
    }

    public void setTimesheetMinutes(Integer timesheetMinutes) {
        this.timesheetMinutes = timesheetMinutes;
    }

    public Integer getLeaveMinutes() {
        return this.leaveMinutes;
    }

    public void setLeaveMinutes(Integer leaveMinutes) {
        this.leaveMinutes = leaveMinutes;
    }
}
