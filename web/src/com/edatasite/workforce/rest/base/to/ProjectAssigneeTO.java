package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class ProjectAssigneeTO implements IsSerializable {
    private Double wageRate;
    private Double clientChargeRate;
    private Double workloadPercentage;
    private UserTO employee;
    private Integer estimatedTime;
    private Integer timeSpent;

    public ProjectAssigneeTO() {
    }

    public ProjectAssigneeTO(KpiTreeInfo info) {
        this.wageRate = info.getWageRate();
        this.clientChargeRate = info.getClientChargeRate();
        this.workloadPercentage = WrapUtils.getDouble(info.getWorkloadPercentage());
        this.estimatedTime = info.getTime();
        this.timeSpent = info.getTimeSpent();
        this.employee = new UserTO(info);
        this.employee.setId(info.getId());
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

    public Double getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Double workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }

    public UserTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserTO employee) {
        this.employee = employee;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }
}
