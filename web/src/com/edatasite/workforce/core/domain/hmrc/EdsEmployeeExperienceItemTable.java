package com.edatasite.workforce.core.domain.hmrc;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsReference;

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
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeExperienceItemTable")
public class EdsEmployeeExperienceItemTable extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "hireDate")
    private Date hireDate;

    @Column(name = "resignDate")
    private Date resignDate;

    @Column(name = "industryId")
    private Integer industryId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "industryId", updatable = false, insertable = false)
    private EdsReference industry;

    @Column(name = "position")
    private String position;

    @Column(name = "positionId")
    private Integer positionId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "positionId", updatable = false, insertable = false)
    private EdsPosition pos;

    @Column(name = "department")
    private String department;

    @Column(name = "departmentId")
    private Integer departmentId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "departmentId", updatable = false, insertable = false)
    private EdsDepartment dep;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsEmployeeExperienceItemTableCF customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private EdsEmployee edsEmployee;

    private String organization;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getResignDate() {
        return resignDate;
    }

    public void setResignDate(Date resignDate) {
        this.resignDate = resignDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public EdsEmployeeExperienceItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsEmployeeExperienceItemTableCF customFields) {
        this.customFields = customFields;
    }

    public EdsReference getIndustry() {
        return industry;
    }

    public void setIndustry(EdsReference industry) {
        this.industry = industry;
    }

    public EdsEmployee getEdsEmployee() {
        return edsEmployee;
    }

    public void setEdsEmployee(EdsEmployee edsEmployee) {
        this.edsEmployee = edsEmployee;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public EdsPosition getPos() {
        return pos;
    }

    public void setPos(EdsPosition pos) {
        this.pos = pos;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public EdsDepartment getDep() {
        return dep;
    }

    public void setDep(EdsDepartment dep) {
        this.dep = dep;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
