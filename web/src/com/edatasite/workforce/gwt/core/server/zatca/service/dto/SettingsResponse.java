package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class SettingsResponse {

    private String certificate;
    private String csr;
    private String csrPemFormat;
    private String privateKey;
    private String privateKeyPemFormat;

    private String complianceSecurityToken;
    private String complianceSecret;

    private String securityToken;
    private String secret;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public String getCsrPemFormat() {
        return csrPemFormat;
    }

    public void setCsrPemFormat(String csrPemFormat) {
        this.csrPemFormat = csrPemFormat;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyPemFormat() {
        return privateKeyPemFormat;
    }

    public void setPrivateKeyPemFormat(String privateKeyPemFormat) {
        this.privateKeyPemFormat = privateKeyPemFormat;
    }

    public String getComplianceSecurityToken() {
        return complianceSecurityToken;
    }

    public void setComplianceSecurityToken(String complianceSecurityToken) {
        this.complianceSecurityToken = complianceSecurityToken;
    }

    public String getComplianceSecret() {
        return complianceSecret;
    }

    public void setComplianceSecret(String complianceSecret) {
        this.complianceSecret = complianceSecret;
    }

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SettingsResponse)) {
            return false;
        } else {
            SettingsResponse other = (SettingsResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label167: {
                    Object this$csr = this.getCsr();
                    Object other$csr = other.getCsr();
                    if (this$csr == null) {
                        if (other$csr == null) {
                            break label167;
                        }
                    } else if (this$csr.equals(other$csr)) {
                        break label167;
                    }

                    return false;
                }

                Object this$csrPemFormat = this.getCsrPemFormat();
                Object other$csrPemFormat = other.getCsrPemFormat();
                if (this$csrPemFormat == null) {
                    if (other$csrPemFormat != null) {
                        return false;
                    }
                } else if (!this$csrPemFormat.equals(other$csrPemFormat)) {
                    return false;
                }

                label153: {
                    Object this$privateKey = this.getPrivateKey();
                    Object other$privateKey = other.getPrivateKey();
                    if (this$privateKey == null) {
                        if (other$privateKey == null) {
                            break label153;
                        }
                    } else if (this$privateKey.equals(other$privateKey)) {
                        break label153;
                    }

                    return false;
                }

                Object this$privateKeyPemFormat = this.getPrivateKeyPemFormat();
                Object other$privateKeyPemFormat = other.getPrivateKeyPemFormat();
                if (this$privateKeyPemFormat == null) {
                    if (other$privateKeyPemFormat != null) {
                        return false;
                    }
                } else if (!this$privateKeyPemFormat.equals(other$privateKeyPemFormat)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingsResponse;
    }

    public int hashCode() {
        int result = 1;
        Object $csr = this.getCsr();
        result = result * 59 + ($csr == null ? 43 : $csr.hashCode());
        Object $csrPemFormat = this.getCsrPemFormat();
        result = result * 59 + ($csrPemFormat == null ? 43 : $csrPemFormat.hashCode());
        Object $privateKey = this.getPrivateKey();
        result = result * 59 + ($privateKey == null ? 43 : $privateKey.hashCode());
        Object $privateKeyPemFormat = this.getPrivateKeyPemFormat();
        result = result * 59 + ($privateKeyPemFormat == null ? 43 : $privateKeyPemFormat.hashCode());
        return result;
    }

    public String toString() {
        String var10000 = this.getCsr();
        return "CsrOutputDto(csr=" + var10000 + ", csrPemFormat=" + this.getCsrPemFormat() + ", privateKey=" + this.getPrivateKey() + ", privateKeyPemFormat=" + this.getPrivateKeyPemFormat() + ")";
    }
}
