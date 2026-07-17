package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsAccountBudget;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.gwt.core.server.db.AccountBudgetManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 13.03.2009
 * Time: 15:51:44
 * To change this template use File | Settings | File Templates.
 */
@Repository("accountBudgetManager")
public class AccountBudgetManagerImpl extends BaseManager<EdsAccountBudget> implements AccountBudgetManager {

    public AccountBudgetManagerImpl() {
        super(EdsAccountBudget.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<EdsAccountBudget> findBudgetedAccountInTheRange(Date from, Date to, String departmentAndTreeChildIDs, boolean isPNL) {
        if (departmentAndTreeChildIDs != null && departmentAndTreeChildIDs.trim().length() > 0) {
            if (isPNL) {
                return find("select ab from EdsAccountBudget ab join fetch ab.account where ab.department.objectID in (" + departmentAndTreeChildIDs + ") and (ab.account.accountType.category = ? or ab.account.accountType.category = ?) " +
                        " and ab.date between ? and ? order by ab.account.objectID, ab.date", EdsAccountType.REVENUE, EdsAccountType.EXPENSES, from, to);
            } else {
                return find("select ab from EdsAccountBudget ab join fetch ab.account where ab.department.objectID in (" + departmentAndTreeChildIDs + ") and ab.date between ? and ? order by ab.account.objectID, ab.date", from, to);
            }
        } else {
            if (isPNL) {
                return find("select ab from EdsAccountBudget ab join fetch ab.account where ab.department is null and (ab.account.accountType.category = ? or ab.account.accountType.category = ?) " +
                        " and ab.date between ? and ? order by ab.account.objectID, ab.date", EdsAccountType.REVENUE, EdsAccountType.EXPENSES, from, to);
            } else {
                return find("select ab from EdsAccountBudget ab join fetch ab.account where ab.department is null and ab.date between ? and ? order by ab.account.objectID, ab.date", from, to);
            }
        }
    }

    @Override
    public EdsAccountBudget findAccountBudget(Integer accountID, Integer departmentID, Date from, Date to) {
        if (departmentID != null) {
            return (EdsAccountBudget) findSingle("select ab from EdsAccountBudget ab where ab.account.objectID = ? and ab.department.objectID = ? and ab.date between ? and ? order by ab.objectID desc", accountID, departmentID, from, to);
        } else {
            return (EdsAccountBudget) findSingle("select ab from EdsAccountBudget ab where ab.account.objectID = ? and ab.department is null and ab.date between ? and ? order by ab.objectID desc", accountID, from, to);
        }
    }
}
