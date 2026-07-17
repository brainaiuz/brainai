package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TwilioSettings implements IsSerializable {
    public static final String NUMBER = "NUMBER";
    public static final String SID = "SID";
    public static final String TOKEN = "TOKEN";
    public static final String SID2= "SID2";

    private Integer objectID;
    private String number;
    private String accountSid;
    private String authToken;
    private String applicationSid;
    private boolean record;

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

    public String getNumberWithoutPlus() {
        return getNumber() == null ? null : getNumber().replace("\\+", "");
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }
}
