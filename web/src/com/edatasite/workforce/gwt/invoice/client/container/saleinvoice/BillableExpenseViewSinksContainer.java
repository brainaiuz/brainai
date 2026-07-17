package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.BillableExpenseView;

import java.util.LinkedList;

public class BillableExpenseViewSinksContainer extends SinksContainer {
    public BillableExpenseViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new BillableExpenseView(params));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
