package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class LeadAddTO extends ResponseData {

    private String first_name;
    private String last_name;
    private Integer status;
    private String phone_number;
    private String email;
    private Integer company;
    private ArrayList<ContactAddressAddTO> contact_addresses;
    private ArrayList<Object> custom_fields;
    private ArrayList<NoteDto> notes;

    public LeadAddTO() {
    }

    public LeadAddTO(String first_name, String last_name, Integer status, String phone_number, String email, Integer company, ArrayList<ContactAddressAddTO> contact_addresses, ArrayList<Object> custom_fields, ArrayList<NoteDto> notes) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.status = status;
        this.phone_number = phone_number;
        this.email = email;
        this.company = company;
        this.contact_addresses = contact_addresses;
        this.custom_fields = custom_fields;
        this.notes = notes;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public ArrayList<ContactAddressAddTO> getContact_addresses() {
        return contact_addresses;
    }

    public void setContact_addresses(ArrayList<ContactAddressAddTO> contact_addresses) {
        this.contact_addresses = contact_addresses;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public ArrayList<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteDto> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeadAddTO)) return false;

        LeadAddTO leadAddTO = (LeadAddTO) o;

        if (getFirst_name() != null ? !getFirst_name().equals(leadAddTO.getFirst_name()) : leadAddTO.getFirst_name() != null)
            return false;
        if (getLast_name() != null ? !getLast_name().equals(leadAddTO.getLast_name()) : leadAddTO.getLast_name() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(leadAddTO.getStatus()) : leadAddTO.getStatus() != null)
            return false;
        if (getPhone_number() != null ? !getPhone_number().equals(leadAddTO.getPhone_number()) : leadAddTO.getPhone_number() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(leadAddTO.getEmail()) : leadAddTO.getEmail() != null) return false;
        if (getCompany() != null ? !getCompany().equals(leadAddTO.getCompany()) : leadAddTO.getCompany() != null)
            return false;
        if (getContact_addresses() != null ? !getContact_addresses().equals(leadAddTO.getContact_addresses()) : leadAddTO.getContact_addresses() != null)
            return false;
        if (getCustom_fields() != null ? !getCustom_fields().equals(leadAddTO.getCustom_fields()) : leadAddTO.getCustom_fields() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(leadAddTO.getNotes()) : leadAddTO.getNotes() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getFirst_name() != null ? getFirst_name().hashCode() : 0;
        result = 31 * result + (getLast_name() != null ? getLast_name().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getPhone_number() != null ? getPhone_number().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getCompany() != null ? getCompany().hashCode() : 0);
        result = 31 * result + (getContact_addresses() != null ? getContact_addresses().hashCode() : 0);
        result = 31 * result + (getCustom_fields() != null ? getCustom_fields().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LeadAddTO{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", status=" + status +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", company=" + company +
                ", contact_addresses=" + contact_addresses +
                ", custom_fields=" + custom_fields +
                ", notes=" + notes +
                '}';
    }
}
