package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class SignUpTO extends ResponseData {

    private String email;
    private String name;
    private PhoneTO phone;
    private String company_name;
    private String registration_type;
    private String social_token;
    private String avatar_image;

    public SignUpTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneTO phone) {
        this.phone = phone;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getRegistration_type() {
        return registration_type;
    }

    public void setRegistration_type(String registration_type) {
        this.registration_type = registration_type;
    }

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }
}
