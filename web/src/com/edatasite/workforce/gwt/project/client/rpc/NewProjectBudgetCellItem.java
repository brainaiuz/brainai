package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/12
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProjectBudgetCellItem implements IsSerializable {
    private Integer month;
    private Integer year;

    private BigDecimal budget;
    private BigDecimal actual;

    private Boolean isTotal;

    public NewProjectBudgetCellItem() {
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public Boolean isTotal() {
        return isTotal != null ? isTotal : false;
    }

    public void setTotal(Boolean total) {
        isTotal = total;
    }
}
