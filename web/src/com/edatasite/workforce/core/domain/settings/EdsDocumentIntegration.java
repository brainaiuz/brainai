package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "document_integration")
public class EdsDocumentIntegration extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "didox_inn")
    private String didoxInn;

    @Column(name = "didox_password")
    private String didoxPassword;

    @Column(name = "didox_token")
    private String didoxToken;

    @Column(name = "didox_token_expire")
    private LocalDateTime didoxTokenExpire;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDidoxInn() {
        return didoxInn;
    }

    public void setDidoxInn(String didoxInn) {
        this.didoxInn = didoxInn;
    }

    public String getDidoxPassword() {
        return didoxPassword;
    }

    public void setDidoxPassword(String didoxPassword) {
        this.didoxPassword = didoxPassword;
    }

    public String getDidoxToken() {
        return didoxToken;
    }

    public void setDidoxToken(String didoxToken) {
        this.didoxToken = didoxToken;
    }

    public LocalDateTime getDidoxTokenExpire() {
        return didoxTokenExpire;
    }

    public void setDidoxTokenExpire(LocalDateTime didoxTokenExpire) {
        this.didoxTokenExpire = didoxTokenExpire;
    }
}
