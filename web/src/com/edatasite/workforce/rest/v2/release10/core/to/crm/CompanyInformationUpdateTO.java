package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov 04/04/2018.
 */
public class CompanyInformationUpdateTO extends ResponseData {

    private Integer account_owner;
    private String account_name;
    private Integer parent_account;
    private Integer primary_contact;
    private ArrayList<String> account_types;


    public CompanyInformationUpdateTO() {
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

    public Integer getParent_account() {
        return parent_account;
    }

    public void setParent_account(Integer parent_account) {
        this.parent_account = parent_account;
    }

    public Integer getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(Integer primary_contact) {
        this.primary_contact = primary_contact;
    }

    public ArrayList<String> getAccount_types() {
        return account_types;
    }

    public void setAccount_types(ArrayList<String> account_types) {
        this.account_types = account_types;
    }
}
