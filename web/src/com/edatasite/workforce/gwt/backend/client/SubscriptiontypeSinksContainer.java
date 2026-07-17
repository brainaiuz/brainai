package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.SubscriptiontypeEditView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class SubscriptiontypeSinksContainer extends SinksContainer {

	public SubscriptiontypeSinksContainer(String name, String description, String[] strings) {
		super(name, description, strings);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new SubscriptiontypeEditView(id));
	}
}