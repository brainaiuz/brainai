package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollCategoryV2View;

import java.util.LinkedList;

public class PayrollCategoryV2AddSinksContainer extends SinksContainer {

    public PayrollCategoryV2AddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new PayrollCategoryV2View());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
