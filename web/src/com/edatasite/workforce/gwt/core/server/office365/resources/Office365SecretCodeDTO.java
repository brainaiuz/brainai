package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by umakarimov on 9/21/15.
 */
public class Office365SecretCodeDTO implements IsSerializable {
    private String email;
    private String objectId;
    private String secretCode;

    private Integer authTokenId;

    public Office365SecretCodeDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public Integer getAuthTokenId() {
        return authTokenId;
    }

    public void setAuthTokenId(Integer authTokenId) {
        this.authTokenId = authTokenId;
    }
}
