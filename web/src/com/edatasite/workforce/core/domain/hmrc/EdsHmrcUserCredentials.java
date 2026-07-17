package com.edatasite.workforce.core.domain.hmrc;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "hmrcUserCredentials")
public class EdsHmrcUserCredentials extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "token_type")
    private String tokenType;
    @Column(name = "expires_in")
    private Long expiresInSeconds;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "scope")
    private String scope;

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

    public HmrcUserCredentialsDTO toDTO() {
        HmrcUserCredentialsDTO credentialsDTO = new HmrcUserCredentialsDTO();
        credentialsDTO.setAccessToken(getAccessToken());
        credentialsDTO.setTokenType(getTokenType());
        credentialsDTO.setRefreshToken(getRefreshToken());
        credentialsDTO.setScope(getScope());
        credentialsDTO.setExpiresInSeconds(getExpiresInSeconds());
        return credentialsDTO;
    }
}
