package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "brigada_employees")
public class EdsBrigadaEmployee extends EdsObject {

    public EdsBrigadaEmployee(EdsEmployeeDepartment employeeDepartment, EdsBrigada project) {
        this.employeeDepartment = employeeDepartment;
        this.project = project;
    }

    public EdsBrigadaEmployee() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsBrigada project;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeDepartmentId")
    private EdsEmployeeDepartment employeeDepartment;

    private String code;

    @Transient
    private Set<EdsJobFunction> appliedJobFunctions = new HashSet<>();
    private Date startDate;
    private Date endDate;

    @Column(insertable = false, updatable = false)
    private Date creationdate;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @Transient
    private String deletedby;


    private transient boolean propertiesChanged = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionid")
    private EdsPosition position;

    private Date contractStartDate;
    private Date contractEndDate;

    private String unit;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBrigada getProject() {
        return project;
    }

    public void setProject(EdsBrigada project) {
        this.project = project;
    }

    public EdsEmployeeDepartment getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(EdsEmployeeDepartment employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public Set<EdsJobFunction> getAppliedJobFunctions() {
        return appliedJobFunctions;
    }

    public void setAppliedJobFunctions(Set<EdsJobFunction> appliedJobFunctions) {
        this.appliedJobFunctions = appliedJobFunctions;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(String deletedby) {
        this.deletedby = deletedby;
    }


    public boolean isPropertiesChanged() {
        return propertiesChanged;
    }

    public void setPropertiesChanged(boolean propertiesChanged) {
        this.propertiesChanged = propertiesChanged;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
