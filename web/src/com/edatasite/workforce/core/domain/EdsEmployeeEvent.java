package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Oct 20, 2009
 * Time: 4:13:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeevent")
public class EdsEmployeeEvent extends EdsObject implements CalendarObject {
    public static final String OWNER = "OWNER"; // event owner

    public static final String KNOW = "KNOW"; // can see event date and time
    public static final String READ = "READ"; // can see all event data
    public static final String READ_WRITE = "READ_WRITE"; // can edit event data

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsUser employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EdsEvent event;

    @Column(name = "edit")
    private Boolean edit;

    @Column(name = "shared")
    private Boolean shared;//If shared = false and edit = true, it means that the employee is the owner of this event.

    /**
     * This field is used in Calendar Sync, will serve as last synced date for that particular user
     *
     * @see com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceImpl#synchronizeEvents(Integer, java.util.Date, java.util.Date)
     */
    @Column(name = "lastModifiedDate")
    private Date lastModifiedDate;

    @Column(name = "googleId", length = 500)
    private String googleId;

    @Column(name = "officeid")
    @Type(type = "text")
    private String officeID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "permission")
    private String permission;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsUser getEmployee() {
        return employee;
    }

    public void setEmployee(EdsUser employee) {
        this.employee = employee;
    }

    public EdsEvent getEvent() {
        return event;
    }

    public void setEvent(EdsEvent event) {
        this.event = event;
    }

    public Date getLastModifiedDate() {
        if (lastModifiedDate == null) return new Date();
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getGoogleID() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean canKnow() {
        return KNOW.equalsIgnoreCase(this.permission) || this.canRead();
    }

    public boolean canRead() {
        return READ.equalsIgnoreCase(this.permission) || this.canWrite();
    }

    public boolean canWrite() {
        return READ_WRITE.equalsIgnoreCase(this.permission) || this.isOwner();
    }

    public boolean isOwner() {
        return OWNER.equalsIgnoreCase(this.permission);
    }

    public boolean isShared() {
        return !this.isOwner() && this.canKnow();
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean isEdit() {
        return edit == null ? false : edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }
}
