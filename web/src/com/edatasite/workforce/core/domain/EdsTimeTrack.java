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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 28.03.2001
 * Time: 1:10:12
 * Software Team
 */


//@Indexed
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timetrack")
public class EdsTimeTrack extends EdsObject implements StaffInOut {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsUser employee;

    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    @Column(name = "fingerPrintUser")
    private Boolean isFingerPrintUser;

    @Column(name = "inMethod")
    private String inMethod;
    private String location;
    private Integer shiftId;
    private Integer timeSlotId;

    public EdsTimeTrack() {

    }

    public EdsTimeTrack(EdsEmployee employee, EdsReference status) {
        this.employee = employee;
        this.status = status;
        this.startDate = new Date();
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
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

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public boolean isFingerPrintUser() {
        return Boolean.TRUE.equals(isFingerPrintUser);
    }

    public void setFingerPrintUser(Boolean fingerPrintUser) {
        isFingerPrintUser = fingerPrintUser;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInMethod() {
        return inMethod;
    }

    public void setInMethod(String inMethod) {
        this.inMethod = inMethod;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}

