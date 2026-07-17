package com.edatasite.workforce.gwt.invoice.client.container.expense;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ProjectBaseExpenseAddView;

import java.util.LinkedList;

/**
 * Created by Normurod on 10/23/2016.
 */
public class ProjectBaseExpenseAddSinksContainer extends SinksContainer {

    public ProjectBaseExpenseAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ProjectBaseExpenseAddView());
    }
}
