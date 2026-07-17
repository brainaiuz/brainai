package com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile;

import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 01/04/2018.
 */
public class UserUpdateTO extends ResponseData {

    private String user_name;
    private String email;
    private PhoneTO phone;

    public UserUpdateTO() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PhoneTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneTO phone) {
        this.phone = phone;
    }
}
