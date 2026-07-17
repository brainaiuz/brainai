package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Abdurakhmonov Farrukh on 04/10/2018.
 */
public class AddContactTO extends ResponseData {
    private String first_name;
    private Integer title_id;
    private String last_name;
    private String phone_number;

    public Integer getTitle_id() {
        return title_id;
    }

    public void setTitle_id(Integer title_id) {
        this.title_id = title_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    private String job_title;

    private ArrayList<PhoneDto> phoneNumbers;
    private ArrayList<EmailDto> emails;
    private String email;
    private Integer company;
    private String company_name;//Created for zapier/shopify integration
    private String note;//Created for zapier/shopify integration
    private boolean is_customer = false;//Created for zapier/shopify integration
    private Integer supervisor;
    private ArrayList<ContactAddressAddTO> contact_addresses;
    private ArrayList<Object> custom_fields;
    private boolean name_unique;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date_of_birth;


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

    public String getCompany_name() {
        return company_name;
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

    public Integer getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Integer supervisor) {
        this.supervisor = supervisor;
    }

    public ArrayList<ContactAddressAddTO> getContact_addresses() {
        return contact_addresses;
    }

    public void setContact_addresses(ArrayList<ContactAddressAddTO> contact_addresses) {
        this.contact_addresses = contact_addresses;
    }

    public ArrayList<Object> getCustom_fields() {
        if(custom_fields==null) {
            custom_fields = new ArrayList<>();
        }
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public boolean isName_unique() {
        return name_unique;
    }

    public void setName_unique(boolean name_unique) {
        this.name_unique = name_unique;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public ArrayList<PhoneDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<PhoneDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public ArrayList<EmailDto> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<EmailDto> emails) {
        this.emails = emails;
    }

}
