package com.edatasite.workforce.gwt.core.client.rpc.approvers;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BackupEmployeeItem implements IsSerializable {
    private ApproverItemMini parentBackupEmployee;
    private ArrayList<ApproverItemMini> childList = new ArrayList<>();
    private BigDecimal dutyPercentage;

    public ApproverItemMini getParentBackupEmployee() {
        return parentBackupEmployee;
    }

    public void setParentBackupEmployee(ApproverItemMini parentBackupEmployee) {
        this.parentBackupEmployee = parentBackupEmployee;
    }

    public ArrayList<ApproverItemMini> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<ApproverItemMini> childList) {
        this.childList = childList;
    }

    public BigDecimal getDutyPercentage() {
        return dutyPercentage;
    }

    public void setDutyPercentage(BigDecimal dutyPercentage) {
        this.dutyPercentage = dutyPercentage;
    }
}
