package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class SocialCheckTO extends ResponseData {
    private String social_type;
    private String social_token;
    private String user_token;
    private Boolean user_exist;
    private SocialDataTO social_data;

    public SocialCheckTO() {
    }

    public String getSocial_type() {
        return social_type;
    }

    public void setSocial_type(String social_type) {
        this.social_type = social_type;
    }

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public Boolean getUser_exist() {
        return user_exist;
    }

    public void setUser_exist(Boolean user_exist) {
        this.user_exist = user_exist;
    }

    public SocialDataTO getSocial_data() {
        return social_data;
    }

    public void setSocial_data(SocialDataTO social_data) {
        this.social_data = social_data;
    }
}
