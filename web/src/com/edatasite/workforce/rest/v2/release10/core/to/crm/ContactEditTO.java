package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class ContactEditTO extends ResponseData {

    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private Integer company;
    private Integer supervisor;

    public ContactEditTO() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Integer getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Integer supervisor) {
        this.supervisor = supervisor;
    }
}
