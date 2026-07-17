package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.PaypalPaymentView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2019-06-20 14:24
 */
public class PaymentGatewaysSettingsSinksContainer extends SinksContainer {

    public PaymentGatewaysSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new PaypalPaymentView());
//        addView(new StripePaymentView());
    }
}
