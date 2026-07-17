package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 10:28:20
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseManager extends Manager<EdsExpense> {

    List<EdsEmployee> getEmployeesWithExpenses();

    List<EdsExpense> getExpenses(Integer employeeID);

    List<EdsExpense> getExpenseByReport(Integer reportID);

    EdsExpense getExpense(Integer objectID);

    List<EdsExpense> getBillableExpenses(ListingFilterParameter fp);

    List<EdsExpense> getExpenseByInvoice(Integer invoiceID);

    EdsExpenseReport getOldExpense(ExpenseReportsListItem expenseReportsListItem);

    Integer[] getRelatedTimesheetsByExpense(Integer expenseId);

    void removeRelatedTimesheetsFromExpense(Integer expenseId);
}
