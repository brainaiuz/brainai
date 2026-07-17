package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

public class SalaryHistory implements IsSerializable {

    private Integer id;

    private Integer employeeId;
    private BigDecimal salary;
    private DateNonConvertable effectiveDate;

    private Integer relationId;
    private String relationType;

    public SalaryHistory() {
    }

    public SalaryHistory(Integer employeeId, BigDecimal salary, Date effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = new DateNonConvertable(effectiveDate);
    }

    public SalaryHistory(Integer id, Integer employeeId, BigDecimal salary, Date effectiveDate, Integer relationId, String relationType) {
        this.id = id;
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = new DateNonConvertable(effectiveDate);
        this.relationId = relationId;
        this.relationType = relationType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public DateNonConvertable getEffectiveDate() {
        return effectiveDate;
    }

    public Date getNonConvertableEffectiveDate() {
        return effectiveDate != null ? effectiveDate.getDate() : null;
    }

    public void setEffectiveDate(DateNonConvertable effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
