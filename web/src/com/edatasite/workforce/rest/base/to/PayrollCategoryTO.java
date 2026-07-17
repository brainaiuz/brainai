package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d Madrahimov on 05/02/17 1:54 PM
 */
public class PayrollCategoryTO implements IsSerializable {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    SelectItemTO category;
    SelectItemTO type;
    SelectItemTO account;
    BigDecimal amount;


    public PayrollCategoryTO() {
    }

    public PayrollCategoryTO(PaymentDeductionObject item) {
        this.category = item.getCategoryItem() != null ? new SelectItemTO(item.getCategoryItem().getId(), item.getCategoryItem().getName()) : null;
        if (item.getType() != null) {
            if (item.getType() == 0 || item.isLoan()) {
                this.type = new SelectItemTO(wfmStrings .fixed());
            } else {
                this.type = new SelectItemTO(item.getPercentage() + "% of Basic Salary");
            }
        } else {
            this.type = new SelectItemTO(wfmStrings.fixed());
        }
        this.amount = item.getPaymentAmount();
    }


    public SelectItemTO getCategory() {
        return category;
    }

    public void setCategory(SelectItemTO category) {
        this.category = category;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
