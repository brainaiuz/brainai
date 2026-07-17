package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.List;

/**
 * Created by Anvar Akramov 03/23/2018.
 */
public class CompanyAddTO extends ResponseData {

    private Integer account_owner;
    private String account_name;
    private String email;
    private String phone;
    private Integer parent_account;
    private List<String> account_types;
    private List<CompanyAddressInformationTO> address_information;
    private List<Object> custom_fields;

    public CompanyAddTO() {
    }

    public Integer getAccount_owner() {
        return account_owner;
    }

    public void setAccount_owner(Integer account_owner) {
        this.account_owner = account_owner;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getParent_account() {
        return parent_account;
    }

    public void setParent_account(Integer parent_account) {
        this.parent_account = parent_account;
    }

    public List<String> getAccount_types() {
        return account_types;
    }

    public void setAccount_types(List<String> account_types) {
        this.account_types = account_types;
    }

    public List<CompanyAddressInformationTO> getAddress_information() {
        return address_information;
    }

    public void setAddress_information(List<CompanyAddressInformationTO> address_information) {
        this.address_information = address_information;
    }

    public List<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
