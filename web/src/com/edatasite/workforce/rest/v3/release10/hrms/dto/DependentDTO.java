package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class DependentDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String relationShip;
    private String city;
    private SelectItem country;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String frstNAme) {
        this.firstName = frstNAme;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }
}
