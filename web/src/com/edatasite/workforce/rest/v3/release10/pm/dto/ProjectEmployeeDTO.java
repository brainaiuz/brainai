package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

/**
 * User: Akhror
 * Date: 07.07.2021
 */
public class ProjectEmployeeDTO {
    private ItemDto employee;
    private Double wageRate;
    private Double customerChargeRate;
    private Float workload;

    private IdName department;
    private IdName position;
    private Integer estimatedTime;
    private Integer timeSpent;
    private Integer actualTime;
    private Float completed;
    private Integer projectEmployeeId;

    public ProjectEmployeeDTO() {
    }

    public ProjectEmployeeDTO(ItemDto employee, Double wageRate, Double customerChargeRate, Float workload, IdName department, IdName position, Integer estimatedTime, Integer timeSpent, Integer actualTime, Float completed) {
        this.employee = employee;
        this.wageRate = wageRate;
        this.customerChargeRate = customerChargeRate;
        this.workload = workload;
        this.department = department;
        this.position = position;
        this.estimatedTime = estimatedTime;
        this.timeSpent = timeSpent;
        this.actualTime = actualTime;
        this.completed = completed;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getCustomerChargeRate() {
        return customerChargeRate;
    }

    public void setCustomerChargeRate(Double customerChargeRate) {
        this.customerChargeRate = customerChargeRate;
    }

    public Float getWorkload() {
        return workload;
    }

    public void setWorkload(Float workload) {
        this.workload = workload;
    }

    public IdName getDepartment() {
        return department;
    }

    public void setDepartment(IdName department) {
        this.department = department;
    }

    public IdName getPosition() {
        return position;
    }

    public void setPosition(IdName position) {
        this.position = position;
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

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Float getCompleted() {
        return completed;
    }

    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public Integer getProjectEmployeeId() {
        return projectEmployeeId;
    }

    public void setProjectEmployeeId(Integer projectEmployeeId) {
        this.projectEmployeeId = projectEmployeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectEmployeeDTO)) return false;

        ProjectEmployeeDTO that = (ProjectEmployeeDTO) o;

        if (getEmployee() != null ? !getEmployee().equals(that.getEmployee()) : that.getEmployee() != null)
            return false;
        if (getWageRate() != null ? !getWageRate().equals(that.getWageRate()) : that.getWageRate() != null)
            return false;
        if (getCustomerChargeRate() != null ? !getCustomerChargeRate().equals(that.getCustomerChargeRate()) : that.getCustomerChargeRate() != null)
            return false;
        if (getWorkload() != null ? !getWorkload().equals(that.getWorkload()) : that.getWorkload() != null)
            return false;
        if (getDepartment() != null ? !getDepartment().equals(that.getDepartment()) : that.getDepartment() != null)
            return false;
        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null)
            return false;
        if (getEstimatedTime() != null ? !getEstimatedTime().equals(that.getEstimatedTime()) : that.getEstimatedTime() != null)
            return false;
        if (getTimeSpent() != null ? !getTimeSpent().equals(that.getTimeSpent()) : that.getTimeSpent() != null)
            return false;
        if (getActualTime() != null ? !getActualTime().equals(that.getActualTime()) : that.getActualTime() != null)
            return false;
        if (getCompleted() != null ? !getCompleted().equals(that.getCompleted()) : that.getCompleted() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getEmployee() != null ? getEmployee().hashCode() : 0;
        result = 31 * result + (getWageRate() != null ? getWageRate().hashCode() : 0);
        result = 31 * result + (getCustomerChargeRate() != null ? getCustomerChargeRate().hashCode() : 0);
        result = 31 * result + (getWorkload() != null ? getWorkload().hashCode() : 0);
        result = 31 * result + (getDepartment() != null ? getDepartment().hashCode() : 0);
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        result = 31 * result + (getEstimatedTime() != null ? getEstimatedTime().hashCode() : 0);
        result = 31 * result + (getTimeSpent() != null ? getTimeSpent().hashCode() : 0);
        result = 31 * result + (getActualTime() != null ? getActualTime().hashCode() : 0);
        result = 31 * result + (getCompleted() != null ? getCompleted().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectEmployeeDTO{" +
                "employee=" + employee +
                ", wageRate=" + wageRate +
                ", customerChargeRate=" + customerChargeRate +
                ", workload=" + workload +
                ", department=" + department +
                ", position=" + position +
                ", estimatedTime=" + estimatedTime +
                ", timeSpent=" + timeSpent +
                ", actualTime=" + actualTime +
                ", completed=" + completed +
                '}';
    }
}
