package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * User : Dilsh0d Madrahimov on 9/17/2019 3:46 PM
 */
public class UserAddDTO extends ResponseData {
    private String first_name;
    private String last_name;
    private String email;
    private Long driver_id;
    private String position;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Long driver_id) {
        this.driver_id = driver_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
