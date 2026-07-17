package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.AllPricingMaterialView;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.SubscriptionHistoryNewListView;

import java.util.LinkedList;

/**
 * User: Anvar Akramov
 * Date: Sep 11, 2018
 * Time: 4:56:36 PM
 */
public class MyAccountNewSinksContainer extends SinksContainer {

    public MyAccountNewSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AllPricingMaterialView());
        addView(new SubscriptionHistoryNewListView());
        addView(new EmployeeListView(EmployeeListView.FROM_PRICING));
    }
}