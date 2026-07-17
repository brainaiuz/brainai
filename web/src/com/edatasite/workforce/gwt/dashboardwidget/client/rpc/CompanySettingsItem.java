package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CompanySettingsItem implements IsSerializable {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private Address address;
    private boolean isSetup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isSetup() {
        return isSetup;
    }

    public void setSetup(boolean setup) {
        isSetup = setup;
    }
}
