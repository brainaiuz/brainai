package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.SubscriptionManagementSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 4/18/12
 * Time: 6:29 PM
 */
public class SubscriptionManagementViewSinksContainer extends SinksContainer {

    public SubscriptionManagementViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new SubscriptionManagementSummaryView(id));
    }
}