package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class PhoneTO extends ResponseData {

    private String country_code;
    private String phone_number;
    private String type;
    private String category;
    private Boolean primaryContact;

    public PhoneTO() {
    }

    public PhoneTO(String phone_number) {
        this.phone_number = phone_number;
    }

    public PhoneTO(String country_code, String phone_number) {
        this.country_code = country_code;
        this.phone_number = phone_number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        String phone = "";
        if (country_code != null && !"".equals(country_code)) {
            phone = phone + country_code;
        }
        if (phone_number != null && !"".equals(phone_number)) {
            phone = phone + phone_number;
        }
        return phone;
    }

    public Boolean getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }
}
