package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 27.02.2009
 * Time: 18:00:55
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalanceItem implements IsSerializable {
    private Integer accountId;
    private Integer parentId;

    private String code;
    private String parentCode;

    private String name;
    private String parentName;

    private String categoryCode;
    private String categoryType;

    private boolean floatingAccount;

    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal beginningBalance;
    private BigDecimal endingBalance;
    private BigDecimal beginningDebit;
    private BigDecimal endingDebit;
    private BigDecimal beginningCredit;
    private BigDecimal endingCredit;
    private Integer baseAccountId;
    private Integer key;
    private HashSet<TrialBalanceItem> childs;
    private boolean calculated = false;

    public TrialBalanceItem() {
    }

    public TrialBalanceItem(Integer accountId, String accountCode, String name, BigDecimal debit, BigDecimal credit, BigDecimal beginningBalance, BigDecimal endingBalance) {
        this.accountId = accountId;
        this.code = accountCode;
        this.name = name;
        this.debit = debit;
        this.credit = credit;
    }

    public TrialBalanceItem(Integer accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getBeginningDebit() {
        return beginningDebit;
    }

    public void setBeginningDebit(BigDecimal beginningDebit) {
        this.beginningDebit = beginningDebit;
    }

    public BigDecimal getBeginningCredit() {
        return beginningCredit;
    }

    public void setBeginningCredit(BigDecimal beginningCredit) {
        this.beginningCredit = beginningCredit;
    }

    public BigDecimal getEndingDebit() {
        return endingDebit;
    }

    public void setEndingDebit(BigDecimal endingDebit) {
        this.endingDebit = endingDebit;
    }

    public BigDecimal getEndingCredit() {
        return endingCredit;
    }

    public void setEndingCredit(BigDecimal endingCredit) {
        this.endingCredit = endingCredit;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isFloatingAccount() {
        return floatingAccount;
    }

    public void setFloatingAccount(boolean floatingAccount) {
        this.floatingAccount = floatingAccount;
    }

    public Integer getBaseAccountId() {
        return baseAccountId;
    }

    public void setBaseAccountId(Integer baseAccountId) {
        this.baseAccountId = baseAccountId;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public HashSet<TrialBalanceItem> getChilds() {
        if (childs == null) {
            childs = new HashSet<>();
        }
        return childs;
    }

    public void setChilds(HashSet<TrialBalanceItem> childs) {
        this.childs = childs;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrialBalanceItem that = (TrialBalanceItem) o;

        if (!getAccountId().equals(that.getAccountId())) return false;
        if (!getCode().equals(that.getCode())) return false;
        if (!getName().equals(that.getName())) return false;
        return getCategoryCode().equals(that.getCategoryCode());
    }

    @Override
    public int hashCode() {
        int result = getAccountId().hashCode();
        result = 31 * result + getCode().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCategoryCode().hashCode();
        return result;
    }
}
