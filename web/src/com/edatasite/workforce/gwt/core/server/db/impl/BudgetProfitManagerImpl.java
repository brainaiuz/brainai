package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetedProfit;
import com.edatasite.workforce.gwt.core.server.db.BudgetProfitManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 03.04.2009
 * Time: 15:11:15
 * To change this template use File | Settings | File Templates.
 */
@Repository("budgetProfitManager")
public class BudgetProfitManagerImpl extends BaseManager<EdsBudgetedProfit> implements BudgetProfitManager {

    public BudgetProfitManagerImpl() {
        super(EdsBudgetedProfit.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<EdsBudgetedProfit> findBudgetedProfitInTheRange(Date from, Date to) {
        return find("select bp from EdsBudgetedProfit bp  where bp.date between ? and ?", from, to);
    }
}
