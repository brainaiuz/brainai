package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;

import java.util.ArrayList;

/**
 * Created by Akramov Anvar on 04/10/2018.
 */
public class AddZapierContactTO extends ResponseData {
    private Integer id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private CrmAccountTO company;
    private String company_name;//Created for zapier/shopify integration
    private String note;//Created for zapier/shopify integration
    private boolean is_customer = false;//Created for zapier/shopify integration
    private ArrayList<ContactAddressAddTO> addresses;


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

    public String getCompany_name() {
        return company_name;
    }

    public CrmAccountTO getCompany() {
        return company;
    }

    public void setCompany(CrmAccountTO company) {
        this.company = company;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isIs_customer() {
        return is_customer;
    }

    public void setIs_customer(boolean is_customer) {
        this.is_customer = is_customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<ContactAddressAddTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<ContactAddressAddTO> addresses) {
        this.addresses = addresses;
    }
}
