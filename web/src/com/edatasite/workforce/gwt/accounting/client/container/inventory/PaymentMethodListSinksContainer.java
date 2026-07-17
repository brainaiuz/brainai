package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.PaymentMethodListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: Sep 16, 2016
 */
public class PaymentMethodListSinksContainer extends SinksContainer {

    public PaymentMethodListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new PaymentMethodListView());
    }
}
