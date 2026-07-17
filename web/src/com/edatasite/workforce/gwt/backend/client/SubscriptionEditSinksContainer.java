package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.SubscriptionEditView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: admin
 * Date: Jan 16, 2010
 * Time: 12:42:20 PM
 */
public class SubscriptionEditSinksContainer extends SinksContainer {

    public SubscriptionEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new SubscriptionEditView(id));
    }
}