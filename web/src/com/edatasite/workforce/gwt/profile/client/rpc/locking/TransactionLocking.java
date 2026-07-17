package com.edatasite.workforce.gwt.profile.client.rpc.locking;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

public class TransactionLocking implements IsSerializable {
    private String description;
    private DateNonConvertable lockDate;
    private String status;
    private HashMap<String, TransactionLockingModule> modules;
    private String reason;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateNonConvertable getLockDate() {
        return lockDate;
    }

    public void setLockDate(DateNonConvertable lockDate) {
        this.lockDate = lockDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, TransactionLockingModule> getModules() {
        if (modules == null) {
            modules = new HashMap<>();
        }
        return modules;
    }

    public void setModules(HashMap<String, TransactionLockingModule> modules) {
        this.modules = modules;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
