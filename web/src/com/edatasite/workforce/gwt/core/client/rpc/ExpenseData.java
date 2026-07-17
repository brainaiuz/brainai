package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/29/11
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseData implements IsSerializable {
    private Integer objectID;
    private Integer paymentType;
    private String title;
    private Double amount;
    private boolean inBaseCurrency;
    private boolean applied = true;
    private Integer accountID;
    private String account;

    public ExpenseData() {
    }

    public ExpenseData(Integer objectID, String title, Double amount, boolean inBaseCurrency, Integer accountID, String account) {
        this.objectID = objectID;
        this.title = title;
        this.amount = amount;
        this.inBaseCurrency = inBaseCurrency;
        this.accountID = accountID;
        this.account = account;
    }

    public ExpenseData(Integer objectID, String title, Double amount, boolean inBaseCurrency, Integer accountID, String account, Integer paymentType) {
        this(objectID, title, amount, inBaseCurrency, accountID, account);
        this.paymentType = paymentType;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isInBaseCurrency() {
        return inBaseCurrency;
    }

    public void setInBaseCurrency(boolean inBaseCurrency) {
        this.inBaseCurrency = inBaseCurrency;
    }
}
