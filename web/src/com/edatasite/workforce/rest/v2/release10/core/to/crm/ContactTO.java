package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh 01/29/2018.
 */
public class ContactTO extends ResponseData {
    private String name;
    private String first_name;//added for shopifier
    private String last_name;//added for shopifier
    private Integer item_id;
    private String phone;
    private String avatar_image;
    private ContactsTO contacts;
    private CrmAccountTO company;
    private Integer titleId;
    private String titleName;
    private String jobTitle;
    private ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();

    public ContactTO() {
    }

    public ContactTO(Integer item_id) {
        this.item_id = item_id;
    }

    public ContactTO(Integer item_id, Integer company_id) {
        this.item_id = item_id;
        this.company = new CrmAccountTO(company_id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public ContactsTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsTO contacts) {
        this.contacts = contacts;
    }

    public CrmAccountTO getCompany() {
        return company;
    }

    public void setCompany(CrmAccountTO company) {
        this.company = company;
    }

    public ArrayList<EntityContactAddressTO> getEntityAddresses() {
        if(entityAddresses==null) {
            entityAddresses = new ArrayList<>();
        }
        return entityAddresses;
    }

    public void setEntityAddresses(ArrayList<EntityContactAddressTO> entityAddresses) {
        this.entityAddresses = entityAddresses;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

}
