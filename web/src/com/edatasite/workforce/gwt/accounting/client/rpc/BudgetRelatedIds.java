package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 03.04.2009
 * Time: 15:18:13
 * To change this template use File | Settings | File Templates.
 */
public class BudgetRelatedIds implements IsSerializable {

    private Integer accountBudgetId;
    private Integer profitId;

    public Integer getAccountBudgetId() {
        return accountBudgetId;
    }

    public void setAccountBudgetId(Integer accountBudgetId) {
        this.accountBudgetId = accountBudgetId;
    }

    public Integer getProfitId() {
        return profitId;
    }

    public void setProfitId(Integer profitId) {
        this.profitId = profitId;
    }
}
