package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.PriceOrderConfirmView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 1/28/12
 * Time: 4:58 PM
 */
public class PricingOrderSinksContainer extends SinksContainer {

    public PricingOrderSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length >= 2) {
            addView(new PriceOrderConfirmView(params[0], Integer.valueOf(params[1])));
        }
    }
}
