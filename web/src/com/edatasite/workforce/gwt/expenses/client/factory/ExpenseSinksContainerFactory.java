package com.edatasite.workforce.gwt.expenses.client.factory;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.expenses.client.ExpensesSinksContainer;
import com.edatasite.workforce.gwt.expenses.client.history.ExpenseEmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.history.ExpenseReportHistoryProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.10.2008
 * Time: 15:20:58
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseSinksContainerFactory extends SinksContainerFactory {

    public ExpenseSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("expense");
    }

    public void initDefaultContainers() {
        if (!Utils.hasRole(CLIENT)) {
            ExpensesSinksContainer expenses = new ExpensesSinksContainer("expense", "Expenses");
            showPrepairedView(expenses, EXPENSES_FIRST_VIEW, EXPENSES_HOME, EXPENSES_HOME);
        }
    }

    public void registerProcessors() {
        registerHistoryProcessor("expenseReports", new ExpenseReportHistoryProcessor());
        registerHistoryProcessor("expenseemailcompose", new ExpenseEmailComposeHistoryProcessor());

    }

    public void registerMenuItems() {
        addNewMenuItem("Expense Claims", "expenseReports|add/add");
    }
}
