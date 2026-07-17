package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class RecurringDeductionViewSinksContainer extends SinksContainer {

    public RecurringDeductionViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new RecurringDeductionSummaryView(id));
        addView(new AddRecurringDeductionView(id));
    }
}
