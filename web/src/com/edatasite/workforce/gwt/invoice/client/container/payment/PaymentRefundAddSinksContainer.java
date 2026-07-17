package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.AddPaymentRefund;

import java.util.LinkedList;

public class PaymentRefundAddSinksContainer extends SinksContainer {
    public PaymentRefundAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isReceivable = getName().startsWith("customerRefund");
        addView(new AddPaymentRefund(isReceivable, params));
    }
}
