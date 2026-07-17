package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 6:43 PM
 */
public class CrmAccountInformationDTO extends ResponseData {
    private String name;
    private ArrayList<String> account_types;
    private String phone;
    private String email;
    private String website;
    private String fax;
    private String indsutry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAccount_types() {
        return account_types;
    }

    public void setAccount_types(ArrayList<String> account_types) {
        this.account_types = account_types;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getIndsutry() {
        return indsutry;
    }

    public void setIndsutry(String indsutry) {
        this.indsutry = indsutry;
    }
}
