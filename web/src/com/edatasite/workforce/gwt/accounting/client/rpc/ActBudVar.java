package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 28.03.2009
 * Time: 13:00:50
 * To change this template use File | Settings | File Templates.
 */
public class ActBudVar implements IsSerializable {

    private BigDecimal actual;
    private BigDecimal budget;


    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getVariance() {
        if (actual != null && budget != null && budget.intValue() != 0) {
            return AccountingConstants.HUNDRED.multiply(actual.subtract(budget)).divide(budget, 2, RoundingMode.HALF_UP);
        } else {
            return null;
        }
    }

//    public boolean isEmpty(){
//        return actual == null && budget == null;
//    }


}
