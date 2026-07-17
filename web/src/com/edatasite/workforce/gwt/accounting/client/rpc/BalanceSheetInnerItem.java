package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * User: administrator
 * Date: 28.02.2009
 * Time: 13:05:59
 */
public class BalanceSheetInnerItem implements IsSerializable {

    private String name;
    private String code;
    private BigDecimal value;
    private Integer accountID;

    public BalanceSheetInnerItem() {
    }

    public BalanceSheetInnerItem(String name, BigDecimal value) {
        this(name, value, null);
    }

    public BalanceSheetInnerItem(String name, BigDecimal value, Integer accountID) {
        this.name = name;
        this.value = value;
        this.accountID = accountID;
    }

    public BalanceSheetInnerItem(String name, String code, BigDecimal value, Integer accountID) {
        this.name = name;
        this.code = code;
        this.value = value;
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }
}