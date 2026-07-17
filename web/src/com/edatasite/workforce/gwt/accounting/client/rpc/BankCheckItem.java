package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankCheckItem implements IsSerializable {
    private Integer objectID;
    private AccountItem account;
    private BigDecimal amount;
    private String description;
    private SelectItem crmAccount;
    private SelectItem client;
    private String quickbookItemID;
    private SelectItem project;

    public BankCheckItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public AccountItem getAccount() {
        return account;
    }

    public void setAccount(AccountItem account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public String getQuickbookItemID() {
        return quickbookItemID;
    }

    public void setQuickbookItemID(String quickbookItemID) {
        this.quickbookItemID = quickbookItemID;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }
}
