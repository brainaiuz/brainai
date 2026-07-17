package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Created by Sherzod on 1/14/2016.
 */
public class CashFlowItem implements IsSerializable {
    private SelectItem account;
    private BigDecimal balance;
    private String code;
    private Integer parentId;
    private String parentCode;
    private String parentName;
    private HashSet<CashFlowItem> childs;
    private boolean calculated = false;
    private boolean gainAndLoss = false;

    private boolean foreignAccount;
    private Integer accountCurrencyId;

    public CashFlowItem() {
    }

    public CashFlowItem(Integer accountId, String accountCode, String name, BigDecimal balance) {
        this.account = new SelectItem(accountId, name);
        this.code = accountCode;
        this.balance = balance;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public HashSet<CashFlowItem> getChilds() {
        if (childs == null) {
            childs = new HashSet<>();
        }
        return childs;
    }

    public void setChilds(HashSet<CashFlowItem> childs) {
        this.childs = childs;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean isForeignAccount() {
        return foreignAccount;
    }

    public void setForeignAccount(boolean foreignAccount) {
        this.foreignAccount = foreignAccount;
    }

    public Integer getAccountCurrencyId() {
        return accountCurrencyId;
    }

    public void setAccountCurrencyId(Integer accountCurrencyId) {
        this.accountCurrencyId = accountCurrencyId;
    }

    public boolean isGainAndLoss() {
        return this.gainAndLoss;
    }

    public void setGainAndLoss(final boolean gainAndLoss) {
        this.gainAndLoss = gainAndLoss;
    }
}
