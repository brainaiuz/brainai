package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User : Akhror on 11/03/2023
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "attendance_hour")
public class EdsAttendanceHour extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsUser employee;

    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private EdsShift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private EdsShiftSettings timeslot;

    @Enumerated(EnumType.STRING)
    private AttendanceHoursType type;

    public EdsAttendanceHour() {
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsUser getEmployee() {
        return employee;
    }

    public void setEmployee(EdsUser employee) {
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

    public EdsShift getShift() {
        return shift;
    }

    public void setShift(EdsShift shift) {
        this.shift = shift;
    }

    public EdsShiftSettings getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(EdsShiftSettings timeslot) {
        this.timeslot = timeslot;
    }

    public AttendanceHoursType getType() {
        return type;
    }

    public void setType(AttendanceHoursType type) {
        this.type = type;
    }
}
