package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;

import java.io.Serializable;

/**
 * Created by Dilsh0d on 10/24/2017.
 */
public class SignUpItem implements Serializable {
    private String socialNetworkId;
    private String email;
    private String firstName;
    private String lastName;
    private String accessToken;
    private RegistrationTypeEnum registrationType;

    public SignUpItem() {
    }

    public SignUpItem(String socialNetworkId, String email, String firstName, String lastName, String accessToken, RegistrationTypeEnum registrationType) {
        this.socialNetworkId = socialNetworkId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessToken = accessToken;
        this.registrationType = registrationType;
    }

    public String getSocialNetworkId() {
        return socialNetworkId;
    }

    public void setSocialNetworkId(String socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }
}
