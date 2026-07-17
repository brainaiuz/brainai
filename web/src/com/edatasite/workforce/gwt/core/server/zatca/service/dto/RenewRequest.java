package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class RenewRequest {
    private String securityToken;
    private String secret;
    private String csr;

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

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }
}
