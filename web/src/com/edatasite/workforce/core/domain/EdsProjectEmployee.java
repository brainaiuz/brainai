package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import javax.persistence.*;
import java.util.*;

/**
 * User: Slizer3D
 * Date: 23.03.2007
 * Time: 22:10:07
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectEmployee",
        indexes = {
                @Index(columnList = "projectid", name = "projectEmployee_projectid_idx"),
                @Index(columnList = "employeeDepartmentId", name = "projectEmployee_employeedepartmentid_idx")
        })
public class EdsProjectEmployee extends EdsObject {

    public EdsProjectEmployee() {
    }

    public EdsProjectEmployee(EdsEmployeeDepartment employeeDepartment, EdsProject project) {
        this.employeeDepartment = employeeDepartment;
        this.project = project;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeDepartmentId")
    private EdsEmployeeDepartment employeeDepartment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "projectemployeeId")
    @OrderBy("changeDate ASC")
    private List<EdsProjectEmployeeWageClientRateHistory> wageClientRatesHistory = new ArrayList<>();

    @Transient
    private Set<EdsJobFunction> appliedJobFunctions = new HashSet<>();
    private Date startDate;
    private Date endDate;

    @Column(insertable = false, updatable = false)
    private Date creationdate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectemployeeId")
    private Set<EdsEmployeeTask> employeeTasks = new HashSet<>();

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @Transient
    private String deletedby;

    @Column(name = "wageRate")
    private Double wageRate = 0d;

    @Column(name = "clientChargeRate")
    private Double clientChargeRate = 0d;

    @Column(name = "workloadPercentage")
    private Float workloadPercentage = 0f;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Column(name = "historical")
    private Boolean historical = false;
    /**
     * The subProjectEmployees in this projectEmployee. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicalParent", fetch = FetchType.LAZY)
    @OrderBy("objectID desc")
    private List<EdsProjectEmployee> subProjectEmployees = new ArrayList<>();
    /**
     * The parent ProjectEmployee of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsProjectEmployee historicalParent;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    private transient boolean propertiesChanged = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionid")
    private EdsPosition position;

    private Date contractStartDate;
    private Date contractEndDate;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<EdsProjectEmployeeWageClientRateHistory> getWageClientRatesHistory() {
        return wageClientRatesHistory;
    }

    public void setWageClientRatesHistory(List<EdsProjectEmployeeWageClientRateHistory> wageClientRatesHistory) {
        this.wageClientRatesHistory = wageClientRatesHistory;
    }

    public Double getWageRate() {
        if (wageRate == null) {
            wageRate = 0.0;
        }
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        Double oldWageRate = this.wageRate;
        this.wageRate = wageRate;
        if (!ServerUtils.equalsDouble(wageRate, oldWageRate)) {
            propertiesChanged = true;
        }
    }

    public Double getClientChargeRate() {
        if (clientChargeRate == null) {
            clientChargeRate = 0.0;
        }
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        Double oldClientChargeRate = this.clientChargeRate;
        this.clientChargeRate = clientChargeRate;
        if (!ServerUtils.equalsDouble(clientChargeRate, oldClientChargeRate)) {
            propertiesChanged = true;
        }
    }

    public Float getWorkloadPercentage() {
        if (workloadPercentage == null) {
            this.workloadPercentage = 0f;
        }
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Float workloadPercentage) {
        Float oldWorkloadPercentage = this.workloadPercentage;
        this.workloadPercentage = workloadPercentage;
        if (!ServerUtils.equalsFloat(workloadPercentage, oldWorkloadPercentage)) {
            propertiesChanged = true;
        }
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public List<EdsProjectEmployee> getSubProjectEmployees() {
        return subProjectEmployees;
    }

    public void setSubProjectEmployees(List<EdsProjectEmployee> subProjectEmployees) {
        this.subProjectEmployees = subProjectEmployees;
    }

    public EdsProjectEmployee getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsProjectEmployee historicalParent) {
        this.historicalParent = historicalParent;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isPropertiesChanged() {
        return propertiesChanged;
    }

    public void setPropertiesChanged(boolean propertiesChanged) {
        this.propertiesChanged = propertiesChanged;
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
        Date oldStartDate = this.startDate;
        this.startDate = startDate;
        if (!ServerUtils.equalsDate(startDate, oldStartDate)) {
            propertiesChanged = true;
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        Date oldEndDate = this.endDate;
        this.endDate = endDate;
        if (!ServerUtils.equalsDate(endDate, oldEndDate)) {
            propertiesChanged = true;
        }
    }

    public Set<EdsEmployeeTask> getEmployeeTasks() {
        return employeeTasks;
    }

    public void setEmployeeTasks(Set<EdsEmployeeTask> employeeTasks) {
        this.employeeTasks = employeeTasks;
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

    public EdsEmployeeDepartment getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(EdsEmployeeDepartment employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
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

    @Override
    public String toString() {
        if (employeeDepartment != null && employeeDepartment.getEmployee() != null && employeeDepartment.getEmployee().getName() != null) {
            return employeeDepartment.getEmployee().getName();
        }
        return "";
    }

    public Date getCreationdate() {
        return creationdate;
    }

}
