package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: Dilshod Madrahimov
 * Date: 02/03/12
 * Time: 11:58
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeetimetrack",
        indexes = {
                @Index(columnList = "employeeId", name = "employeetimetrack_employeeId_idx")
        })
public class EdsEmployeeTimeTrack extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer employeeId;
    private String location;

    @Temporal(TemporalType.DATE)
    private Date date;
    private Date checkIn;
    private Date onBreak;
    private Date onDuty;
    private Date checkOut;
    private Date creationDate;

    public EdsEmployeeTimeTrack() {
    }

    public EdsEmployeeTimeTrack(Integer objectID, Integer employeeId, String location, Date date, Date checkIn, Date onBreak, Date onDuty, Date checkOut, Date creationDate) {
        this.objectID = objectID;
        this.employeeId = employeeId;
        this.location = location;
        this.date = date;
        this.checkIn = checkIn;
        this.onBreak = onBreak;
        this.onDuty = onDuty;
        this.checkOut = checkOut;
        this.creationDate = creationDate;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getOnBreak() {
        return onBreak;
    }

    public void setOnBreak(Date onBreak) {
        this.onBreak = onBreak;
    }

    public Date getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(Date onDuty) {
        this.onDuty = onDuty;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsEmployeeTimeTrack)) return false;
        if (!super.equals(o)) return false;

        EdsEmployeeTimeTrack that = (EdsEmployeeTimeTrack) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getEmployeeId() != null ? !getEmployeeId().equals(that.getEmployeeId()) : that.getEmployeeId() != null)
            return false;
        if (getLocation() != null ? !getLocation().equals(that.getLocation()) : that.getLocation() != null)
            return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        if (getCheckIn() != null ? !getCheckIn().equals(that.getCheckIn()) : that.getCheckIn() != null) return false;
        if (getOnBreak() != null ? !getOnBreak().equals(that.getOnBreak()) : that.getOnBreak() != null) return false;
        if (getOnDuty() != null ? !getOnDuty().equals(that.getOnDuty()) : that.getOnDuty() != null) return false;
        if (getCheckOut() != null ? !getCheckOut().equals(that.getCheckOut()) : that.getCheckOut() != null)
            return false;
        if (getCreationDate() != null ? !getCreationDate().equals(that.getCreationDate()) : that.getCreationDate() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getEmployeeId() != null ? getEmployeeId().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getCheckIn() != null ? getCheckIn().hashCode() : 0);
        result = 31 * result + (getOnBreak() != null ? getOnBreak().hashCode() : 0);
        result = 31 * result + (getOnDuty() != null ? getOnDuty().hashCode() : 0);
        result = 31 * result + (getCheckOut() != null ? getCheckOut().hashCode() : 0);
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
}
