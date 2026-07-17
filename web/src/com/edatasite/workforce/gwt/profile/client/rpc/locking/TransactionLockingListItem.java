package com.edatasite.workforce.gwt.profile.client.rpc.locking;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TransactionLockingListItem implements IsSerializable {
    public static String ACTION = "action";
    public static String DESCRIPTION = "description";
    public static String LOCK_DATE = "lockDate";
    public static String STATUS = "status";
    public static String CREATED_DATE = "createdDate";
    public static String CREATED_BY = "createdBy";
    public static String UPDATED_DATE = "updatedDate";
    public static String UPDATED_BY = "updatedBy";

    private Integer id;
    private String description;
    private DateNonConvertable lockDate;
    private String status;
    private DateNonConvertable createdDate;
    private SelectItem createdBy;
    private DateNonConvertable updatedDate;
    private SelectItem updatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    public DateNonConvertable getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(SelectItem updatedBy) {
        this.updatedBy = updatedBy;
    }
}
