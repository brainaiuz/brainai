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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "attendance_terminal")
public class EdsAttendanceTerminal extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "locationid")
    private Integer locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid", insertable = false, updatable = false)
    private EdsLocation location;

    @Column(name = "companyuniqueid")
    private String companyUniqueID;

    @Column(name = "companybranchname")
    private String companyBranchName;

    @Column(name = "dynamicstatus")
    private Boolean dynamicStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "eid")
    private Long externalId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public String getCompanyUniqueID() {
        return companyUniqueID;
    }

    public void setCompanyUniqueID(String companyUniqueID) {
        this.companyUniqueID = companyUniqueID;
    }

    public String getCompanyBranchName() {
        return companyBranchName;
    }

    public void setCompanyBranchName(String companyBranchName) {
        this.companyBranchName = companyBranchName;
    }

    public Boolean getDynamicStatus() {
        return dynamicStatus;
    }

    public void setDynamicStatus(Boolean dynamicStatus) {
        this.dynamicStatus = dynamicStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
}
