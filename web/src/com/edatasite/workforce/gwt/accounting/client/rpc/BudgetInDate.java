package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 16.03.2009
 * Time: 19:04:15
 * To change this template use File | Settings | File Templates.
 */
public class BudgetInDate implements IsSerializable {

    private Integer accountBudgetID;
    private Integer profitID;
    private Integer entityID;
    private BigDecimal value; // expected
    private BigDecimal actualValue;
    private Date date;
    private Integer groupId;

    public BudgetInDate() {

    }

    public BudgetInDate(Date date, BigDecimal value) {
        this(null, null, date, value);
    }

    public BudgetInDate(Date date, BigDecimal value, BigDecimal actualValue) {
        this(null, null, date, value, actualValue);
    }

    public BudgetInDate(Integer accountBudgetID, Integer profitID, Date date, BigDecimal value) {
        this.accountBudgetID = accountBudgetID;
        this.profitID = profitID;
        this.date = date;
        this.value = value;
    }

    public BudgetInDate(Integer accountBudgetID, Integer profitID, Date date, BigDecimal value, BigDecimal actualValue) {
        this.accountBudgetID = accountBudgetID;
        this.profitID = profitID;
        this.date = date;
        this.value = value;
        this.actualValue = actualValue;
    }

    public Integer getAccountBudgetID() {
        return accountBudgetID;
    }

    public void setAccountBudgetID(Integer accountBudgetID) {
        this.accountBudgetID = accountBudgetID;
    }

    public BigDecimal getValue() {
        return value != null ? value : BigDecimal.ZERO;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getActualValue() {
        return actualValue != null ? actualValue : BigDecimal.ZERO;
    }

    public void setActualValue(final BigDecimal actualValue) {
        this.actualValue = actualValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getProfitID() {
        return profitID;
    }

    public void setProfitID(Integer profitID) {
        this.profitID = profitID;
    }

    public Integer getEntityID() {
        return this.entityID;
    }

    public void setEntityID(final Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
}
