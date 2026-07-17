package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.ArrayList;

public class SickLeaveSettings implements IsSerializable {
    private Integer fullyPaidLeaveDays;
    private Integer halfPaidLeaveDays;
    private Integer unPaidLeaveDays;
    private Integer minPeriodOfService;
    private SelectItem sickLeaveCategory;
    private ArrayList<PaymentDeductionSelectItem> allowances;

    public Integer getFullyPaidLeaveDays() {
        return fullyPaidLeaveDays;
    }

    public void setFullyPaidLeaveDays(Integer fullyPaidLeaveDays) {
        this.fullyPaidLeaveDays = fullyPaidLeaveDays;
    }

    public Integer getHalfPaidLeaveDays() {
        return halfPaidLeaveDays;
    }

    public void setHalfPaidLeaveDays(Integer halfPaidLeaveDays) {
        this.halfPaidLeaveDays = halfPaidLeaveDays;
    }

    public Integer getUnPaidLeaveDays() {
        return unPaidLeaveDays;
    }

    public void setUnPaidLeaveDays(Integer unPaidLeaveDays) {
        this.unPaidLeaveDays = unPaidLeaveDays;
    }

    public Integer getMinPeriodOfService() {
        return minPeriodOfService;
    }

    public void setMinPeriodOfService(Integer minPeriodOfService) {
        this.minPeriodOfService = minPeriodOfService;
    }

    public SelectItem getSickLeaveCategory() {
        return sickLeaveCategory;
    }

    public void setSickLeaveCategory(SelectItem sickLeaveCategory) {
        this.sickLeaveCategory = sickLeaveCategory;
    }


    public ArrayList<PaymentDeductionSelectItem> getAllowances() {
        if (allowances == null) {
            allowances = new ArrayList<>();
        }
        return allowances;
    }

    public void setAllowances(ArrayList<PaymentDeductionSelectItem> allowances) {
        this.allowances = allowances;
    }
}
