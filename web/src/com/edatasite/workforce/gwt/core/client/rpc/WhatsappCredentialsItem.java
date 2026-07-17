package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WhatsappCredentialsItem implements IsSerializable {
    private String accessToken;
    private String phoneNumber;

    public WhatsappCredentialsItem(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public WhatsappCredentialsItem() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
