package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

public class UserItem implements Serializable {

    private String companyId;
    private String database;
    private String serviceId;
    private String userId;

    public UserItem(String companyId, String database, String serviceId, String userId) {
        this.companyId = companyId;
        this.database = database;
        this.serviceId = serviceId;
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
