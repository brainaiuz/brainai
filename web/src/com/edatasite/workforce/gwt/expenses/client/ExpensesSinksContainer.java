package com.edatasite.workforce.gwt.expenses.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseListView;

import java.util.LinkedList;

public class ExpensesSinksContainer extends SinksContainer implements Constants {

    public ExpensesSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ExpenseListView());
    }
}


