package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.HashMap;

/**
 * Created by Anvar Akramov on 02/05/2018.
 */
public class SignUpGenericTO extends ResponseData {

    private String company_name;
    private String company_language;
    private String email;
    private String name;
    private PhoneTO phone;
    private HashMap<String, String> custom_fields;

    public SignUpGenericTO() {
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_language() {
        return company_language;
    }

    public void setCompany_language(String company_language) {
        this.company_language = company_language;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneTO phone) {
        this.phone = phone;
    }

    public HashMap<String, String> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(HashMap<String, String> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
