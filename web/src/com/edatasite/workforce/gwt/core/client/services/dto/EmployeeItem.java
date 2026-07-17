package com.edatasite.workforce.gwt.core.client.services.dto;

import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class EmployeeItem extends SelectItem {

    private Integer department;
    private String position;
    private String email;
    private String phoneNumber;
    private String tgNumber;
    private Gender gender;
    private String imageUrl;
    private Boolean leader;
    private Boolean vacant;

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTgNumber() {
        return tgNumber;
    }

    public void setTgNumber(String tgNumber) {
        this.tgNumber = tgNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean isLeader() {
        return leader != null && leader;
    }

    public void setLeader(Boolean leader) {
        this.leader = leader;
    }

    public Boolean isVacant() {
        return vacant;
    }

    public void setVacant(Boolean vacant) {
        this.vacant = vacant;
    }
}
