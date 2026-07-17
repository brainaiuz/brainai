package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.PayrunPaymentListView;

import java.util.LinkedList;

public class PayrunPaymentListSinksContainer extends SinksContainer {

    public PayrunPaymentListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new PayrunPaymentListView(id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
