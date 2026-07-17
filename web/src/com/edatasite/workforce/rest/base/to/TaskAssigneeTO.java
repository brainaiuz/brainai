package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek on 21.02.2015.
 */
public class TaskAssigneeTO implements IsSerializable {

    Integer id;
    Integer estimatedTime;
    Integer actualTime;
    Double percentCompleted;
    Double wageRate;

    EmployeeTO employee;
    SelectItemTO status;

    public TaskAssigneeTO() {
    }

    public TaskAssigneeTO(KpiTreeInfo item) {
        this.id = item.getId();
        this.employee = new EmployeeTO(item);
        this.estimatedTime = item.getTime();
        this.actualTime = item.getActualTime();
        this.percentCompleted = WrapUtils.getDouble(item.getPercent(), null);
        this.wageRate = item.getWageRate();
        if (item.getStatusId() != null) {
            this.status = new SelectItemTO(item.getStatusId());
        }
    }

    public TaskAssigneeTO(IdTime item) {
        this.id = item.getId();
        this.employee = new EmployeeTO();
        this.employee.setName(item.getEmployeeName());
        this.estimatedTime = item.getTime();
        this.actualTime = item.getActualTime();
        this.percentCompleted = WrapUtils.getDouble(item.getPercent(), null);
        if (item.getStatusId() != null) {
            this.status = new SelectItemTO(item.getStatusId());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(Double percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public EmployeeTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeTO employee) {
        this.employee = employee;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }
}
