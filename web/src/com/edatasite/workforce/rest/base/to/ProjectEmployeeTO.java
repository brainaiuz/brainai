package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov on 3/30/15.
 */
public class ProjectEmployeeTO implements IsSerializable {

    SelectItemTO employee;
    SelectItemTO position;
    SelectItemTO department;
    Integer timeSpent;
    Integer actualTime;
    Float completedPercent;
    Integer estimatedTime;


    public ProjectEmployeeTO() {
    }

    public ProjectEmployeeTO(PositionsSelectItem item) {
        this.employee = new SelectItemTO(item.getEmployeeId(), item.getName(), item.getEmployeeNumber(), "");
        this.position = new SelectItemTO(item.getPositionId(), item.getPositionName());
        this.department = new SelectItemTO(item.getDepartmentId(), item.getDepartmentName());
        this.timeSpent = item.getTimeSpent();
        this.actualTime = item.getActualTime();
        this.completedPercent = item.getPercent();
        this.estimatedTime = item.getTime();
    }

    public SelectItemTO getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItemTO employee) {
        this.employee = employee;
    }

    public SelectItemTO getPosition() {
        return position;
    }

    public void setPosition(SelectItemTO position) {
        this.position = position;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Float getCompletedPercent() {
        return completedPercent;
    }

    public void setCompletedPercent(Float completedPercent) {
        this.completedPercent = completedPercent;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
