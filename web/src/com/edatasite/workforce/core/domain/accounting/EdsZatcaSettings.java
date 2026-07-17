package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.ZatcaState;

import javax.persistence.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "zatcasettings")
public class EdsZatcaSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(columnDefinition = "text")
    private String certificate;

    @Column(columnDefinition = "text")
    private String csr;
    @Column(columnDefinition = "text")
    private String privateKey;

    @Column(columnDefinition = "text")
    private String complianceSecurityToken;
    @Column(columnDefinition = "text")
    private String complianceSecret;

    @Column(columnDefinition = "text")
    private String securityToken;
    @Column(columnDefinition = "text")
    private String secret;

    @Enumerated(EnumType.STRING)
    private ZatcaState state = ZatcaState.PENDING;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
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

    public ZatcaState getState() {
        return state;
    }

    public void setState(ZatcaState state) {
        this.state = state;
    }

    public boolean isActive() {
        return ZatcaState.ACTIVE.equals(this.state);
    }

}
