package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d Madrahimov on 5/13/15 8:02 PM
 */
public class ManualEntryItemTO implements IsSerializable {
    Integer id;
    String description;
    SelectItemTO account;
    BigDecimal debit;
    BigDecimal credit;
    SelectItemTO name;
    SelectItemTO project;
    SelectItemTO department;

    public ManualEntryItemTO() {
    }

    public ManualEntryItemTO(NewManualTransactionItem item) {
        this.id = item.getObjectId();
        this.description = item.getDescription();
        this.account = new SelectItemTO(item.getAccountItem().getId(), item.getAccountItem().getName(), item.getAccountItem().getCode(), "");
        this.debit = item.getDebit();
        this.credit = item.getCredit();
        this.name = WrapUtils.wrapSelectItemTO(item.getCustomerOrSupplier());
        this.department = WrapUtils.wrapSelectItemTO(item.getDepartment());
        this.project = WrapUtils.wrapSelectItemTO(item.getProject());
    }

    public NewManualTransactionItem wrap(ManualEntryItemTO manualEntryItemTO) {
        NewManualTransactionItem item = new NewManualTransactionItem();
        item.setObjectId(manualEntryItemTO.getId());
        item.setAccountItem(new AccountItem(manualEntryItemTO.getAccount().getId(), manualEntryItemTO.getAccount().getCode(), manualEntryItemTO.getAccount().getName()));
        item.setDebit(manualEntryItemTO.getDebit());
        item.setCredit(manualEntryItemTO.getCredit());
        item.setDescription(manualEntryItemTO.getDescription());
        item.setCustomerOrSupplier(WrapUtils.wrapSelectItem(manualEntryItemTO.getName()));
        item.setProject(WrapUtils.wrapSelectItem(manualEntryItemTO.getProject()));
        item.setDepartment(WrapUtils.wrapSelectItem(manualEntryItemTO.getDepartment()));
        return item;
    }


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

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public SelectItemTO getName() {
        return name;
    }

    public void setName(SelectItemTO name) {
        this.name = name;
    }

    public SelectItemTO getProject() {
        return project;
    }

    public void setProject(SelectItemTO project) {
        this.project = project;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }
}
