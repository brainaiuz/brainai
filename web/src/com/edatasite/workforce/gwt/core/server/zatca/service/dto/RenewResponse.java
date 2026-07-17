package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class RenewResponse {
    private String securityToken;
    private String secret;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
