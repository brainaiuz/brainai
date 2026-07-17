package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;

public class OvertimeObjectData implements Serializable {
    private Integer id;
    private SelectItem employee;
    private SelectItem category;
    private BigDecimal overtimeHours;
    private DateNonConvertable date;
    private OvertimeObject overtimeObject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getCategory() {
        return category;
    }

    public void setCategory(SelectItem category) {
        this.category = category;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public OvertimeObject getOvertimeObject() {
        return overtimeObject;
    }

    public void setOvertimeObject(OvertimeObject overtimeObject) {
        this.overtimeObject = overtimeObject;
    }
}
