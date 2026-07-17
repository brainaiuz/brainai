package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "firebase_credentials")
public class EdsFirebaseCredentials extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "type")
    private String type;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "private_key_id", length = 500)
    private String privateKeyId;

    @Column(name = "private_key", length = 2050)
    private String privateKey;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "auth_uri")
    private String authUri;

    @Column(name = "token_uri")
    private String tokenUri;

    @Column(name = "auth_provider_x509_cert_url")
    private String authProviderX509CertUrl;

    @Column(name = "client_x509_cert_url", length = 500)
    private String clientX509CertUrl;

    @Column(name = "universe_domain")
    private String universeDomain;

    @Column(name = "assertion_token", length = 2050)
    private String assertionToken;

    @Column(name = "assertion_created_at")
    private Date assertionCreatedAt;

    @Column(name = "assertion_expire_at")
    private Date assertionExpiredAt;

    @Column(name = "oauth2_token", length = 2050)
    private String OAuth2Token;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateKeyId() {
        return privateKeyId;
    }

    public void setPrivateKeyId(String privateKeyId) {
        this.privateKeyId = privateKeyId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getAuthProviderX509CertUrl() {
        return authProviderX509CertUrl;
    }

    public void setAuthProviderX509CertUrl(String authProviderX509CertUrl) {
        this.authProviderX509CertUrl = authProviderX509CertUrl;
    }

    public String getClientX509CertUrl() {
        return clientX509CertUrl;
    }

    public void setClientX509CertUrl(String clientX509CertUrl) {
        this.clientX509CertUrl = clientX509CertUrl;
    }

    public String getUniverseDomain() {
        return universeDomain;
    }

    public void setUniverseDomain(String universeDomain) {
        this.universeDomain = universeDomain;
    }

    public String getAssertionToken() {
        return assertionToken;
    }

    public void setAssertionToken(String assertionToken) {
        this.assertionToken = assertionToken;
    }

    public Date getAssertionCreatedAt() {
        return assertionCreatedAt;
    }

    public void setAssertionCreatedAt(Date assertionCreatedAt) {
        this.assertionCreatedAt = assertionCreatedAt;
    }

    public Date getAssertionExpiredAt() {
        return assertionExpiredAt;
    }

    public void setAssertionExpiredAt(Date assertionExpiredAt) {
        this.assertionExpiredAt = assertionExpiredAt;
    }

    public String getOAuth2Token() {
        return OAuth2Token;
    }

    public void setOAuth2Token(String OAuth2Token) {
        this.OAuth2Token = OAuth2Token;
    }
}
