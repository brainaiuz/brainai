package com.edatasite.workforce.core.domain;


import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: 27.05.2009
 * Time: 15:54:37
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ProjectEmployeeWageClientRateHistory")
public class EdsProjectEmployeeWageClientRateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectemployeeid")
    private EdsProjectEmployee projectEmployee;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    private Double wageRate;
    private Double clientChargeRate;
    private Date changeDate;

    @Column(name = "workloadPercentage")
    private Float workloadPercentage = 0f;

    public EdsProjectEmployee getProjectEmployee() {
        return projectEmployee;
    }

    public void setProjectEmployee(EdsProjectEmployee projectEmployee) {
        this.projectEmployee = projectEmployee;
    }

    public Double getWageRate() {
        if (wageRate == null) {
            wageRate = 0.0;
        }
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        if (clientChargeRate == null) {
            clientChargeRate = 0.0;
        }
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Float getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Float workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
}
