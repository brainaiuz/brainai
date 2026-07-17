package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Ilhom Lutfullaev
 * Date: 04.12.2009
 * Time: 16:16:25
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "incumbentshistory")
public class EdsIncumbentsHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "positionid")
    private Integer positionId;

    @Column(name = "employeeid")
    private Integer employeeId;

    @Column(name = "employeename")
    private String employeeName;

    @Column(name = "fullparttime")
    private Integer fullPartTime;

    @Column(name = "startdate")
    private Date startDate;

    @Column(name = "enddate")
    private Date endDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullPartTime) {
        this.fullPartTime = fullPartTime;
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
}
