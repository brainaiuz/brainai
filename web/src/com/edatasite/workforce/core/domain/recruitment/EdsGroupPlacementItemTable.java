package com.edatasite.workforce.core.domain.recruitment;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;

import javax.persistence.CascadeType;
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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "group_placement_item_table")
public class EdsGroupPlacementItemTable extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "empId")
    private Integer empId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "empId", updatable = false, insertable = false)
    private EdsEmployee employee;

    @Column(name = "candidateId")
    private Integer candidateId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateId", updatable = false, insertable = false)
    private EdsCrmContact candidate;

    @Column(name = "depId")
    private Integer curDepId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "depId", updatable = false, insertable = false)
    private EdsDepartment department;

    @Column(name = "posId")
    private Integer posId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "posId", updatable = false, insertable = false)
    private EdsPosition position;

    @Column(name = "vacancyId")
    private Integer vacancyId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancyId", updatable = false, insertable = false)
    private EdsVacancy vacancy;

    @Column(name = "locationId")
    private Integer locationId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", updatable = false, insertable = false)
    private EdsLocation location;

    @Column(name = "effective_date")
    private Date effectiveDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_placement_id")
    private EdsGroupPlacement edsGroupPlacement;

    private Integer type;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customfieldsid")
//    private EdsRotationItemTableCF customFields;


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

    public Integer getCurDepId() {
        return curDepId;
    }

    public void setCurDepId(Integer curDepId) {
        this.curDepId = curDepId;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public Integer getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Integer vacancyId) {
        this.vacancyId = vacancyId;
    }

    public EdsVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(EdsVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public EdsGroupPlacement getEdsGroupPlacement() {
        return edsGroupPlacement;
    }

    public void setEdsGroupPlacement(EdsGroupPlacement edsGroupPlacement) {
        this.edsGroupPlacement = edsGroupPlacement;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        this.candidate = candidate;
    }
}
