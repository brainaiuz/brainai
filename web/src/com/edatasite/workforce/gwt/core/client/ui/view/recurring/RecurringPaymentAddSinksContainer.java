package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class RecurringPaymentAddSinksContainer extends SinksContainer {

    public RecurringPaymentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddRecurringPaymentView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
