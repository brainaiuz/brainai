package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 9/11/2018.
 */
public class AddPushToken extends ResponseData {

    private String push_token;
    private String device_os;

    public AddPushToken() {
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }
}
