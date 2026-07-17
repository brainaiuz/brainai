package com.edatasite.workforce.core.domain.rbac.history;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.rbac.EdsBaseRbac;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: Feb 22, 2010
 * Time: 5:50:44 PM
 */
@MappedSuperclass
public class EdsBaseTaskRbac extends EdsBaseRbac {

    @ManyToOne
    @JoinColumn(name = "taskID")
    private EdsTask task;

    ///// Task Specific fields

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    private Float percent;

    private Integer estimatedTime;

    private Integer actualTime;

    private Date actualStartDate;

    private Date actualEndDate;

    private Integer timeSpent;

    private Double plannedWageAmount = 0.0;

    private Double plannedClientChargeAmount = 0.0;

    private Double actualWageAmmount = 0.0;

    private Double actualClientChargeAmmount = 0.0;

    /**
     * Employee Planned time/(task start and end date days division)
     */
    private Integer dailyLoad;


    public void updateActualWageAmmount(double diff) {
        setActualWageAmmount(getActualWageAmmount() + diff);
    }

    public void updateActualClientChargeAmmount(double diff) {
        setActualClientChargeAmmount(getActualClientChargeAmmount() + diff);
    }

    public void updatePlannedWageAmount(double diff) {
        setPlannedWageAmount(getPlannedWageAmount() + diff);
    }

    public void updatePlannedClientChargeAmount(double diff) {
        setPlannedClientChargeAmount(getPlannedClientChargeAmount() + diff);
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Double getPlannedWageAmount() {
        return plannedWageAmount;
    }

    public void setPlannedWageAmount(Double plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public Double getPlannedClientChargeAmount() {
        return plannedClientChargeAmount;
    }

    public void setPlannedClientChargeAmount(Double plannedClientChargeAmount) {
        this.plannedClientChargeAmount = plannedClientChargeAmount;
    }

    public Double getActualWageAmmount() {
        return actualWageAmmount;
    }

    public void setActualWageAmmount(Double actualWageAmmount) {
        this.actualWageAmmount = actualWageAmmount;
    }

    public Double getActualClientChargeAmmount() {
        return actualClientChargeAmmount;
    }

    public void setActualClientChargeAmmount(Double actualClientChargeAmmount) {
        this.actualClientChargeAmmount = actualClientChargeAmmount;
    }

    public Integer getDailyLoad() {
        return dailyLoad;
    }

    public void setDailyLoad(Integer dailyLoad) {
        this.dailyLoad = dailyLoad;
    }
}
