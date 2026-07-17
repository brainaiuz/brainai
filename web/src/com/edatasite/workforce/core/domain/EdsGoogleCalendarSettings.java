package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Nov 2, 2010
 * Time: 7:58:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googlecalendarsettings")
public class EdsGoogleCalendarSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser calendarOwner;

    @Column(name = "eventIsChecked")
    private Boolean eventIsChecked;

    @Column(name = "callIsChecked")
    private Boolean callIsChecked;

    @Column(name = "projectIsChecked")
    private Boolean projectIsChecked;

    @Column(name = "taskIsChecked")
    private Boolean taskIsChecked;

    @Column(name = "issueIsChecked")
    private Boolean issueIsChecked;

    @Column(name = "paIsChecked")
    private Boolean paIsChecked;

    @Column(name = "leaveRequesttIsChecked")
    private Boolean leaveRequestIsChecked;

    @Column(name = "holidayIsChecked")
    private Boolean holidayIsChecked;

    @Column(name = "courseIsChecked")
    private Boolean courseIsChecked;

    @Column(name = "defaultView")
    private Integer defaultView;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsUser getCalendarOwner() {
        return calendarOwner;
    }

    public void setCalendarOwner(EdsUser calendarOwner) {
        this.calendarOwner = calendarOwner;
    }

    public Boolean isEventIsChecked() {
        return eventIsChecked;
    }

    public void setEventIsChecked(Boolean eventIsChecked) {
        this.eventIsChecked = eventIsChecked;
    }

    public Boolean isCallIsChecked() {
        return callIsChecked;
    }

    public void setCallIsChecked(Boolean callIsChecked) {
        this.callIsChecked = callIsChecked;
    }



    public Boolean isProjectIsChecked() {
        return projectIsChecked;
    }

    public void setProjectIsChecked(Boolean projectIsChecked) {
        this.projectIsChecked = projectIsChecked;
    }

    public Boolean isTaskIsChecked() {
        return taskIsChecked;
    }

    public void setTaskIsChecked(Boolean taskIsChecked) {
        this.taskIsChecked = taskIsChecked;
    }

    public Boolean isIssueIsChecked() {
        return issueIsChecked;
    }

    public void setIssueIsChecked(Boolean issueIsChecked) {
        this.issueIsChecked = issueIsChecked;
    }

    public Boolean isPaIsChecked() {
        return paIsChecked;
    }

    public void setPaIsChecked(Boolean paIsChecked) {
        this.paIsChecked = paIsChecked;
    }

    public Boolean isLeaveRequestIsChecked() {
        return leaveRequestIsChecked;
    }

    public void setLeaveRequestIsChecked(Boolean leaveRequestIsChecked) {
        this.leaveRequestIsChecked = leaveRequestIsChecked;
    }

    public Boolean isHolidayIsChecked() {
        return holidayIsChecked;
    }

    public void setHolidayIsChecked(Boolean hoidayIsChecked) {
        this.holidayIsChecked = hoidayIsChecked;
    }

    public Boolean isCourseIsChecked() {
        return courseIsChecked;
    }

    public void setCourseIsChecked(Boolean courseIsChecked) {
        this.courseIsChecked = courseIsChecked;
    }

    public Integer getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(Integer defaultView) {
        this.defaultView = defaultView;
    }
}
