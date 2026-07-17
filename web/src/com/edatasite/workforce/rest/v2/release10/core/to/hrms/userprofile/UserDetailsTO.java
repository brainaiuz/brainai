package com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.PhoneEmailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 9/25/2017.
 */
public class UserDetailsTO extends ResponseData {

    private Integer user_id;
    private String full_name;
    private String user_email;
    private String user_name;
    private ArrayList<PhoneEmailTO> phone_number;
    private String company_locale;
    private Integer user_company_id;
    private String company_name;
    private String timezone;
    private String website;
    private String office_phone;
    private AddressTO address;
    private ArrayList<CustomFieldsTO> custom_fields;

    public UserDetailsTO() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<PhoneEmailTO> getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(ArrayList<PhoneEmailTO> phone_number) {
        this.phone_number = phone_number;
    }

    public String getCompany_locale() {
        return company_locale;
    }

    public void setCompany_locale(String company_locale) {
        this.company_locale = company_locale;
    }

    public Integer getUser_company_id() {
        return user_company_id;
    }

    public void setUser_company_id(Integer user_company_id) {
        this.user_company_id = user_company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOffice_phone() {
        return office_phone;
    }

    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }

    public AddressTO getAddress() {
        return address;
    }

    public void setAddress(AddressTO address) {
        this.address = address;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
