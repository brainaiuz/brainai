package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/14/11
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "twilioSettings")
public class EdsTwilioSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "account_sid")
    private String accountSid;

    @Column(name = "record", columnDefinition = " boolean default false")
    private boolean record;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "application_sid")
    private String applicationSid;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getApplicationSid() {
        return applicationSid;
    }

    public void setApplicationSid(String applicationSid) {
        this.applicationSid = applicationSid;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public EdsTwilioSettings fromRPC(TwilioSettings item) {
        setNumber(item.getNumber());
        setAccountSid(item.getAccountSid());
        setAuthToken(item.getAuthToken());
        setApplicationSid(item.getApplicationSid());
        setRecord(isRecord());
        return this;
    }

    public TwilioSettings getRPC() {
        TwilioSettings item = new TwilioSettings();
        item.setObjectID(getObjectID());
        item.setNumber(getNumber());
        item.setAccountSid(getAccountSid());
        item.setAuthToken(getAuthToken());
        item.setApplicationSid(getApplicationSid());
        item.setRecord(isRecord());
        return item;
    }
}
