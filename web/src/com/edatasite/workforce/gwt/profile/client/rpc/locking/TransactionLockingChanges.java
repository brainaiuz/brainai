package com.edatasite.workforce.gwt.profile.client.rpc.locking;

import java.io.Serializable;

public class TransactionLockingChanges implements Serializable {

    private String field;
    private String oldValue;
    private String newValue;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
