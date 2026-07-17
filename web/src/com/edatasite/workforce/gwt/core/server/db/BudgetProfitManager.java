package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetedProfit;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 03.04.2009
 * Time: 15:10:04
 * To change this template use File | Settings | File Templates.
 */
public interface BudgetProfitManager extends Manager<EdsBudgetedProfit> {
    List<EdsBudgetedProfit> findBudgetedProfitInTheRange(Date from, Date to);
}
