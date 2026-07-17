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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "backup_employees")
public class EdsBackupEmployee extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer parentId;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "duty_percentage")
    private BigDecimal dutyPercentage;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @ManyToOne()
    @JoinColumn(name = "sickrequest_id")
    private EdsSickRequest sickRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsBackupsEmployee backupsEmployees;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @Column(name = "recall_date")
    private Date recallDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getDutyPercentage() {
        return dutyPercentage;
    }

    public void setDutyPercentage(BigDecimal dutyPercentage) {
        this.dutyPercentage = dutyPercentage;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public void setEmployees(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsSickRequest getSickRequest() {
        return sickRequest;
    }

    public void setSickRequest(EdsSickRequest sickRequest) {
        this.sickRequest = sickRequest;
    }

    public EdsBackupsEmployee getBackupsEmployees() {
        return backupsEmployees;
    }

    public void setBackupsEmployees(EdsBackupsEmployee backupsEmployees) {
        this.backupsEmployees = backupsEmployees;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(Date recallDate) {
        this.recallDate = recallDate;
    }
}
