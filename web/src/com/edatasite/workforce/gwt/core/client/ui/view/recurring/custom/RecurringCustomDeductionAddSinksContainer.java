package com.edatasite.workforce.gwt.core.client.ui.view.recurring.custom;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class RecurringCustomDeductionAddSinksContainer extends SinksContainer {

    public RecurringCustomDeductionAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddRecurringCustomDeductionView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
