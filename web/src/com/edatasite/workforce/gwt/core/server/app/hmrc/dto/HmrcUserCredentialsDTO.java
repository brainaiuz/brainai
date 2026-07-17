package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;

public class HmrcUserCredentialsDTO {
    private String accessToken;
    private String tokenType;
    private Long expiresInSeconds;
    private String refreshToken;
    private String scope;

    public HmrcUserCredentialsDTO() {
    }

    public HmrcUserCredentialsDTO(TokenResponseTO tokenResponseTO) {
        setAccessToken(tokenResponseTO.getAccess_token());
        setTokenType(tokenResponseTO.getToken_type());
        setExpiresInSeconds(Long.valueOf(tokenResponseTO.getExpires_in()));
        setRefreshToken(tokenResponseTO.getRefresh_token());
        setScope(tokenResponseTO.getScope());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(Long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
