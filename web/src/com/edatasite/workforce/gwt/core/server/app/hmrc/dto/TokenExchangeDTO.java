package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenExchangeDTO {

    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("grant_type")
    private String grantType = "authorization_code";
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("code")
    private String code;


    public TokenExchangeDTO() {
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
