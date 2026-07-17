package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.PayrollPaymentView;

import java.util.LinkedList;

public class PayrollPaymentViewSinksContainer extends SinksContainer {

    public PayrollPaymentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new PayrollPaymentView(id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
