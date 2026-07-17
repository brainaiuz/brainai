package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.PayrunPaymentAddView;

import java.util.LinkedList;

public class PayrunPaymentAddSinksContainer extends SinksContainer {

    public PayrunPaymentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        Integer payrunID = null;
        if (params != null && params.length > 1) {
            payrunID = Integer.parseInt(params[1]);
            addView(new PayrunPaymentAddView(payrunID));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
