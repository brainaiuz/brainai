package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: maverick
 * Date: 3/27/11
 * Time: 5:46 PM
 */
public class ProjectEmployeeWageClientHistoryItem implements IsSerializable {

    private Integer objectId;
    private Double wageRate;
    private Double clientChargeRate;
    private Float workloadPercentage;
    private DateNonConvertable changeDate;
    private boolean isCurrent;

    public ProjectEmployeeWageClientHistoryItem() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
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

    public DateNonConvertable getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(DateNonConvertable changeDate) {
        this.changeDate = changeDate;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}