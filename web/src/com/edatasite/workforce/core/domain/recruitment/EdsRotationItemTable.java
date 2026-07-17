package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rotationItemTable")
public class EdsRotationItemTable extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "name", length = 1000)
    @Type(type = "text")
    private String name;

    @Column(name = "empId")
    private Integer empId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "empId", updatable = false, insertable = false)
    private EdsEmployee employee;

    @Column(name = "curLocId")
    private Integer curLocId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "curLocId", updatable = false, insertable = false)
    private EdsLocation currentLocation;

    @Column(name = "curDepId")
    private Integer curDepId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "curDepId", updatable = false, insertable = false)
    private EdsDepartment currentDepartment;

    @Column(name = "curPosId")
    private Integer curPosId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "curPosId", updatable = false, insertable = false)
    private EdsPosition currentPosition;

    @Column(name = "newLocId")
    private Integer newLocId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "newLocId", updatable = false, insertable = false)
    private EdsLocation newLocation;

    @Column(name = "newDepID")
    private Integer newDepID;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "newDepID", updatable = false, insertable = false)
    private EdsDepartment newDepartment;

    @Column(name = "newPosId")
    private Integer newPosId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "newPosId", updatable = false, insertable = false)
    private EdsPosition newPosition;

    @Type(type = "text")
    private String description;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rotation_id")
    private EdsRotation edsRotation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsRotationItemTableCF customFields;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EdsRotation getEdsRotation() {
        return edsRotation;
    }

    public void setEdsRotation(EdsRotation edsRotation) {
        this.edsRotation = edsRotation;
    }

    public EdsRotationItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsRotationItemTableCF customFields) {
        this.customFields = customFields;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Integer getCurLocId() {
        return curLocId;
    }

    public void setCurLocId(Integer curLocId) {
        this.curLocId = curLocId;
    }

    public EdsLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(EdsLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getCurDepId() {
        return curDepId;
    }

    public void setCurDepId(Integer curDepId) {
        this.curDepId = curDepId;
    }

    public EdsDepartment getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(EdsDepartment currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public Integer getCurPosId() {
        return curPosId;
    }

    public void setCurPosId(Integer curPosId) {
        this.curPosId = curPosId;
    }

    public EdsPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(EdsPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Integer getNewLocId() {
        return newLocId;
    }

    public void setNewLocId(Integer newLocId) {
        this.newLocId = newLocId;
    }

    public EdsLocation getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(EdsLocation newLocation) {
        this.newLocation = newLocation;
    }

    public Integer getNewDepID() {
        return newDepID;
    }

    public void setNewDepID(Integer newDepID) {
        this.newDepID = newDepID;
    }

    public EdsDepartment getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(EdsDepartment newDepartment) {
        this.newDepartment = newDepartment;
    }

    public Integer getNewPosId() {
        return newPosId;
    }

    public void setNewPosId(Integer newPosId) {
        this.newPosId = newPosId;
    }

    public EdsPosition getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(EdsPosition newPosition) {
        this.newPosition = newPosition;
    }
}
