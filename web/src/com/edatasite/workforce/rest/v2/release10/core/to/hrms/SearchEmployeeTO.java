package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;

/**
 * Created by Dilshod Madrahimov on 11/07/2017.
 */
public class SearchEmployeeTO extends IdNameTO {

    private String phone;
    private String email;
    private String avatar_image;
    private IdNameTO department;
    private IdNameTO position;


    public SearchEmployeeTO() {

    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public IdNameTO getDepartment() {
        return department;
    }

    public void setDepartment(IdNameTO department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IdNameTO getPosition() {
        return position;
    }

    public void setPosition(IdNameTO position) {
        this.position = position;
    }
}
