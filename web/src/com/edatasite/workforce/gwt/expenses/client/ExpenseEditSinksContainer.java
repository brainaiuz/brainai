package com.edatasite.workforce.gwt.expenses.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseAddEditView;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.11.2008
 * Time: 13:42:39
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseEditSinksContainer extends SinksContainer {

    public ExpenseEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public ExpenseEditSinksContainer(String name, String description, String[] params, String sectionContext) {
        super(name, description, params);
    }

    protected void initViews() {

        if (!(params.length > 1 && params[1] != null && params[1].equals(Constants.EXPENSE_VIEW))) {
            ExpenseAddEditView editReport = new ExpenseAddEditView(id);
            addView(editReport);
        }
        ExpenseSummaryView previewView;
        if (getDescription() != null){
            previewView = new ExpenseSummaryView(id, params.length == 3 ? params[2] : getDescription());
        }else{
            previewView = new ExpenseSummaryView(id);
        }
        addView(previewView);
    }
}
