package com.edatasite.workforce.gwt.profile.client.rpc.locking;

import java.io.Serializable;

public class TransactionLockingModule implements Serializable {
    private String module;
    private String description;
    private String status;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
