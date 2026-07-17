package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.AllPricingView;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.SubscriptionHistoryListView;

import java.util.LinkedList;

/**
 * User: Unni
 * Date: Nov 25, 2008
 * Time: 4:56:36 PM
 */
public class MyAccountSinksContainer extends SinksContainer {

    public MyAccountSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AllPricingView());
        addView(new SubscriptionHistoryListView());
    }
}