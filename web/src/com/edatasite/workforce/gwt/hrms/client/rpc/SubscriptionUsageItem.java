package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionUsageItem implements IsSerializable {
    private Integer id;
    private SelectItem employee;
    private SelectItem vendor;
    private SelectItem contact;
    private DateNonConvertable date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getVendor() {
        return vendor;
    }

    public void setVendor(SelectItem vendor) {
        this.vendor = vendor;
    }

    public SelectItem getContact() {
        return contact;
    }

    public void setContact(SelectItem contact) {
        this.contact = contact;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }
}
