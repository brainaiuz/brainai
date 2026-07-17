package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.PayrollPaymentAddView;

import java.util.LinkedList;

public class PayrollPaymentAddSinksContainer  extends SinksContainer {

    public PayrollPaymentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        Integer addPaymentID = null;
        if (params != null && params.length > 1) {
            addPaymentID = Integer.parseInt(params[1]);
            addView(new PayrollPaymentAddView(addPaymentID));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
