package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.RemoveTestEmailView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class RemoveTestEmailSinksContainer extends SinksContainer {
	public RemoveTestEmailSinksContainer(String name, String description) {
		super(name, description);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new RemoveTestEmailView());
	}
}