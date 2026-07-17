package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Nov 2, 2010
 * Time: 8:38:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class UsersCalendarSettingsItem implements IsSerializable {

    private Integer userId;
    private Boolean eventIsChecked;
    private Boolean callIsChecked;
    private Boolean projectIsChecked;
    private Boolean taskIsChecked;
    private Boolean issueIsChecked;
    private Boolean paIsChecked;
    private Boolean leaveRequestIsChecked;
    private Boolean holidayIsChecked;
    private Boolean courseIsChecked;
    private Integer defaultView = 2;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean isEventIsChecked() {
        return eventIsChecked != null ? eventIsChecked : false;
    }

    public void setEventIsChecked(Boolean eventIsChecked) {
        this.eventIsChecked = eventIsChecked;
    }

    public Boolean isCallIsChecked() {
        return callIsChecked != null ? callIsChecked : false;
    }

    public void setCallIsChecked(Boolean callIsChecked) {
        this.callIsChecked = callIsChecked;
    }

    public Boolean isProjectIsChecked() {
        return projectIsChecked != null ? projectIsChecked : false;
    }

    public void setProjectIsChecked(Boolean projectIsChecked) {
        this.projectIsChecked = projectIsChecked;
    }

    public Boolean isTaskIsChecked() {
        return taskIsChecked != null ? taskIsChecked : false;
    }

    public void setTaskIsChecked(Boolean taskIsChecked) {
        this.taskIsChecked = taskIsChecked;
    }

    public Boolean isIssueIsChecked() {
        return issueIsChecked != null ? issueIsChecked : false;
    }

    public void setIssueIsChecked(Boolean issueIsChecked) {
        this.issueIsChecked = issueIsChecked;
    }

    public Boolean isPaIsChecked() {
        return paIsChecked != null ? paIsChecked : false;
    }

    public void setPaIsChecked(Boolean paIsChecked) {
        this.paIsChecked = paIsChecked;
    }

    public Boolean isLeaveRequestIsChecked() {
        return leaveRequestIsChecked ? leaveRequestIsChecked : false;
    }

    public void setLeaveRequestIsChecked(Boolean leaveRequestIsChecked) {
        this.leaveRequestIsChecked = leaveRequestIsChecked;
    }

    public Boolean isHolidayIsChecked() {
        return holidayIsChecked ? holidayIsChecked : false;
    }

    public void setHolidayIsChecked(Boolean holidayIsChecked) {
        this.holidayIsChecked = holidayIsChecked;
    }

    public Integer getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(Integer defaultView) {
        this.defaultView = defaultView;
    }

    public Boolean isCourseIsChecked() {
        return courseIsChecked ? courseIsChecked : false;
    }

    public void setCourseIsChecked(Boolean courseIsChecked) {
        this.courseIsChecked = courseIsChecked;
    }
}
