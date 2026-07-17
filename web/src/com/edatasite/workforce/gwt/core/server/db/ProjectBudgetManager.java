package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProjectBudget;
import com.edatasite.workforce.core.domain.EdsProjectBudgetItem;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/18/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectBudgetManager extends Manager<EdsProjectBudget> {
    EdsProjectBudget getBudgetByProject(Integer projectID);

    HashMap<Integer, HashMap<String, EdsProjectBudgetItem>> getProjectBudgetItems(Integer accountID, EdsProjectBudget projectBudget, boolean isDetailedPurchasesEnabled);

    List<EdsAccount> getBudgetAccounts(EdsProjectBudget projectBudget, String type);

    HashMap<Integer, BigDecimal> getAccountsActualByProjectAndMonth(Integer accountID, Integer projectID, String type, Date startDate, Date endDate);

    List<EdsAccount> getAccountsForProjectBudget(ListingFilterParameter filterParameter);

    void deleteProjectBudgetItems(Integer projectBudgetID);

    BigDecimal getProjectEmployeeCostByMonth(Integer projectID, Date startDate, Date endDate);

    BigDecimal getProjectEmployeeBudget(Integer projectID);

    BigDecimal getProjectPurchasesByMonth(Integer projectID, Date startDate, Date endDate);

//	HashMap<Integer, BigDecimal> getProjectsActuals(List<Integer> projectID);

    BigDecimal getProjectIncome(Integer projectID);

    BigDecimal getProjectExpense(Integer projectID);

//	HashMap<Integer, BigDecimal> getProjectsCosts(List<Integer> projectID);

    BigDecimal getProjectPlanedExpense(Integer projectID);

    BigDecimal getProjectPlanedIncome(Integer projectID);

    HashMap<Integer, Double> getPlannedExpenseByProjectIDs(String projectIDs);

    HashMap<Integer, Double> getPlannedExpenseFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees);

    HashMap<Integer, Double> getPlannedIncomeByProjectIDs(String projectIDs);

    HashMap<Integer, Double> getPlannedIncomeFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees);

    HashMap<Integer, BigDecimal> getActualExpenseByProjectIDs(String projectIDs);

    HashMap<Integer, BigDecimal> getActualExpenseFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees);

    HashMap<Integer, BigDecimal> getActualIncomeByProjectIDs(String projectIDs);

    HashMap<Integer, BigDecimal> getActualIncomeFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees);

    List<EdsProjectBudgetItem> getBudgetItems(Integer budgetID, String type, Integer accountID);

    EdsProjectBudgetItem getBudgetItem(Integer budgetID, String type, Integer accountID, Integer month, Integer year);

    EdsProjectBudgetItem getTotalBudgetItem(Integer budgetID, String type, Integer accountID);
}
