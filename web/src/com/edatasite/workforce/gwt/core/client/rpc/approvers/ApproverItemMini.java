package com.edatasite.workforce.gwt.core.client.rpc.approvers;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Hayot on 2/17/2016.
 */
public class ApproverItemMini implements IsSerializable {
    private Integer objectID;
    private Integer clonedFrom;
    private ReferenceItem status;
    private SelectItem exactEmployee; // bu leave requestni approve qilish kk bo'lgan yoki approve qilgan employee
    private Integer appproveStatusId;
    private Integer rejectStatusId;
    private Integer approverOrder;
    private DateNonConvertable fromBackupEmployeeDate;
    private DateNonConvertable dueBackupEmployeeDate;
    private Integer startStatusId;
    private boolean isVozlojeniya;
    private boolean backup;

    public SelectItem getExactEmployee() {
        return exactEmployee;
    }

    public void setExactEmployee(SelectItem exactEmployee) {
        this.exactEmployee = exactEmployee;
    }

    public Integer getClonedFrom() {
        return clonedFrom == null ? getObjectID() : clonedFrom;
    }

    public void setClonedFrom(Integer clonedFrom) {
        this.clonedFrom = clonedFrom;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public DateNonConvertable getFromBackupEmployeeDate() {
        return fromBackupEmployeeDate;
    }

    public void setFromBackupEmployeeDate(DateNonConvertable fromBackupEmployeeDate) {
        this.fromBackupEmployeeDate = fromBackupEmployeeDate;
    }

    public DateNonConvertable getDueBackupEmployeeDate() {
        return dueBackupEmployeeDate;
    }

    public void setDueBackupEmployeeDate(DateNonConvertable dueBackupEmployeeDate) {
        this.dueBackupEmployeeDate = dueBackupEmployeeDate;
    }


    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public Integer getAppproveStatusId() {
        return appproveStatusId;
    }

    public void setAppproveStatusId(Integer appproveStatusId) {
        this.appproveStatusId = appproveStatusId;
    }

    public Integer getRejectStatusId() {
        return rejectStatusId;
    }

    public void setRejectStatusId(Integer rejectStatusId) {
        this.rejectStatusId = rejectStatusId;
    }

    public Integer getApproverOrder() {
        return approverOrder;
    }

    public void setApproverOrder(Integer approverOrder) {
        this.approverOrder = approverOrder;
    }

    public Integer getStartStatusId() {
        return startStatusId;
    }

    public void setStartStatusId(Integer startStatusId) {
        this.startStatusId = startStatusId;
    }

    @Override
    public String toString() {
        return exactEmployee != null ? exactEmployee.getName() : "";
    }

    public boolean isVozlojeniya() {
        return isVozlojeniya;
    }

    public void setVozlojeniya(boolean vozlojeniya) {
        isVozlojeniya = vozlojeniya;
    }

    public boolean isBackup() {
        return this.backup;
    }

    public void setBackup(final boolean backup) {
        this.backup = backup;
    }
}
