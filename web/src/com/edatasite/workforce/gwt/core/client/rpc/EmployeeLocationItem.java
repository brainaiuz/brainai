package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EmployeeLocationItem implements IsSerializable {
    private Integer id;
    private SelectItem location;
    private SelectItem employee;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }
}
