package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.List;

/**
 * Created by Abdurakhmonov Farrukh 03/15/2018.
 */
public class EntityInformationTO extends ResponseData {
    private String first_name;
    private String last_name;
    private String avatar_url;
    private CategoryTO status;
    private String phone_number;
    private String email;
    private CategoryTO company;
    private CategoryTO supervisor;
    private List<NoteDto> notes;

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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public CategoryTO getStatus() {
        return status;
    }

    public void setStatus(CategoryTO status) {
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

    public CategoryTO getCompany() {
        return company;
    }

    public void setCompany(CategoryTO company) {
        this.company = company;
    }

    public CategoryTO getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(CategoryTO supervisor) {
        this.supervisor = supervisor;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }
}
