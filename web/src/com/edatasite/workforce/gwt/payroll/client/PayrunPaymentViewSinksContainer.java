package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.PayrunPaymentView;

import java.util.LinkedList;

public class PayrunPaymentViewSinksContainer extends SinksContainer {

    public PayrunPaymentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new PayrunPaymentView(id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
