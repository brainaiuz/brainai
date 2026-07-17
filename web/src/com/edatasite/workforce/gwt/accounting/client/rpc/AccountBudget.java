package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 13.03.2009
 * Time: 16:01:27
 * To change this template use File | Settings | File Templates.
 */
public class AccountBudget implements IsSerializable {

    private Integer id;
    private Integer accountID;
    private Integer departmentID;
    private int month;
    private int year;
    private Date date;
    private BigDecimal budget;
    private Integer entityID;
    private String type;
    private String columnCode;
    private Integer groupId;
    private Integer budgetManagerId;

    private Integer profitId;
    private BigDecimal grossProfit;
    private BigDecimal netProfit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public Integer getProfitId() {
        return profitId;
    }

    public void setProfitId(Integer profitId) {
        this.profitId = profitId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getBudgetManagerId() {
        return this.budgetManagerId;
    }

    public void setBudgetManagerId(final Integer budgetManagerId) {
        this.budgetManagerId = budgetManagerId;
    }

    public String getColumnCode() {
        return this.columnCode;
    }

    public void setColumnCode(final String columnCode) {
        this.columnCode = columnCode;
    }

    public Integer getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAccountID() {
        return this.accountID;
    }

    public void setAccountID(final Integer accountID) {
        this.accountID = accountID;
    }
}
