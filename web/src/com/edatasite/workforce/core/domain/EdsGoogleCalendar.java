package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 16:25:53
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googlecalendar")
public class EdsGoogleCalendar extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "googleid")
    @Type(type = "text")
    private String googleID;

    @Column(name = "token")
    @Type(type = "text")
    private String token;

    @Column(name = "refreshtoken")
    @Type(type = "text")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "calendarid")
    @Type(type = "text")
    private String calendarID;

    @Column(name = "task_calendar_id")
    private String taskCalendarID;

    private Integer attempts = 0;

    private Boolean active = true;

    @Column(name = "reason", length = 1000)
    private String reason;

    private Boolean officeCalendar = false;

    @Column(name = "officeUserId")
    @Type(type = "text")
    private String officeUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officeCalendarTimezoneId")
    @ForeignKey(name = "none")
    private EdsTimeZone calendarTimeZone;

    public Integer getObjectID() {
        return objectID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(String calendarID) {
        this.calendarID = calendarID;
    }

    public String getTaskCalendarID() {
        return taskCalendarID;
    }

    public void setTaskCalendarID(String taskCalendarID) {
        this.taskCalendarID = taskCalendarID;
    }

    public Integer getAttempts() {
        if(attempts == null) {
            setAttempts(0);
        }
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getActive() {
        return active == null || active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Boolean getOfficeCalendar() {
        return officeCalendar != null ? officeCalendar : false;
    }

    public void setOfficeCalendar(Boolean officeCalendar) {
        this.officeCalendar = officeCalendar;
    }

    public String getOfficeUserId() {
        return officeUserId;
    }

    public void setOfficeUserId(String officeUserId) {
        this.officeUserId = officeUserId;
    }

    public EdsTimeZone getCalendarTimeZone() {
        return calendarTimeZone;
    }

    public void setCalendarTimeZone(EdsTimeZone calendarTimeZone) {
        this.calendarTimeZone = calendarTimeZone;
    }
}
