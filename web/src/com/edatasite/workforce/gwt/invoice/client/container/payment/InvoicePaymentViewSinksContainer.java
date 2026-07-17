package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.InvoicePaymentView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Akmal
 * Date: 12-Mar-2009
 * Time: 17:24:45
 * To change this template use File | Settings | File Templates.
 */
public class InvoicePaymentViewSinksContainer extends SinksContainer {
    public InvoicePaymentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        InvoicePaymentView paymentView = new InvoicePaymentView(id, params);
        addView(paymentView);
    }
}
