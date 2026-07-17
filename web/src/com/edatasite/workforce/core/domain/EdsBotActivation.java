package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.utils.EdsContextParams;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Base64;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "bot_keys")
public class EdsBotActivation extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectId;

    @Column(name = "key", length = 20)
    private String key = UUID.uuid(20);

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "company_id")
    private Integer companyId;

    public EdsBotActivation() {
    }

    @Override
    public Integer getObjectID() {
        return objectId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getKey() {
        return key;
    }

    public String getKeyEncoded() {
        return key + "::" + Base64.getEncoder().encodeToString((EdsContextParams.getContextHost()).getBytes());
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EdsUser getCreator() {
        return creator;
    }

    @Override
    public void setCreator(EdsUser creator) {
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(EdsUser updater) {
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
