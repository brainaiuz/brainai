package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsAccountBudget;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 13.03.2009
 * Time: 15:51:27
 * To change this template use File | Settings | File Templates.
 */
public interface AccountBudgetManager extends Manager<EdsAccountBudget> {

    List<EdsAccountBudget> findBudgetedAccountInTheRange(Date from, Date to, String departmentAndTreeChildIDs, boolean isPNL);

    EdsAccountBudget findAccountBudget(Integer accountID, Integer departmentID, Date from, Date to);
}
