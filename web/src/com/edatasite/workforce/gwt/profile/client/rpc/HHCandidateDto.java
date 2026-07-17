package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.HHIdDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HHCandidateDto {
    private String first_name;
    private String last_name;
    private String middle_name;
    private String birth_date;
    private HHIdDto gender;
    private RevolutAmountDto salary;
    private List<HHCandidateContactDto> contact;
    private String skills;
    private DynamicDto total_experience;
    private HHDownloadDto download;

    public DynamicDto getTotal_experience() {
        return total_experience;
    }

    public void setTotal_experience(DynamicDto total_experience) {
        this.total_experience = total_experience;
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

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public HHIdDto getGender() {
        return gender;
    }

    public void setGender(HHIdDto gender) {
        this.gender = gender;
    }

    public RevolutAmountDto getSalary() {
        return salary;
    }

    public void setSalary(RevolutAmountDto salary) {
        this.salary = salary;
    }

    public List<HHCandidateContactDto> getContact() {
        return contact;
    }

    public void setContact(List<HHCandidateContactDto> contact) {
        this.contact = contact;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public HHDownloadDto getDownload() {
        return download;
    }

    public void setDownload(HHDownloadDto download) {
        this.download = download;
    }
}
