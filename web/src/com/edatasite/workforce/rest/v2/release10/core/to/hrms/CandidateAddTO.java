package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldPostTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class CandidateAddTO extends ResponseData {
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String employer;
    private String skills;
    private Integer work_experience;
    private Double expected_salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date_of_birth;
    private ArrayList<Integer> vacancy_list;
    private ArrayList<AddressTO> addresses;
    private ArrayList<CustomFieldPostTO> custom_fields;
    private ArrayList<SpokenLanguageTO> spoken_languages;

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

    public ArrayList<Integer> getVacancy_list() {
        return vacancy_list;
    }

    public void setVacancy_list(ArrayList<Integer> vacancy_list) {
        this.vacancy_list = vacancy_list;
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

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Integer getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(Integer work_experience) {
        this.work_experience = work_experience;
    }

    public Double getExpected_salary() {
        return expected_salary;
    }

    public void setExpected_salary(Double expected_salary) {
        this.expected_salary = expected_salary;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public ArrayList<AddressTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressTO> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<CustomFieldPostTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldPostTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public ArrayList<SpokenLanguageTO> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(ArrayList<SpokenLanguageTO> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }
}
